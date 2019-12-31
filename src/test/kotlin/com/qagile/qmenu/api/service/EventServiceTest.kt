package com.qagile.qmenu.api.service

import com.qagile.qmenu.api.domain.Event
import com.qagile.qmenu.api.domain.EventLocation
import com.qagile.qmenu.api.domain.EventPlace
import java.lang.Exception
import java.util.Optional
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
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

    private fun getEvent(name: String, description: String): Event {
        val eventLocation = EventLocation(lat = -23.4954556, lng = -46.6406668)
        val eventAddress = EventPlace(address = "Rua Copacabana 160, Casa 08", neighborhood = "Santana",
            city = "São Paulo", state = "SP", location = eventLocation)

        return Event(applicationUserId = 1, name = name, description = description,
            email = "eletrosho@eletrosho.com.br", place = eventAddress)
    }

    @Test
    fun test_update_event_ok() {
        val oldEvent = eventService.saveEvent(getEvent("Rock", "Festival do Rock")).toFuture().get()
        val newEvent = Event(id = oldEvent.id, name = "Rock in Rio Brasil", imageUrl = "http://qagile.com.br/eletroshop.png")
        var expected: Event? = eventService.updateEvent(newEvent).toFuture().get()

        Assert.assertEquals(true, expected?.name == newEvent.name)
        Assert.assertEquals(true, expected?.imageUrl == newEvent.imageUrl)
    }

    @Test
    fun test_update_event_error() {
        val newEvent = Event(id = "5e0641f3c8f94e5ddad6591f", name = "Rock in Rio Brasil", imageUrl = "http://qagile.com.br/eletroshop.png")
        try {
            eventService.updateEvent(newEvent).toFuture().get()
        } catch (ex: Exception) {
            Assert.assertEquals("com.qagile.qmenu.api.entities.EventException: O evento não está cadastrado!", ex.message)
        }
    }

    @Test
    fun test_save_event_ok() {
        val event = getEvent("ElectroShop", "festa do cerveja")
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
        eventService.saveEvent(event).toFuture().get()
        eventService.removeEvent(event, 123).toFuture().get()

        val expected: Optional<Event>? = eventService.findById(event.id!!).toFuture().get()

        Assert.assertEquals(false, expected?.isPresent)
    }
}
