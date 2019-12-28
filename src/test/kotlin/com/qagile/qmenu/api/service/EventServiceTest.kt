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

    fun getCompany(name: String, description: String): Event {
        val companyLocation = EventLocation(lat = -23.4954556, lng = -46.6406668)
        val companyAddress = EventPlace(address = "Rua Copacabana 160, Casa 08", neighborhood = "Santana",
            city = "São Paulo", state = "SP", location = companyLocation)

        return Event(applicationUserId = 1, name = name, description = description,
            email = "eletrosho@eletrosho.com.br", place = companyAddress)
    }

    @Test
    fun test_update_company_ok() {
        val oldCompany = eventService.saveCompany(getCompany("Rock", "Festival do Rock")).toFuture().get()
        val newCompany = Event(id = oldCompany.id, name = "Rock in Rio Brasil", imageUrl = "http://qagile.com.br/eletroshop.png")
        var expected: Event? = eventService.updateCompany(newCompany).toFuture().get()

        Assert.assertEquals(true, expected?.name == newCompany.name)
        Assert.assertEquals(true, expected?.imageUrl == newCompany.imageUrl)
    }

    @Test
    fun test_update_company_error() {
        val newCompany = Event(id = "5e0641f3c8f94e5ddad6591f", name = "Rock in Rio Brasil", imageUrl = "http://qagile.com.br/eletroshop.png")
        try {
            eventService.updateCompany(newCompany).toFuture().get()
        } catch (ex: Exception) {
            Assert.assertEquals("com.qagile.qmenu.api.entities.CompanyException: O evento não está cadastrado!", ex.message)
        }
    }

    @Test
    fun test_save_company_ok() {
        val company = getCompany("ElectroShop", "festa do cerveja")
        val expected: Event? = eventService.saveCompany(company).toFuture().get()

        Assert.assertEquals(company.id, expected?.id)
        Assert.assertEquals(company.applicationUserId, expected?.applicationUserId)
        Assert.assertEquals(company.name, expected?.name)
        Assert.assertEquals(company.description, expected?.description)
        Assert.assertEquals(company.email, expected?.email)
        Assert.assertEquals(company.place.address, expected?.place?.address)
        Assert.assertEquals(company.place.neighborhood, expected?.place?.neighborhood)
        Assert.assertEquals(company.place.city, expected?.place?.city)
        Assert.assertEquals(company.place.state, expected?.place?.state)
        Assert.assertEquals(company.place.location.lat, expected?.place?.location?.lat)
        Assert.assertEquals(company.place.location.lng, expected?.place?.location?.lng)
    }

    @Test
    fun test_delete_company_ok() {
        val company = getCompany("Sertanejão", "festa do peao")
        eventService.saveCompany(company).toFuture().get()
        eventService.deleteCompany(company).toFuture().get()

        val expected: Optional<Event>? = eventService.findById(company).toFuture().get()

        Assert.assertEquals(false, expected?.isPresent)
    }
}
