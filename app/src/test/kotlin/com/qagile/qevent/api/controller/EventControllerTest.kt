package com.qagile.qevent.api.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.qagile.qevent.api.domain.Event
import com.qagile.qevent.api.entities.EventLocation
import com.qagile.qevent.api.entities.EventPlace
import com.qagile.qevent.api.entities.request.CreateEventRequest
import com.qagile.qevent.api.entities.request.DeleteRequest
import com.qagile.qevent.api.entities.request.UpdateEventRequest
import com.qagile.qevent.api.entities.response.DeleteResponse
import com.qagile.qevent.api.modules.qacquirer.entities.request.CreateUserEventRequest
import com.qagile.qevent.api.modules.qacquirer.entities.response.CreateUserEventResponse
import com.qagile.qevent.api.routers.EventRouter
import com.qagile.qevent.api.routers.UserEventRouter
import com.qagile.qevent.api.service.EventService
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
@WebMvcTest(EventController::class)
class EventControllerTest {

    @MockBean
    private lateinit var eventService: EventService

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Before
    fun initMocks() {
        MockitoAnnotations.initMocks(this)
    }

    val userId = 12L
    val eventId = "11"
    val authorization = "123"
    val acquirer = "mercadopago"
    val grantType = "authorization_code"
    private var eventLocation = EventLocation(lat = -23.4954556, lng = -46.6406668)
    private var createUserEventRequest = CreateUserEventRequest(authorization, acquirer, eventId, grantType)
    private var createUserEventResponse = CreateUserEventResponse(eventId, userId)
    private var eventAddress = EventPlace(address = "Rua Copacabana 160, Casa 08", neighborhood = "Santana",
        city = "São Paulo", state = "SP", location = eventLocation)

    private fun getEventRequest(name: String, description: String): CreateEventRequest {

        return CreateEventRequest(name = name, description = description,
            email = "eletrosho@eletrosho.com.br", place = eventAddress)
    }

    private fun getEvent(name: String, description: String): Event {

        return Event(userId = 1, name = name, description = description,
            email = "eletrosho@eletrosho.com.br", place = eventAddress)
    }

