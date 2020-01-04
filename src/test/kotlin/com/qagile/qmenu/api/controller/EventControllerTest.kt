package com.qagile.qmenu.api.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.qagile.qmenu.api.domain.Event
import com.qagile.qmenu.api.domain.EventLocation
import com.qagile.qmenu.api.domain.EventPlace
import com.qagile.qmenu.api.entities.request.CreateEventRequest
import com.qagile.qmenu.api.entities.request.DeleteEventRequest
import com.qagile.qmenu.api.entities.request.UpdateEventRequest
import com.qagile.qmenu.api.entities.response.DeleteEventResponse
import com.qagile.qmenu.api.routers.EventRouter
import com.qagile.qmenu.api.service.EventService
import io.reactivex.Single
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

    private var eventLocation = EventLocation(lat = -23.4954556, lng = -46.6406668)
    private var eventAddress = EventPlace(address = "Rua Copacabana 160, Casa 08", neighborhood = "Santana",
        city = "São Paulo", state = "SP", location = eventLocation)

    private fun getEventRequest(name: String, description: String): CreateEventRequest {

        return CreateEventRequest(name = name, description = description,
            email = "eletrosho@eletrosho.com.br", place = eventAddress)
    }

    private fun getEvent(name: String, description: String): Event {

        return Event(applicationUserId = 1, name = name, description = description,
            email = "eletrosho@eletrosho.com.br", place = eventAddress)
    }

    @Test
    @Throws(Exception::class)
    fun test_create_event_v1_ok() {
        val request = getEventRequest("ElectroShop", "festa do cerveja")
        val requestHeader: HashMap<String, String> = hashMapOf("Content-Type" to "application/json", "user_id" to "123")
        val applicationUserId = requestHeader.get("user_id")?.toLong()
        val response = getEvent("ElectroShop", "festa do cerveja")

        Mockito.`when`(eventService.checkCreateEvent(request, applicationUserId!!)).thenReturn(Single.just(response))

        this.mvc.perform(MockMvcRequestBuilders.post(EventRouter.CREATE_EVENT_V1)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("user_id", applicationUserId)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Throws(Exception::class)
    fun test_create_event_v1_bad_request_header() {
        val request = getEventRequest("ElectroShop", "festa do cerveja")
        val requestHeader: HashMap<String, String> = hashMapOf("Content-Type" to "application/json", "user_id" to "123")
        val applicationUserId = requestHeader.get("user_id")?.toLong()
        val response = getEvent("ElectroShop", "festa do cerveja")

        Mockito.`when`(eventService.checkCreateEvent(request, applicationUserId!!)).thenReturn(Single.just(response))

        this.mvc.perform(MockMvcRequestBuilders.post(EventRouter.CREATE_EVENT_V1)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @Throws(Exception::class)
    fun test_create_event_v1_body() {
        val request = getEventRequest( "","festa do cerveja")
        val requestHeader: HashMap<String, String> = hashMapOf("Content-Type" to "application/json", "user_id" to "123")
        val applicationUserId = requestHeader.get("user_id")?.toLong()
        val response = getEvent("ElectroShop", "festa do cerveja")

        Mockito.`when`(eventService.checkCreateEvent(request, applicationUserId!!)).thenReturn(Single.just(response))

        this.mvc.perform(MockMvcRequestBuilders.post(EventRouter.CREATE_EVENT_V1)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("user_id", applicationUserId)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @Throws(Exception::class)
    fun test_remove_event_v1_ok() {
        val request = DeleteEventRequest("5e0b84ef2ea4095e7c19d782")
        val requestHeader: HashMap<String, String> = hashMapOf("Content-Type" to "application/json", "user_id" to "123")
        val applicationUserId = requestHeader.get("user_id")?.toLong()
        val response = DeleteEventResponse(id = "aaa", applicationUserId = applicationUserId.toString(), message = "remove_event")

        Mockito.`when`(eventService.checkRemoveEvent(request, applicationUserId!!)).thenReturn(Single.just(response))

        this.mvc.perform(MockMvcRequestBuilders.delete(EventRouter.DELETE_EVENT_V1)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("user_id", applicationUserId)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Throws(Exception::class)
    fun test_remove_event_v1_request_header() {
        val request = DeleteEventRequest("5e0b84ef2ea4095e7c19d782")
        val requestHeader: HashMap<String, String> = hashMapOf("Content-Type" to "application/json", "user_id" to "123")
        val applicationUserId = requestHeader.get("user_id")?.toLong()
        val response = DeleteEventResponse(id = "aaa", applicationUserId = applicationUserId.toString(), message = "remove_event")

        Mockito.`when`(eventService.checkRemoveEvent(request, applicationUserId!!)).thenReturn(Single.just(response))

        this.mvc.perform(MockMvcRequestBuilders.delete(EventRouter.DELETE_EVENT_V1)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @Throws(Exception::class)
    fun test_remove_event_v1_request_body() {
        val request = DeleteEventRequest()
        val requestHeader: HashMap<String, String> = hashMapOf("Content-Type" to "application/json", "user_id" to "123")
        val applicationUserId = requestHeader.get("user_id")?.toLong()
        val response = DeleteEventResponse(id = "aaa", applicationUserId = applicationUserId.toString(), message = "remove_event")

        Mockito.`when`(eventService.checkRemoveEvent(request, applicationUserId!!)).thenReturn(Single.just(response))

        this.mvc.perform(MockMvcRequestBuilders.delete(EventRouter.DELETE_EVENT_V1)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @Throws(Exception::class)
    fun test_update_event_v1_ok() {
        val request = UpdateEventRequest(id = "5e0d19ec2e57b97f0adfa7b3", name = "Summer Festival")
        val requestHeader: HashMap<String, String> = hashMapOf("Content-Type" to "application/json", "user_id" to "123")
        val applicationUserId = requestHeader.get("user_id")?.toLong()
        val response = getEvent("Summer Festival", "festival da cerveja")

        Mockito.`when`(eventService.checkUpdateEvent(request, applicationUserId!!)).thenReturn(Single.just(response))

        this.mvc.perform(MockMvcRequestBuilders.put(EventRouter.UPDATE_EVENT_V1)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("user_id", applicationUserId)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Throws(Exception::class)
    fun test_update_event_v1_ok_request_header() {
        val request = UpdateEventRequest(id = "5e0d19ec2e57b97f0adfa7b3", name = "Summer Festival")
        val requestHeader: HashMap<String, String> = hashMapOf("Content-Type" to "application/json", "user_id" to "123")
        val applicationUserId = requestHeader.get("user_id")?.toLong()
        val response = getEvent("Summer Festival", "festival da cerveja")

        Mockito.`when`(eventService.checkUpdateEvent(request, applicationUserId!!)).thenReturn(Single.just(response))

        this.mvc.perform(MockMvcRequestBuilders.put(EventRouter.UPDATE_EVENT_V1)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @Throws(Exception::class)
    fun test_update_event_v1_ok_request_body() {
        val request = UpdateEventRequest(name = "Summer Festival")
        val requestHeader: HashMap<String, String> = hashMapOf("Content-Type" to "application/json", "user_id" to "123")
        val applicationUserId = requestHeader.get("user_id")?.toLong()
        val response = getEvent("Summer Festival", "festival da cerveja")

        Mockito.`when`(eventService.checkUpdateEvent(request, applicationUserId!!)).thenReturn(Single.just(response))

        this.mvc.perform(MockMvcRequestBuilders.put(EventRouter.UPDATE_EVENT_V1)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("user_id", applicationUserId)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }
}
