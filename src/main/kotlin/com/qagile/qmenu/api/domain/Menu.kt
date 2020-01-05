package com.qagile.qmenu.api.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.qagile.qmenu.api.entities.ProductStatus
import com.qagile.qmenu.api.entities.request.CreateMenuRequest
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "menu")
data class Menu(

    @Id
    @JsonProperty("id")
    val id: String? = ObjectId().toHexString(),

    @JsonProperty("event_id")
    val eventId: String = "",

    @JsonProperty("product")
    val product: String = "",

    @JsonProperty("description")
    val description: String = "",

    @JsonProperty("status")
    val status: ProductStatus = ProductStatus.ACTIVE,

    @JsonProperty("price")
    val price: Double = "0.0".toDouble()

) {
    fun convertToMenu(createMenuRequest: CreateMenuRequest): Menu {

        return Menu(
            eventId = createMenuRequest.eventId,
            product = createMenuRequest.product,
            description = createMenuRequest.description,
            price = createMenuRequest.price
        )
    }
}
