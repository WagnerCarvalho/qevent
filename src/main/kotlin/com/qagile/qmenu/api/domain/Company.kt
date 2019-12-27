package com.qagile.qmenu.api.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="company")
data class Company (
    @Id
    @JsonProperty("id")
    val id: String? = ObjectId().toHexString(),

    @JsonProperty("application_user_id")
    val applicationUserId: Long = 0,

    @JsonProperty("name")
    val name: String = "",

    @JsonProperty("description")
    val description: String = "",

    @JsonProperty("email")
    val email: String = "",

    @JsonProperty("image_url")
    val imageUrl: String = "",

    @JsonProperty("version")
    val version: Long = 1,

    @JsonProperty("place")
    val place: CompanyPlace = CompanyPlace()
) {
    fun mergeDataCompany(newCompany: Company, oldCompany: Company): Company {

        return Company(
            id = oldCompany.id,
            applicationUserId = oldCompany.applicationUserId,
            name = if (newCompany.name == "") oldCompany.name else newCompany.name,
            description = if (newCompany.description == "") oldCompany.description else newCompany.description,
            email = if (newCompany.email == "") oldCompany.email else newCompany.email,
            imageUrl = if (newCompany.imageUrl == "") oldCompany.imageUrl else newCompany.imageUrl,
            version = oldCompany.version + 1,
            place = CompanyPlace(
                address = if (newCompany.place.address == "") oldCompany.place.address else newCompany.place.address,
                neighborhood = if (newCompany.place.neighborhood == "") oldCompany.place.neighborhood else newCompany.place.neighborhood,
                city = if (newCompany.place.city == "") oldCompany.place.city else newCompany.place.city,
                state = if (newCompany.place.state == "") oldCompany.place.state else newCompany.place.state,
                location = CompanyLocation(
                    lat = if (newCompany.place.location.lat == 0.0) oldCompany.place.location.lat else newCompany.place.location.lat,
                    lng = if (newCompany.place.location.lng == 0.0) oldCompany.place.location.lng else newCompany.place.location.lng
                )
            )
        )
    }
}
