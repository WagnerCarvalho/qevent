package com.qagile.qevent.api.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.qagile.qevent.api.domain.Menu
import com.qagile.qevent.api.entities.request.CreateMenuRequest
import com.qagile.qevent.api.entities.request.DeleteRequest
import com.qagile.qevent.api.entities.request.UpdateMenuRequest
import com.qagile.qevent.api.entities.response.DeleteResponse
import com.qagile.qevent.api.routers.MenuRouter
import com.qagile.qevent.api.service.MenuService
import io.reactivex.Single.just
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@RunWith(SpringRunner::class)
@WebMvcTest(MenuController::class)
class MenuControllerTest {

    @Autowired
    @MockBean
    private lateinit var menuService: MenuService

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Before
    fun initMocks() {
        MockitoAnnotations.initMocks(this)
    }

    private fun getMenu(eventId: String, product: String, description: String, price: Double): Menu {

        return Menu(
            eventId = eventId,
            product = product,
            description = description,
            price = price
        )
    }

    private fun getMenuRequest(eventId: String, product: String, description: String, price: Double): CreateMenuRequest {

        return CreateMenuRequest(
            eventId = eventId,
            product = product,
            description = description,
            price = price
        )
    }

    @Test
    @Throws(Exception::class)
    fun test_create_menu_ok() {
        val request = getMenuRequest("5e124d06295a410254dd9cf9", "Cerveja", "Puro Malte", "10".toDouble())
        val response = getMenu("5e124d06295a410254dd9cf9", "Cerveja", "Puro Malte", "10.00".toDouble())
        val userId = 123L

        Mockito.`when`(menuService.checkCreateMenu(request, userId!!)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.post(MenuRouter.CREATE_MENU_V1)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("user_id", userId)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Throws(Exception::class)
    fun test_create_menu_bad_request_header() {
        val request = getMenuRequest("5e124d06295a410254dd9cf9", "Cerveja", "Puro Malte", "10".toDouble())
        val response = getMenu("5e124d06295a410254dd9cf9", "Cerveja", "Puro Malte", "10.00".toDouble())
        val userId = 123L

        Mockito.`when`(menuService.checkCreateMenu(request, userId!!)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.post(MenuRouter.CREATE_MENU_V1)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @Throws(Exception::class)
    fun test_delete_menu_ok() {
        val userId = 123L
        val request = DeleteRequest(id = "123")
        val response = DeleteResponse().getDeleteEventResponse(request.id, userId, "Item removido do Menu com sucesso!")

        Mockito.`when`(menuService.removeMenu(request, userId)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.delete(MenuRouter.DELETE_MENU_V1, "123")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("user_id", userId)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Throws(Exception::class)
    fun test_delete_menu_bad_request_header() {
        val userId = 123L
        val request = DeleteRequest(id = "123")
        val response = DeleteResponse().getDeleteEventResponse(request.id, userId, "Item removido do Menu com sucesso!")

        Mockito.`when`(menuService.removeMenu(request, userId)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.delete(MenuRouter.DELETE_MENU_V1, "123")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @Throws(Exception::class)
    fun test_updateMenu_ok() {
        val userId = 123L
        val request = UpdateMenuRequest(id = "123", product = "refrigerante")
        val response = getMenu("5e124d06295a410254dd9cf9", "Cerveja", "Puro Malte", "10.00".toDouble())

        Mockito.`when`(menuService.checkUpdateMenu(request, userId)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.put(MenuRouter.UPDATE_MENU_V1, "123")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("user_id", userId)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Throws(Exception::class)
    fun test_updateMenu_bad_request_header() {
        val userId = 123L
        val request = UpdateMenuRequest(id = "123", product = "refrigerante")
        val response = getMenu("5e124d06295a410254dd9cf9", "Cerveja", "Puro Malte", "10.00".toDouble())

        Mockito.`when`(menuService.checkUpdateMenu(request, userId)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.put(MenuRouter.UPDATE_MENU_V1, "123")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @Throws(Exception::class)
    fun test_updateMenu_bad_request_body() {
        val userId = 123L
        val request = UpdateMenuRequest(id = "123", product = "refrigerante")
        val response = getMenu("5e124d06295a410254dd9cf9", "Cerveja", "Puro Malte", "10.00".toDouble())

        Mockito.`when`(menuService.checkUpdateMenu(request, userId)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.put(MenuRouter.UPDATE_MENU_V1, "123")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("user_id", userId))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }
}
