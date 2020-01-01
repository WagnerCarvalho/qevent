package com.qagile.qmenu.api.repository

import com.qagile.qmenu.api.domain.Event
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : MongoRepository <Event, String> {

    fun findByEmail(email: String): MutableList<Event>
    // fun removeById(id: String): Boolean
}
