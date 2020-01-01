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

        return removeEvent(Event(deleteEventRequest.id), applicationUserId)
    }

    fun checkCreateEvent(createEventRequest: CreateEventRequest, applicationUserId: Long): Single<Event> {
        logger.info("Start checkEvent, createEventRequest: $createEventRequest")

        val events = Event()
        return saveEvent(events.convertToEvents(createEventRequest, applicationUserId))
            .doOnSuccess {
                logger.info("End checkEvent to Feed Send Elastic Search - event: $it")
            }.doOnError {
                logger.error("Error checkEvent - saveEvent error: $it")
            }
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
        logger.info("Start saveEvent,  event: $event")

        return save(event)
            .doOnSuccess {
                logger.info("End saveEvent, company: $it")
            }.doOnError {
                logger.info("Error saveEvent, error: $it")
            }.onErrorResumeNext {
                Single.error(EventException("400", Translator.getMessage(ErrorCode.EVENT_TRY_AGAIN_LATER)))
            }
    }

    fun removeEvent(event: Event, applicationUserId: Long): Single<DeleteEventResponse> {
        logger.info("Start deleteEvent, event: $event")

        return findById(event.id!!)
            .filter {
                it.isPresent
            }.flatMapSingle {
                remove(it.get()).map { DeleteEventResponse().getDeleteEventResponse(event.id, applicationUserId) }
            }.doOnSuccess {
                logger.info("End deleteEvent, event: $it")
            }.doOnError {
                logger.info("Error deleteEvent, error: $it")
            }.onErrorResumeNext {
                Single.error(EventException("400", Translator.getMessage(ErrorCode.EVENT_DOES_NOT_EXIST)))
            }
    }

    fun findById(id: String) = just(eventRepository.findById(id))

    private fun save(event: Event) = just(eventRepository.save(event))

    private fun remove(event: Event) = just(eventRepository.deleteById(event.id.toString()))
}
