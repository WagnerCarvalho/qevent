package com.qagile.qmenu.core.health

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@RunWith(SpringRunner::class)
@WebMvcTest(PingHandlerController::class)
class PingHandlerControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var pingHandlerController: PingHandlerController

    @Test
    @Throws(Exception::class)
    fun test_ping_success() {

        this.mvc.perform(MockMvcRequestBuilders.get("/ping"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Throws(Exception::class)
    fun test_ping_not_found() {

        this.mvc.perform(MockMvcRequestBuilders.get("/ping1"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    @Throws(Exception::class)
    fun test_ping_checj_response() {
        Assert.assertEquals("pong", pingHandlerController.health())
    }
}