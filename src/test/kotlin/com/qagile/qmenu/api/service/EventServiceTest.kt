package com.qagile.qmenu.api.service

import com.qagile.qmenu.api.domain.Event
import com.qagile.qmenu.api.domain.EventLocation
import com.qagile.qmenu.api.domain.EventPlace
import com.qagile.qmenu.api.entities.request.UpdateEventRequest
import com.qagile.qmenu.api.repository.EventRepository
import java.util.Optional
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`doThrow`
import org.mockito.Mockito.`mock`
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
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
    @MockBean
    private lateinit var eventRepository: EventRepository

    private fun getEvent(name: String, description: String): Event {
        val eventLocation = EventLocation(lat = -23.4954556, lng = -46.6406668)
        val eventAddress = EventPlace(address = "Rua Copacabana 160, Casa 08", neighborhood = "Santana",
            city = "São Paulo", state = "SP", location = eventLocation)

        return Event(applicationUserId = 1, name = name, description = description,
            email = "eletrosho@eletrosho.com.br", place = eventAddress, imageUrl = "test.com.br")
    }

    @Test
    fun test_update_event_ok() {
        val oldEvent = getEvent("coca-cola", "competicao para beber")
        val newEvent = UpdateEventRequest(id = oldEvent.id, imageUrl = "http://qagile.com.br/eletroshop.png")

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
            Assert.assertEquals("com.qagile.qmenu.api.entities.EventException: O evento não está cadastrado!", ex.message)
        }
    }

    @Test
    fun test_save_event_ok() {
        val event = getEvent("ElectroShop", "festa do cerveja")

        `when`(eventRepository.save(event)).thenReturn(event)
        val expected: Event? = eventService.saveEvent(event).toFuture().get()

        Assert.assertEquals(event.id, expected?.id)
        Assert.assertEquals(event.applicationUserId, expected?.applicationUserId)
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
    fun test_delete_event_ok() {
        val event = getEvent("Sertanejão", "festa do peao")
        val responseFindById: Optional<Event> = Optional.ofNullable(event)
        `when`(eventRepository.findById(event.id.toString())).thenReturn(responseFindById)

        val mock: EventRepository = mock(EventRepository::class.java)
        doThrow(Exception::class.java).`when`(mock).delete(event)

        val expected = eventService.removeEvent(event, 123L).toFuture().get()
        Assert.assertEquals(true, expected?.message == "Event Successfully Removed")
    }
}
