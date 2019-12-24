package com.qagile.qmenu.api.service

import com.qagile.qmenu.api.domain.Item
import io.reactivex.Single
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
class ItemServiceTest {

    @Autowired
    private lateinit var itemService: ItemService

    @Test
    fun test_findByEmail_not_found() {
        val item = Item(name = "ElectroShop", email = "eletr@eletrosho.com.br")
        val expected: MutableList<Item>? = itemService.findByEmail(item).toFuture().get()

        Assert.assertEquals(true, expected?.size == 0)
    }

    @Test
    fun test_findByEmail_success() {
        val item = Item(name = "ElectroShop", email = "eletrosho@eletrosho.com.br")
        val expected: MutableList<Item>? = itemService.findByEmail(item).toFuture().get()

        Assert.assertEquals(true, expected?.size!! > 1)
    }

    @Test
    fun test_save_success(){
        val item = Item(name = "ElectroShop", email = "eletrosho@eletrosho.com.br")
        val expected: Item? = itemService.save(item).toFuture().get()

        Assert.assertEquals(item.email, expected?.email)
        Assert.assertEquals(item.name, expected?.name)
        Assert.assertEquals(item.id, expected?.id)
    }
}