    @Test
    @Throws(Exception::class)
    fun test_create_event_ok() {
        val request = getEventRequest("ElectroShop", "festa do cerveja")
        val requestHeader: HashMap<String, String> = hashMapOf("Content-Type" to "application/json", "user_id" to "123")
        val userId = requestHeader.get("user_id")?.toLong()
        val response = getEvent("ElectroShop", "festa do cerveja")

        Mockito.`when`(eventService.checkCreateEvent(request, userId!!)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.post(EventRouter.CREATE_EVENT_V1)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("user_id", userId)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Throws(Exception::class)
    fun test_create_event_bad_request_header() {
        val request = getEventRequest("ElectroShop", "festa do cerveja")
        val requestHeader: HashMap<String, String> = hashMapOf("Content-Type" to "application/json", "user_id" to "123")
        val userId = requestHeader.get("user_id")?.toLong()
        val response = getEvent("ElectroShop", "festa do cerveja")

        Mockito.`when`(eventService.checkCreateEvent(request, userId!!)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.post(EventRouter.CREATE_EVENT_V1)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @Throws(Exception::class)
    fun test_create_event_body() {
        val request = getEventRequest("", "festa do cerveja")
        val requestHeader: HashMap<String, String> = hashMapOf("Content-Type" to "application/json", "user_id" to "123")
        val userId = requestHeader.get("user_id")?.toLong()
        val response = getEvent("ElectroShop", "festa do cerveja")

        Mockito.`when`(eventService.checkCreateEvent(request, userId!!)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.post(EventRouter.CREATE_EVENT_V1)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("user_id", userId)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @Throws(Exception::class)
    fun test_remove_event_ok() {
        val request = DeleteRequest("5e0b84ef2ea4095e7c19d782")
        val requestHeader: HashMap<String, String> = hashMapOf("Content-Type" to "application/json", "user_id" to "123")
        val userId = requestHeader.get("user_id")?.toLong()
        val response = DeleteResponse(id = "aaa", userId = userId!!, message = "remove_event")

        Mockito.`when`(eventService.checkRemoveEvent(request, userId!!)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.delete(EventRouter.DELETE_EVENT_V1, "5e0b84ef2ea4095e7c19d782")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("user_id", userId)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Throws(Exception::class)
    fun test_remove_event_request_header() {
        val request = DeleteRequest("5e0b84ef2ea4095e7c19d782")
        val requestHeader: HashMap<String, String> = hashMapOf("Content-Type" to "application/json", "user_id" to "123")
        val userId = requestHeader.get("user_id")?.toLong()
        val response = DeleteResponse(id = "aaa", userId = userId!!, message = "remove_event")

        Mockito.`when`(eventService.checkRemoveEvent(request, userId!!)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.delete(EventRouter.DELETE_EVENT_V1, "5e0b84ef2ea4095e7c19d782")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @Throws(Exception::class)
    fun test_remove_event_request_body() {
        val request = DeleteRequest()
        val requestHeader: HashMap<String, String> = hashMapOf("Content-Type" to "application/json", "user_id" to "123")
        val userId = requestHeader.get("user_id")?.toLong()
        val response = DeleteResponse(id = "aaa", userId = userId!!, message = "remove_event")

        Mockito.`when`(eventService.checkRemoveEvent(request, userId!!)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.delete(EventRouter.DELETE_EVENT_V1, "5e0b84ef2ea4095e7c19d782")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @Throws(Exception::class)
    fun test_update_event_ok() {
        val request = UpdateEventRequest(id = "5e0d19ec2e57b97f0adfa7b3", name = "Summer Festival")
        val requestHeader: HashMap<String, String> = hashMapOf("Content-Type" to "application/json", "user_id" to "123")
        val userId = requestHeader.get("user_id")?.toLong()
        val response = getEvent("Summer Festival", "festival da cerveja")

        Mockito.`when`(eventService.checkUpdateEvent(request, userId!!)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.put(EventRouter.UPDATE_EVENT_V1, "5e0d19ec2e57b97f0adfa7b3")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("user_id", userId)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Throws(Exception::class)
    fun test_update_event_ok_request_header() {
        val request = UpdateEventRequest(id = "5e0d19ec2e57b97f0adfa7b3", name = "Summer Festival")
        val requestHeader: HashMap<String, String> = hashMapOf("Content-Type" to "application/json", "user_id" to "123")
        val userId = requestHeader.get("user_id")?.toLong()
        val response = getEvent("Summer Festival", "festival da cerveja")

        Mockito.`when`(eventService.checkUpdateEvent(request, userId!!)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.put(EventRouter.UPDATE_EVENT_V1, "5e0d19ec2e57b97f0adfa7b3")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @Throws(Exception::class)
    fun test_update_event_ok_request_body() {
        val request = UpdateEventRequest(name = "Summer Festival")
        val requestHeader: HashMap<String, String> = hashMapOf("Content-Type" to "application/json", "user_id" to "123")
        val userId = requestHeader.get("user_id")?.toLong()
        val response = getEvent("Summer Festival", "festival da cerveja")

        Mockito.`when`(eventService.checkUpdateEvent(request, userId!!)).thenReturn(just(response))

        this.mvc.perform(MockMvcRequestBuilders.put(EventRouter.UPDATE_EVENT_V1, "")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("user_id", userId)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @Throws(Exception::class)
    fun test_createUserEvent_success() {
        Mockito.`when`(eventService.createUserEvent(userId, createUserEventRequest)).thenReturn(just(createUserEventResponse))

        this.mvc.perform(MockMvcRequestBuilders.post(UserEventRouter.CREATE_USER_EVENT_V1)
            .contentType(MediaType.APPLICATION_JSON)
            .header("user_id", userId)
            .content(objectMapper.writeValueAsString(createUserEventRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Throws(Exception::class)
    fun test_createUserEvent_body_error() {
        Mockito.`when`(eventService.createUserEvent(userId, createUserEventRequest)).thenReturn(just(createUserEventResponse))
        val createUserEventRequest = CreateUserEventRequest(acquirer = "aaa")

        this.mvc.perform(MockMvcRequestBuilders.post(UserEventRouter.CREATE_USER_EVENT_V1)
            .contentType(MediaType.APPLICATION_JSON)
            .header("user_id", userId)
            .content(objectMapper.writeValueAsString(createUserEventRequest)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @Throws(Exception::class)
    fun test_createUserEvent_header_error() {
        Mockito.`when`(eventService.createUserEvent(userId, createUserEventRequest)).thenReturn(just(createUserEventResponse))

        this.mvc.perform(MockMvcRequestBuilders.post(UserEventRouter.CREATE_USER_EVENT_V1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createUserEventRequest)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }
}
