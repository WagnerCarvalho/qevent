package com.qagile.qmenu.api.service

import com.qagile.qmenu.api.domain.Event
import com.qagile.qmenu.api.entities.EventException
import com.qagile.qmenu.api.entities.request.CreateEventRequest
import com.qagile.qmenu.api.entities.request.DeleteEventRequest
import com.qagile.qmenu.api.entities.response.DeleteEventResponse
import com.qagile.qmenu.api.repository.EventRepository
import com.qagile.qmenu.api.utils.ErrorCode
import com.qagile.qmenu.api.utils.Translator
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

    fun checkRemoveEvent(deleteEventRequest: DeleteEventRequest, applicationUserId: Long): Single<DeleteEventResponse> {
        logger.info("Start checkRemoveEvent by applicationUserId: $applicationUserId with request: $deleteEventRequest")

        return removeEvent(Event(deleteEventRequest.id), applicationUserId)
    }

    fun checkCreateEvent(createEventRequest: CreateEventRequest, applicationUserId: Long): Single<Event> {
        logger.info("Start checkEvent by applicationUserId: $applicationUserId with request: $createEventRequest")

        return saveEvent(Event().convertToEvents(createEventRequest, applicationUserId))
    }

    fun updateEvent(newEvent: Event): Single<Event> {
        logger.info("Start updateEvent, newEvent: $newEvent")

        return findById(newEvent.id!!)
            .filter {
                it.isPresent
            }.flatMapSingle {
                save(newEvent.mergeDataCompany(newEvent, it.get()))
            }.doOnSuccess {
                logger.info("End updateEvent, newEvent: $it")
            }.doOnError {
                logger.info("Error updateEvent, error: $it")
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
                logger.info("Error saveEvent by applicationUserId: ${event.applicationUserId} with error: $it")
            }.onErrorResumeNext {
                Single.error(EventException("400", Translator.getMessage(ErrorCode.EVENT_TRY_AGAIN_LATER)))
            }
    }

    fun removeEvent(event: Event, applicationUserId: Long): Single<DeleteEventResponse> {
        logger.info("Start removeEvent by applicationUserId: $applicationUserId with request: $event")

        return findById(event.id!!)
            .filter {
                it.isPresent
            }.flatMapSingle {
                remove(it.get()).map {
                    DeleteEventResponse().getDeleteEventResponse(event.id, applicationUserId)
                }
            }.doOnSuccess {
                logger.info("End removeEvent by applicationUserId: $applicationUserId with response: $it")
                logger.info("End removeEvent by applicationUserId: $applicationUserId with request: $it to feed")
            }.doOnError {
                logger.info("Error removeEvent by applicationUserId: $applicationUserId with error: $event")
            }.onErrorResumeNext {
                Single.error(EventException("400", Translator.getMessage(ErrorCode.EVENT_DOES_NOT_EXIST)))
            }
    }

    fun findById(id: String) = just(eventRepository.findById(id))

    private fun save(event: Event) = just(eventRepository.save(event))

    private fun remove(event: Event) = just(eventRepository.deleteById(event.id.toString()))
}
