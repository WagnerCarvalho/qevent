package com.qagile.qmenu.api.service

import com.qagile.qmenu.api.domain.Company
import com.qagile.qmenu.api.entities.CompanyException
import com.qagile.qmenu.api.repository.CompanyRepository
import com.qagile.qmenu.api.utils.ErrorCode
import com.qagile.qmenu.api.utils.Translator
import io.reactivex.Single
import io.reactivex.Single.just
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class CompanyService {
    private val logger = LoggerFactory.getLogger(CompanyService::class.java)

    @Autowired
    private lateinit var companyRepository: CompanyRepository

    fun updateCompany(newCompany: Company): Single<Company> {
        logger.info("Start updateCompany, newCompany: $newCompany")

        return findById(newCompany)
            .filter {
                it.isPresent
            }.flatMapSingle {
                save(newCompany.mergeDataCompany(newCompany, it.get()))
            }
            .doOnSuccess {
                logger.info("End updateCompany, newCompany: $it")
            }.doOnError {
                logger.info("Error updateCompany, error: $it")
            }.onErrorResumeNext {
                Single.error(CompanyException("400", Translator.getMessage(ErrorCode.COMPANY_DOES_NOT_EXIST)))
            }
    }

    fun saveCompany(company: Company): Single<Company> {
        logger.info("Start saveCompany, company: $company")

        return save(company)
            .doOnSuccess {
                logger.info("End saveCompany, company: $it")
            }.doOnError {
                logger.info("Error saveCompany, error: $it")
            }.onErrorResumeNext {
                Single.error(CompanyException("400", Translator.getMessage(ErrorCode.COMPANY_TRY_AGAIN_LATER)))
            }
    }

    fun deleteCompany(company: Company): Single<Unit> {
        logger.info("Start deleteCompany, company: $company")

        return findById(company)
            .filter {
                it.isPresent
            }.flatMapSingle {
                delete(it.get())
            }.doOnSuccess {
                logger.info("End deleteCompany, company: $it")
            }.doOnError {
                logger.info("Error saveCompany, error: $it")
            }.onErrorResumeNext{
                Single.error(CompanyException("400", Translator.getMessage(ErrorCode.COMPANY_DOES_NOT_EXIST)))
            }
    }

    fun findById(company: Company) = just(companyRepository.findById(company.id!!))

    private fun save(company: Company) = just(companyRepository.save(company))

    private fun delete(company: Company) = just(companyRepository.delete(company))
}









