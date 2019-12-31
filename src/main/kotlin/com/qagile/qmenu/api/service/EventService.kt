package com.qagile.qmenu.api.service

import com.qagile.qmenu.api.domain.Event
import com.qagile.qmenu.api.entities.EventException
import com.qagile.qmenu.api.entities.request.CreateEventRequest
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

    fun checkEvent(createEventRequest: CreateEventRequest, applicationUserId: Long): Single<Event> {
        logger.info("Start checkEvent, createEventRequest: $createEventRequest")

        val events = Event()
        return saveEvent(events.convertToEvents(createEventRequest, applicationUserId))
            .doOnSuccess {
                logger.info("End checkEvent to Feed Send Elastic Search - event: $it")
            }.doOnError {
                logger.error("Error checkEvent - saveCompany error: $it")
            }
    }

    fun updateEvent(newEvent: Event): Single<Event> {
        logger.info("Start updateCompany, newCompany: $newEvent")

        return findById(newEvent)
            .filter {
                it.isPresent
            }.flatMapSingle {
                save(newEvent.mergeDataCompany(newEvent, it.get()))
            }.doOnSuccess {
                logger.info("End updateCompany, newCompany: $it")
            }.doOnError {
                logger.info("Error updateCompany, error: $it")
            }.onErrorResumeNext {
                Single.error(EventException("400", Translator.getMessage(ErrorCode.EVENT_DOES_NOT_EXIST)))
            }
    }

    fun saveEvent(event: Event): Single<Event> {
        logger.info("Start saveCompany, company: $event")

        return save(event)
            .doOnSuccess {
                logger.info("End saveCompany, company: $it")
            }.doOnError {
                logger.info("Error saveCompany, error: $it")
            }.onErrorResumeNext {
                Single.error(EventException("400", Translator.getMessage(ErrorCode.EVENT_TRY_AGAIN_LATER)))
            }
    }

    fun deleteEvent(event: Event): Single<Unit> {
        logger.info("Start deleteCompany, company: $event")

        return findById(event)
            .filter {
                it.isPresent
            }.flatMapSingle {
                delete(it.get())
            }.doOnSuccess {
                logger.info("End deleteCompany, company: $it")
            }.doOnError {
                logger.info("Error saveCompany, error: $it")
            }.onErrorResumeNext {
                Single.error(EventException("400", Translator.getMessage(ErrorCode.EVENT_DOES_NOT_EXIST)))
            }
    }

    fun findById(event: Event) = just(eventRepository.findById(event.id!!))

    private fun save(event: Event) = just(eventRepository.save(event))

    private fun delete(event: Event) = just(eventRepository.delete(event))
}
