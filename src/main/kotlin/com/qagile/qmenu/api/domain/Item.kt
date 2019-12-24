package com.qagile.qmenu.api.domain

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Item (
    @Id
    val id: String? = ObjectId().toHexString(),

    val name: String = "",

    val email: String = ""
)
