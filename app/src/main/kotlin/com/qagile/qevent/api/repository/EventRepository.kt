package com.qagile.qevent.api.repository

import com.qagile.qevent.api.domain.Event
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : MongoRepository <Event, String> {

    fun findByEmail(email: String): MutableList<Event>
    fun findByUserId(UserId: Long): MutableList<Event>
}
