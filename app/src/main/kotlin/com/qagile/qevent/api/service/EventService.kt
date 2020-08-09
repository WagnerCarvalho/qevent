package com.qagile.qevent.api.service

import com.qagile.qevent.api.domain.Event
import com.qagile.qevent.api.entities.EventStatus
import com.qagile.qevent.api.entities.exception.EventException
import com.qagile.qevent.api.entities.request.CreateEventRequest
import com.qagile.qevent.api.entities.request.DeleteRequest
import com.qagile.qevent.api.entities.request.UpdateEventRequest
import com.qagile.qevent.api.entities.response.DeleteResponse
import com.qagile.qevent.api.manager.exception.ManagerException
import com.qagile.qevent.api.modules.qacquirer.service.QacquirerService
import com.qagile.qevent.api.modules.quser.entities.request.CreateUserEventRequest
import com.qagile.qevent.api.modules.quser.entities.response.CreateUserEventResponse
import com.qagile.qevent.api.modules.quser.service.UserEventService
import com.qagile.qevent.api.repository.EventRepository
import com.qagile.qevent.api.utils.*
import io.reactivex.Single
import io.reactivex.Single.just
import java.lang.Exception
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class EventService {
    private val logger = LoggerFactory.getLogger(EventService::class.java)

    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var userEventService: UserEventService

    @Autowired
    private lateinit var managerException: ManagerException

    @Autowired
    private lateinit var qacquirerService: QacquirerService

    @Value("\${image_default}")
    lateinit var imageDefault: String

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

        return saveEvent(Event().convertToEvents(createEventRequest, userId, imageDefault))
    }

    fun checkEventByUser(userId: Long): Single<MutableList<Event>> {
        logger.info("Start checkEventByUser by userId: $userId")

        return getEventByUser(userId)
            .doOnSuccess {
                logger.info("End checkEventByUser by userId: $userId with response: $it")
            }.doOnError {
                logger.info("Error checkEventByUser by userId: $userId with error: ${it.getError()}")
            }
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
                getTestToken(updateEventRequest.id, userId.toString()).subscribe()
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
                    DeleteResponse().getDeleteEventResponse(event.id!!, userId, Translator.getMessage(SuccessCode.EVENT_REMOVE))
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

    fun createUserEvent(userId: Long, request: CreateUserEventRequest, apiKey: String): Single<CreateUserEventResponse> {
        logger.info("Start createUserEvent by userId: $userId with request: $request")

        return findById(request.eventId)
            .filter {
                it.isPresent
            }.flatMapSingle {
                userEventService.createUserEvent(userId, request, apiKey)
            }.doOnSuccess {
                logger.info("End createUserEvent by userId: $userId with response: $it")
            }.doOnError {
                logger.error("Error createUserEvent by userId: $userId with error: ${it.getError()}")
            }.onErrorResumeNext {
                Single.error(managerException.check(it, ErrorCode.USER_EVENT_NOT_CREATE))
            }
    }

    fun checkEvent(id: String, userId: Long): Single<Event> {
        logger.info("Start checkEvent by userId: $userId with id: $id")

        return findById(id)
            .filter {
                it.isPresent
            }.flatMapSingle {
                just(it.get())
            }.doOnSuccess {
                logger.info("End checkEvent by userId: $userId with response: $it")
            }.doOnError {
                logger.error("Error checkEvent by userId: $userId with error: ${it.getError()}")
            }.onErrorResumeNext {
                Single.error(EventException("400", Translator.getMessage(ErrorCode.EVENT_DOES_NOT_EXIST)))
            }
    }

    fun checkEventAll(status: EventStatus): Single<MutableList<Event>> {
        logger.info("Start checkEventAll")

        return getEventAll(status)
            .doOnSuccess {
                logger.info("End checkEventAll")
            }.doOnError {
                logger.error("End checkEventAll")
            }
    }

    private fun getTestToken(eventId: String, userId: String): Single<Boolean> {
        logger.info("EventService - getTestToken with eventId: - $eventId")
        return Single.fromCallable {
            try {
                val response = qacquirerService.searchUser(eventId, userId).toFuture().objectToString()
                logger.info("EventService - getTestToken with response: - $response")
                true
            } catch (exception: Exception) {
                logger.error("EventService - getTestToken with exception: - ${exception.message}")
                false
            }
        }
    }

    fun findById(id: String) = just(eventRepository.findById(id))

    private fun save(event: Event) = just(eventRepository.save(event))

    private fun remove(event: Event) = just(eventRepository.deleteById(event.id.toString()))

    private fun getEventByUser(userId: Long) = just(eventRepository.findByUserId(userId))

    private fun getEventAll(status: EventStatus) = just(eventRepository.findByEventStatus(status.name))
}
