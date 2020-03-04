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

    fun checkUpdateEvent(updateEventRequest: UpdateEventRequest, userId: Long): Single<Event> {
        logger.info("Start checkUpdateEvent by userId: $userId with request: $updateEventRequest")

        return updateEvent(updateEventRequest, userId)
    }

    fun checkRemoveEvent(deleteRequest: DeleteRequest, userId: Long): Single<DeleteResponse> {
        logger.info("Start checkRemoveEvent by userId: $userId with request: $deleteRequest")

        return removeEvent(Event(deleteRequest.id), userId)
    }

    fun checkCreateEvent(createEventRequest: CreateEventRequest, userId: Long): Single<Event> {
        logger.info("Start checkEvent by userId: $userId with request: $createEventRequest")

        return saveEvent(Event().convertToEvents(createEventRequest, userId))
    }

    fun updateEvent(updateEventRequest: UpdateEventRequest, userId: Long): Single<Event> {
        logger.info("Start updateEvent by userId: $userId with request: $updateEventRequest")

        return findById(updateEventRequest.id!!)
            .filter {
                it.isPresent
            }.flatMapSingle {
                save(Event().mergeDataCompany(updateEventRequest, it.get()))
            }.doOnSuccess {
                logger.info("End updateEvent by userId: $userId with response: $it")
                logger.info("End updateEvent by userId: $userId with request: $it to feed")
            }.doOnError {
                logger.error("Error updateEvent by userId: $userId with error: ${it.getError()}")
            }.onErrorResumeNext {
                Single.error(EventException("400", Translator.getMessage(ErrorCode.EVENT_DOES_NOT_EXIST)))
            }
    }

    fun saveEvent(event: Event): Single<Event> {
        logger.info("Start saveEvent by userId: ${event.userId} with request: $event")

        return save(event)
            .doOnSuccess {
                logger.info("End saveEvent by userId: ${event.userId} with response: $it")
                logger.info("End saveEvent by userId: ${event.userId} with request: $it to feed")
            }.doOnError {
                logger.error("Error saveEvent by userId: ${event.userId} with error: ${it.getError()}")
            }.onErrorResumeNext {
                Single.error(EventException("400", Translator.getMessage(ErrorCode.EVENT_TRY_AGAIN_LATER)))
            }
    }

    fun removeEvent(event: Event, userId: Long): Single<DeleteResponse> {
        logger.info("Start removeEvent by userId: $userId with request: $event")

        return findById(event.id!!)
            .filter {
                it.isPresent
            }.flatMapSingle {
                remove(it.get()).map {
                    DeleteResponse().getDeleteEventResponse(event.id, userId, Translator.getMessage(SuccessCode.EVENT_REMOVE))
                }
            }.doOnSuccess {
                logger.info("End removeEvent by userId: $userId with response: $it")
                logger.info("End removeEvent by userId: $userId with request: $it to feed")
            }.doOnError {
                logger.error("Error removeEvent by userId: $userId with error: ${it.getError()}")
            }.onErrorResumeNext {
                Single.error(EventException("400", Translator.getMessage(ErrorCode.EVENT_DOES_NOT_EXIST)))
            }
    }

    fun findById(id: String) = just(eventRepository.findById(id))

    private fun save(event: Event) = just(eventRepository.save(event))

    private fun remove(event: Event) = just(eventRepository.deleteById(event.id.toString()))
}
