package com.qagile.qevent.api.service

import com.qagile.qevent.api.domain.Event
import com.qagile.qevent.api.entities.EventLocation
import com.qagile.qevent.api.entities.EventPlace
import com.qagile.qevent.api.entities.request.UpdateEventRequest
import com.qagile.qevent.api.modules.quser.entities.request.CreateUserEventRequest
import com.qagile.qevent.api.modules.quser.entities.response.CreateUserEventResponse
import com.qagile.qevent.api.modules.quser.service.UserEventService
import com.qagile.qevent.api.repository.EventRepository
import io.reactivex.Single.just
import java.util.Optional
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`doThrow`
import org.mockito.Mockito.`mock`
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener

@RunWith(SpringRunner::class)
@SpringBootTest
@TestExecutionListeners(DependencyInjectionTestExecutionListener::class)
class EventServiceTest {

    @Autowired
    private lateinit var eventService: EventService

    @Autowired
//    @MockBean
    private lateinit var eventRepository: EventRepository

    @Autowired
//    @MockBean
    private lateinit var userEventService: UserEventService

    val apikey = "api-gateway"

    private fun getEvent(name: String, description: String): Event {
        val eventLocation = EventLocation(lat = -23.4954556, lng = -46.6406668)
        val eventAddress = EventPlace(address = "Rua Copacabana 160, Casa 08", neighborhood = "Santana",
            city = "São Paulo", state = "SP", location = eventLocation)

        return Event(userId = 1, name = name, description = description,
            email = "eletrosho@eletrosho.com.br", place = eventAddress, imageUrl = "test.com.br")
    }

    @Test
    fun test_update_eve() {
        val response = eventService.getTestToken("5f308239f2da7907a2610a6c", "11").toFuture()
        val aaa = response
    }

    @Test
    fun test_update_event_ok() {
        val oldEvent = getEvent("coca-cola", "competicao para beber")
        val newEvent = UpdateEventRequest(id = oldEvent.id!!, imageUrl = "http://qagile.com.br/eletroshop.png")

        val responseFindById: Optional<Event> = Optional.ofNullable(oldEvent)
        `when`(eventRepository.findById(newEvent.id.toString())).thenReturn(responseFindById)

        val responseNewEvent = Event().mergeDataCompany(newEvent, responseFindById.get())
        `when`(eventRepository.save(responseNewEvent)).thenReturn(responseNewEvent)

        val expected: Event? = eventService.updateEvent(newEvent, 123L).toFuture().get()

        Assert.assertEquals(true, expected?.name == oldEvent.name)
        Assert.assertEquals(true, expected?.imageUrl == newEvent.imageUrl)
    }

    @Test
    fun test_update_event_error() {
        val newEvent = UpdateEventRequest(id = "5e0641f3c8f94e5ddad6591f", name = "Rock in Rio Brasil", imageUrl = "http://qagile.com.br/eletroshop.png")
        try {
            eventService.updateEvent(newEvent, 123L).toFuture().get()
        } catch (ex: Exception) {
            Assert.assertEquals("com.qagile.qevent.api.entities.exception.EventException: O evento não está cadastrado!", ex.message)
        }
    }

    @Test
    fun test_save_event_ok() {
        val event = getEvent("ElectroShop", "festa do cerveja")

        `when`(eventRepository.save(event)).thenReturn(event)
        val expected: Event? = eventService.saveEvent(event).toFuture().get()

        Assert.assertEquals(event.id, expected?.id)
        Assert.assertEquals(event.userId, expected?.userId)
        Assert.assertEquals(event.name, expected?.name)
        Assert.assertEquals(event.description, expected?.description)
        Assert.assertEquals(event.email, expected?.email)
        Assert.assertEquals(event.place.address, expected?.place?.address)
        Assert.assertEquals(event.place.neighborhood, expected?.place?.neighborhood)
        Assert.assertEquals(event.place.city, expected?.place?.city)
        Assert.assertEquals(event.place.state, expected?.place?.state)
        Assert.assertEquals(event.place.location.lat, expected?.place?.location?.lat)
        Assert.assertEquals(event.place.location.lng, expected?.place?.location?.lng)
    }

    @Test
    fun test_remove_event_ok() {
        val event = getEvent("Sertanejão", "festa do peao")
        val responseFindById: Optional<Event> = Optional.ofNullable(event)
        `when`(eventRepository.findById(event.id.toString())).thenReturn(responseFindById)

        val mock: EventRepository = mock(EventRepository::class.java)
        doThrow(Exception::class.java).`when`(mock).delete(event)

        val expected = eventService.removeEvent(event, 123L).toFuture().get()
        Assert.assertEquals(true, expected?.message == "Evento removido com sucesso!")
    }

    @Test
    fun test_check_update_event() {
        val updateEventRequest = UpdateEventRequest(id = "qwe", name = "Amne")
        val userId = 123L
        val eventNew = getEvent("Amne", "festa do Eletron")
        val eventOld = getEvent("Sertanejão", "festa do peao")

        val responseFindById: Optional<Event> = Optional.ofNullable(eventOld)
        `when`(eventRepository.findById(updateEventRequest.id)).thenReturn(responseFindById)

        val data = Event().mergeDataCompany(updateEventRequest, eventOld)

        `when`(eventRepository.save(data)).thenReturn(eventNew)
        val expected = eventService.checkUpdateEvent(updateEventRequest, userId).toFuture().get()

        Assert.assertEquals(true, expected.name == updateEventRequest.name)
    }

    @Test
    fun test_createUserEvent() {
        val event = getEvent("Amne", "festa do Eletro")
        val responseFindById: Optional<Event> = Optional.ofNullable(event)
        val createUserEvent = CreateUserEventRequest("AA!!", "mercadopago", event.id!!)
        val createUserEventResponse = CreateUserEventResponse(event.id!!, event.userId)

        `when`(eventRepository.findById(event.id!!)).thenReturn(responseFindById)
        `when`(userEventService.createUserEvent(event.userId, createUserEvent, apikey)).thenReturn(just(createUserEventResponse))

        val expected = eventService.createUserEvent(event.userId, createUserEvent, apikey).toFuture().get()
        Assert.assertEquals(true, expected.eventId == event.id)
    }
}
