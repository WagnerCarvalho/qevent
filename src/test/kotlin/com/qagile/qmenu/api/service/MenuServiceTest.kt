package com.qagile.qmenu.api.service

import com.qagile.qmenu.api.domain.Event
import com.qagile.qmenu.api.domain.Menu
import com.qagile.qmenu.api.entities.EventLocation
import com.qagile.qmenu.api.entities.EventPlace
import com.qagile.qmenu.api.entities.ProductStatus
import com.qagile.qmenu.api.entities.request.DeleteRequest
import com.qagile.qmenu.api.repository.MenuRepository
import io.reactivex.Single.just
import java.util.Optional
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener

@RunWith(SpringRunner::class)
@SpringBootTest
@TestExecutionListeners(DependencyInjectionTestExecutionListener::class)
class MenuServiceTest {

    @Autowired
    private lateinit var menuService: MenuService

    @Autowired
    @MockBean
    private lateinit var eventService: EventService

    @Autowired
    @MockBean
    private lateinit var menuRepository: MenuRepository

    private fun getEvent(name: String, description: String): Event {
        val eventLocation = EventLocation(lat = -23.4954556, lng = -46.6406668)
        val eventAddress = EventPlace(address = "Rua Copacabana 160, Casa 08", neighborhood = "Santana",
            city = "São Paulo", state = "SP", location = eventLocation)

        return Event(applicationUserId = 1, name = name, description = description,
            email = "eletrosho@eletrosho.com.br", place = eventAddress, imageUrl = "test.com.br")
    }

    private fun getMenu(eventId: String, product: String, description: String, price: Double): Menu {

        return Menu(
            eventId = eventId,
            product = product,
            description = description,
            price = price
        )
    }

    @Test
    fun test_create_menu_ok() {
        val event = getEvent("coca-cola", "competicao para beber")
        val request = getMenu(event.id.toString(), "Vodka Ciroc", "Vodka Importada", "50.00".toDouble())
        val applicationUserId = 123L

        val responseFindById: Optional<Event> = Optional.ofNullable(event)
        Mockito.`when`(eventService.findById(request.eventId)).thenReturn(just(responseFindById))
        Mockito.`when`(menuRepository.save(request)).thenReturn(request)

        val expected = menuService.saveMenu(request, applicationUserId).toFuture().get()
        Assert.assertEquals(true, expected.id == request.id)
        Assert.assertEquals(true, expected.eventId == request.eventId)
        Assert.assertEquals(true, expected.product == request.product)
        Assert.assertEquals(true, expected.description == request.description)
    }

    @Test
    fun test_create_menu_error() {
        val event = getEvent("coca-cola", "competicao para beber")
        val request = getMenu(event.id.toString(), "Vodka Ciroc", "Vodka Importada", "50.00".toDouble())
        val applicationUserId = 123L

        val responseFindById: Optional<Event> = Optional.empty()
        Mockito.`when`(eventService.findById(request.eventId)).thenReturn(just(responseFindById))
        Mockito.`when`(menuRepository.save(request)).thenReturn(request)

        try {
            menuService.saveMenu(request, applicationUserId).toFuture().get()
        } catch (ex: Exception) {
            Assert.assertEquals("com.qagile.qmenu.api.entities.exception.MenuException: O evento não está cadastrado!", ex.message)
        }
    }

    @Test
    fun test_remove_menu_success() {
        val request = DeleteRequest(id = "qwe")
        val applicationUserId = 123L
        val menu = Menu(id = "qwe", eventId = "123", product = "Coca", description = "Lata de Refrigerante", status = ProductStatus.ACTIVE, price = 5.00)
        val responseFindById: Optional<Menu> = Optional.ofNullable(menu)

        val mock: MenuRepository = Mockito.mock(MenuRepository::class.java)
        Mockito.doThrow(Exception::class.java).`when`(mock).deleteById(request.id)
        Mockito.`when`(menuRepository.findById(request.id)).thenReturn(responseFindById)

        val expected = menuService.removeMenu(request, applicationUserId).toFuture().get()

        Assert.assertEquals(true, expected.message == "Item removido do Menu com sucesso!")
    }

    @Test
    fun test_remove_menu_error() {
        val request = DeleteRequest(id = "qwe")
        val applicationUserId = 123L
        val responseFindById: Optional<Menu> = Optional.empty()

        val mock: MenuRepository = Mockito.mock(MenuRepository::class.java)
        Mockito.doThrow(Exception::class.java).`when`(mock).deleteById(request.id)
        Mockito.`when`(menuRepository.findById(request.id)).thenReturn(responseFindById)

        try {
            menuService.removeMenu(request, applicationUserId).toFuture().get()
        } catch (ex: Exception) {
            Assert.assertEquals("com.qagile.qmenu.api.entities.exception.MenuException: Esse item não existe no Menu!", ex.message)
        }
    }
}
