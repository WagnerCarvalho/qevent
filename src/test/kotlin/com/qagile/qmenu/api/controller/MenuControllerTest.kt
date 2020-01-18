package com.qagile.qmenu.api.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.qagile.qmenu.api.domain.Menu
import com.qagile.qmenu.api.entities.request.CreateMenuRequest
import com.qagile.qmenu.api.entities.request.DeleteRequest
import com.qagile.qmenu.api.entities.response.DeleteResponse
import com.qagile.qmenu.api.routers.MenuRouter
import com.qagile.qmenu.api.service.MenuService
import io.reactivex.Single.just
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
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
        val applicationUserId = 123L

        Mockito.`when`(menuService.checkCreateMenu(request, applicationUserId!!)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.post(MenuRouter.CREATE_MENU_V1)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("user_id", applicationUserId)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Throws(Exception::class)
    fun test_create_menu_bad_request_header() {
        val request = getMenuRequest("5e124d06295a410254dd9cf9", "Cerveja", "Puro Malte", "10".toDouble())
        val response = getMenu("5e124d06295a410254dd9cf9", "Cerveja", "Puro Malte", "10.00".toDouble())
        val applicationUserId = 123L

        Mockito.`when`(menuService.checkCreateMenu(request, applicationUserId!!)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.post(MenuRouter.CREATE_MENU_V1)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @Throws(Exception::class)
    fun test_delete_menu_ok() {
        val applicationUserId = 123L
        val request = DeleteRequest(id = "123")
        val response = DeleteResponse().getDeleteEventResponse(request.id, applicationUserId, "Item removido do Menu com sucesso!")

        Mockito.`when`(menuService.removeMenu(request, applicationUserId)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.delete(MenuRouter.DELETE_MENU_V1)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("user_id", applicationUserId)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Throws(Exception::class)
    fun test_delete_menu_bad_request_header() {
        val applicationUserId = 123L
        val request = DeleteRequest(id = "123")
        val response = DeleteResponse().getDeleteEventResponse(request.id, applicationUserId, "Item removido do Menu com sucesso!")

        Mockito.`when`(menuService.removeMenu(request, applicationUserId)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.delete(MenuRouter.DELETE_MENU_V1)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }
}
