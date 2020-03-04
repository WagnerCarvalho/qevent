package com.qagile.qevent.api.service

import com.qagile.qevent.api.domain.Event
import com.qagile.qevent.api.domain.Menu
import com.qagile.qevent.api.entities.EventLocation
import com.qagile.qevent.api.entities.EventPlace
import com.qagile.qevent.api.entities.ProductStatus
import com.qagile.qevent.api.entities.request.DeleteRequest
import com.qagile.qevent.api.entities.request.UpdateMenuRequest
import com.qagile.qevent.api.repository.MenuRepository
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

    val applicationUserId = 123L

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

        val responseFindById: Optional<Event> = Optional.empty()
        Mockito.`when`(eventService.findById(request.eventId)).thenReturn(just(responseFindById))
        Mockito.`when`(menuRepository.save(request)).thenReturn(request)

        try {
            menuService.saveMenu(request, applicationUserId).toFuture().get()
        } catch (ex: Exception) {
            Assert.assertEquals("com.qagile.qevent.api.entities.exception.MenuException: O evento não está cadastrado!", ex.message)
        }
    }

    @Test
    fun test_remove_menu_success() {
        val request = DeleteRequest(id = "qwe")
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
        val responseFindById: Optional<Menu> = Optional.empty()

        val mock: MenuRepository = Mockito.mock(MenuRepository::class.java)
        Mockito.doThrow(Exception::class.java).`when`(mock).deleteById(request.id)
        Mockito.`when`(menuRepository.findById(request.id)).thenReturn(responseFindById)

        try {
            menuService.removeMenu(request, applicationUserId).toFuture().get()
        } catch (ex: Exception) {
            Assert.assertEquals("com.qagile.qevent.api.entities.exception.MenuException: Esse item não existe no Menu!", ex.message)
        }
    }

    @Test
    fun test_updateMenu() {
        val updateMenuRequest = UpdateMenuRequest(id = "123", product = "refrigerante")
        val menu = getMenu(eventId = "12345", product = "coca-cola", description = "bebida não alcoólica", price = 10.0)
        val event = getEvent("teste", "festa do teste")
        val responseFindById: Optional<Event> = Optional.ofNullable(event)

        Mockito.`when`(eventService.findById(menu.eventId)).thenReturn(just(responseFindById))
        Mockito.`when`(menuRepository.save(Menu().mergeDataMenu(updateMenuRequest, menu))).thenReturn(Menu().mergeDataMenu(updateMenuRequest, menu))

        val expected = menuService.updateMenu(updateMenuRequest, menu, applicationUserId).toFuture().get()
        Assert.assertEquals(true, expected.product == "refrigerante")
    }

    @Test
    fun test_checkUpdateMenu() {
        val updateMenuRequest = UpdateMenuRequest(id = "123", product = "refrigerante")
        val menu = getMenu(eventId = "12345", product = "coca-cola", description = "bebida não alcoólica", price = 10.0)
        val event = getEvent("teste", "festa do teste")

        val responseFindByIdMenu: Optional<Menu> = Optional.ofNullable(menu)
        val responseFindByIdEvent: Optional<Event> = Optional.ofNullable(event)
        Mockito.`when`(menuRepository.findById(updateMenuRequest.id)).thenReturn(responseFindByIdMenu)
        Mockito.`when`(eventService.findById(menu.eventId)).thenReturn(just(responseFindByIdEvent))
        Mockito.`when`(menuRepository.save(Menu().mergeDataMenu(updateMenuRequest, menu))).thenReturn(Menu().mergeDataMenu(updateMenuRequest, menu))

        val expected = menuService.checkUpdateMenu(updateMenuRequest, applicationUserId).toFuture().get()
        Assert.assertEquals(true, expected.product == "refrigerante")
    }
}
