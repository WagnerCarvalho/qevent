package com.qagile.qmenu.api.repository

import com.qagile.qmenu.api.domain.Company
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CompanyRepository : MongoRepository <Company, String> {

    fun findByEmail(email: String): MutableList<Company>
}
