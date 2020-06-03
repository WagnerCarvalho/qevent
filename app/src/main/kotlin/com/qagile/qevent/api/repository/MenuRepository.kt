package com.qagile.qevent.api.repository

import com.qagile.qevent.api.domain.Menu
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MenuRepository : MongoRepository <Menu, String>
