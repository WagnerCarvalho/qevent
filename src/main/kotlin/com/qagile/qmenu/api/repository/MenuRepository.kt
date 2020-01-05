package com.qagile.qmenu.api.repository

import com.qagile.qmenu.api.domain.Menu
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MenuRepository : MongoRepository <Menu, String>
