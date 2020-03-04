package com.qagile.qevent.api.service

import com.qagile.qevent.api.domain.Event
import com.qagile.qevent.api.entities.exception.EventException
import com.qagile.qevent.api.entities.request.CreateEventRequest
import com.qagile.qevent.api.entities.request.DeleteRequest
import com.qagile.qevent.api.entities.request.UpdateEventRequest
import com.qagile.qevent.api.entities.response.DeleteResponse
import com.qagile.qevent.api.repository.EventRepository
import com.qagile.qevent.api.utils.ErrorCode
import com.qagile.qevent.api.utils.SuccessCode
import com.qagile.qevent.api.utils.Translator
import com.qagile.qevent.api.utils.getError
import io.reactivex.Single
import io.reactivex.Single.just
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EventService {
    private val logger = LoggerFactory.getLogger(EventService::class.java)

    @Autowired
    private lateinit var eventRepository: EventRepository

    fun checkUpdateEvent(updateEventRequest: UpdateEventRequest, applicationUserId: Long): Single<Event> {
        logger.info("Start checkUpdateEvent by applicationUserId: $applicationUserId with request: $updateEventRequest")

        return updateEvent(updateEventRequest, applicationUserId)
    }

    fun checkRemoveEvent(deleteRequest: DeleteRequest, applicationUserId: Long): Single<DeleteResponse> {
        logger.info("Start checkRemoveEvent by applicationUserId: $applicationUserId with request: $deleteRequest")

        return removeEvent(Event(deleteRequest.id), applicationUserId)
    }

    fun checkCreateEvent(createEventRequest: CreateEventRequest, applicationUserId: Long): Single<Event> {
        logger.info("Start checkEvent by applicationUserId: $applicationUserId with request: $createEventRequest")

        return saveEvent(Event().convertToEvents(createEventRequest, applicationUserId))
    }

    fun updateEvent(updateEventRequest: UpdateEventRequest, applicationUserId: Long): Single<Event> {
        logger.info("Start updateEvent by applicationUserId: $applicationUserId with request: $updateEventRequest")

        return findById(updateEventRequest.id!!)
            .filter {
                it.isPresent
            }.flatMapSingle {
                save(Event().mergeDataCompany(updateEventRequest, it.get()))
            }.doOnSuccess {
                logger.info("End updateEvent by applicationUserId: $applicationUserId with response: $it")
                logger.info("End updateEvent by applicationUserId: $applicationUserId with request: $it to feed")
            }.doOnError {
                logger.error("Error updateEvent by applicationUserId: $applicationUserId with error: ${it.getError()}")
            }.onErrorResumeNext {
                Single.error(EventException("400", Translator.getMessage(ErrorCode.EVENT_DOES_NOT_EXIST)))
            }
    }

    fun saveEvent(event: Event): Single<Event> {
        logger.info("Start saveEvent by applicationUserId: ${event.applicationUserId} with request: $event")

        return save(event)
            .doOnSuccess {
                logger.info("End saveEvent by applicationUserId: ${event.applicationUserId} with response: $it")
                logger.info("End saveEvent by applicationUserId: ${event.applicationUserId} with request: $it to feed")
            }.doOnError {
                logger.error("Error saveEvent by applicationUserId: ${event.applicationUserId} with error: ${it.getError()}")
            }.onErrorResumeNext {
                Single.error(EventException("400", Translator.getMessage(ErrorCode.EVENT_TRY_AGAIN_LATER)))
            }
    }

    fun removeEvent(event: Event, applicationUserId: Long): Single<DeleteResponse> {
        logger.info("Start removeEvent by applicationUserId: $applicationUserId with request: $event")

        return findById(event.id!!)
            .filter {
                it.isPresent
            }.flatMapSingle {
                remove(it.get()).map {
                    DeleteResponse().getDeleteEventResponse(event.id, applicationUserId, Translator.getMessage(SuccessCode.EVENT_REMOVE))
                }
            }.doOnSuccess {
                logger.info("End removeEvent by applicationUserId: $applicationUserId with response: $it")
                logger.info("End removeEvent by applicationUserId: $applicationUserId with request: $it to feed")
            }.doOnError {
                logger.error("Error removeEvent by applicationUserId: $applicationUserId with error: ${it.getError()}")
            }.onErrorResumeNext {
                Single.error(EventException("400", Translator.getMessage(ErrorCode.EVENT_DOES_NOT_EXIST)))
            }
    }

    fun findById(id: String) = just(eventRepository.findById(id))

    private fun save(event: Event) = just(eventRepository.save(event))

    private fun remove(event: Event) = just(eventRepository.deleteById(event.id.toString()))
}
