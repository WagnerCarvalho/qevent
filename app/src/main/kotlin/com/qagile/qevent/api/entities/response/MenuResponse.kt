package com.qagile.qevent.api.entities.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.qagile.qevent.api.domain.Menu
import com.qagile.qevent.api.entities.CategoryStatus

data class MenuResponse(

    @field:JsonProperty("food")
    var food: MutableList<Menu> = mutableListOf(),

    @field:JsonProperty("drinks")
    var drinks: MutableList<Menu> = mutableListOf(),

    @field:JsonProperty("others")
    var others: MutableList<Menu> = mutableListOf()

) {
    fun get(menuItems: MutableList<Menu>): MenuResponse {

        val menu = MenuResponse()
        menuItems.map {
            when (it.category) {
                CategoryStatus.FOOD.name.toLowerCase() -> menu.food.add(it)
                CategoryStatus.DRINK.name.toLowerCase() -> menu.drinks.add(it)
                else -> menu.others.add(it)
            }
        }
        return menu
    }
}
