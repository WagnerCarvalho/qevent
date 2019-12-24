package com.qagile.qmenu.api.service

import com.qagile.qmenu.api.domain.Item
import com.qagile.qmenu.api.repository.ItemRepository
import io.reactivex.Single
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ItemService {
    private val logger = LoggerFactory.getLogger(ItemService::class.java)

    @Autowired
    private lateinit var itemRepository: ItemRepository

    fun findByEmail(item: Item) = Single.just(itemRepository.findByEmail(item.email))

    fun save(item: Item) = Single.just(itemRepository.save(item))
}

