package com.qagile.qevent.api.modules.acquirer.service

import com.qagile.qevent.api.modules.qacquirer.entities.request.CreateUserEventRequest
import com.qagile.qevent.api.modules.qacquirer.entities.response.CreateUserEventResponse
import com.qagile.qevent.api.modules.qacquirer.gateway.Quser
import com.qagile.qevent.api.modules.qacquirer.service.UserEventService
import io.reactivex.Single.just
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
class UserEventServiceTest {

    @Autowired
    private lateinit var userEventService: UserEventService

    @Autowired
    @MockBean
    private lateinit var quser: Quser

    val userId = 123L
    val authorizationId = "AVBBB"
    val acquirer = "mercadopago"
    val eventId = "1234567"
    val createUserEventRequest = CreateUserEventRequest(authorizationId, acquirer, eventId)
    val createUserEventResponse = CreateUserEventResponse(eventId, userId)
    val apikey = "api-gateway"

    private fun getHeader(userId: Long, apikey: String): Map<String, String> {

        return mapOf(
            "user_id" to userId.toString(),
            "apikey" to apikey
        )
    }

    @Test
    fun test_createUserEvent() {
        Mockito.`when`(quser.createUserEvent(getHeader(userId, apikey), createUserEventRequest)).thenReturn(just((createUserEventResponse)))

        val expected = userEventService.createUserEvent(userId, createUserEventRequest, apikey).toFuture().get()
        Assert.assertEquals(true, expected.eventId == eventId)
    }
}
