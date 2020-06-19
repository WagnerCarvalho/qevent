package com.qagile.qevent.api.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.qagile.qevent.api.entities.ProductStatus
import com.qagile.qevent.api.entities.request.CreateMenuRequest
import com.qagile.qevent.api.entities.request.UpdateMenuRequest
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
    val price: Double = "0.0".toDouble(),

    @JsonProperty("url")
    val url: String = "",

    @JsonProperty("quantity")
    val quantity: Long = 0

) {
    fun convertToMenu(createMenuRequest: CreateMenuRequest): Menu {

        return Menu(
            eventId = createMenuRequest.eventId,
            product = createMenuRequest.product,
            description = createMenuRequest.description,
            price = createMenuRequest.price,
            url = createMenuRequest.url,
            quantity = createMenuRequest.quantity
        )
    }

    fun mergeDataMenu(newMenu: UpdateMenuRequest, oldMenu: Menu): Menu {

        return Menu(
            id = oldMenu.id,
            eventId = oldMenu.eventId,
            product = if (newMenu.product == "") oldMenu.product else newMenu.product,
            description = if (newMenu.description == "") oldMenu.description else newMenu.description,
            status = if (newMenu.status == "") getStatus(oldMenu.description) else getStatus(newMenu.description),
            price = if (newMenu.price == "0.0".toDouble()) oldMenu.price else newMenu.price,
            url = if (newMenu.url == "") oldMenu.url else newMenu.url,
            quantity = if (newMenu.quantity == 0.toLong()) oldMenu.quantity else newMenu.quantity
        )
    }

    fun getStatus(status: String): ProductStatus {

        return when (status) {
            ProductStatus.ACTIVE.name -> ProductStatus.ACTIVE
            ProductStatus.PENDING.name -> ProductStatus.PENDING
            ProductStatus.CANCELED.name -> ProductStatus.CANCELED
            else -> ProductStatus.PENDING
        }
    }
}
