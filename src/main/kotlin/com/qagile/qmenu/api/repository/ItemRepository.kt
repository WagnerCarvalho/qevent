package com.qagile.qmenu.api.repository

import com.qagile.qmenu.api.domain.Item
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository: MongoRepository<Item, String> {

    fun findByEmail(email: String): MutableList<Item>
}