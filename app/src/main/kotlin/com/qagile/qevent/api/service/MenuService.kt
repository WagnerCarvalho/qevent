package com.qagile.qevent.api.service

import com.qagile.qevent.api.domain.Menu
import com.qagile.qevent.api.entities.exception.MenuException
import com.qagile.qevent.api.entities.request.CreateMenuRequest
import com.qagile.qevent.api.entities.request.DeleteRequest
import com.qagile.qevent.api.entities.request.UpdateMenuRequest
import com.qagile.qevent.api.entities.response.DeleteResponse
import com.qagile.qevent.api.repository.MenuRepository
import com.qagile.qevent.api.utils.ErrorCode
import com.qagile.qevent.api.utils.SuccessCode
import com.qagile.qevent.api.utils.Translator
import com.qagile.qevent.api.utils.getError
import io.reactivex.Single
import io.reactivex.Single.just
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MenuService {
    private val logger = LoggerFactory.getLogger(EventService::class.java)

    @Autowired
    private lateinit var menuRepository: MenuRepository

    @Autowired
    private lateinit var eventService: EventService

    fun checkCreateMenu(createMenuRequest: CreateMenuRequest, applicationUserId: Long): Single<Menu> {
        logger.info("Start checkCreateMenu by applicationUserId: $applicationUserId with request: $createMenuRequest")

        return saveMenu(Menu().convertToMenu(createMenuRequest), applicationUserId)
    }

    fun checkUpdateMenu(updateMenuRequest: UpdateMenuRequest, applicationUserId: Long): Single<Menu> {
        logger.info("Start checkUpdateMenu by applicationUserId: $applicationUserId with request: $updateMenuRequest")

        return findById(updateMenuRequest.id)
            .filter {
                it.isPresent
            }.flatMapSingle {
                updateMenu(updateMenuRequest, it.get(), applicationUserId)
            }.doOnSuccess {
                logger.info("End checkUpdateMenu by applicationUserId: $applicationUserId with response: $it")
            }.doOnError {
                logger.error("Error removeMenu by applicationUserId: $applicationUserId with error: ${it.getError()}")
            }.onErrorResumeNext {
                Single.error(MenuException("400", Translator.getMessage(ErrorCode.ERROR_TO_UPDATE_MENU)))
            }
    }

    fun updateMenu(updateMenuRequest: UpdateMenuRequest, menu: Menu, applicationUserId: Long): Single<Menu> {
        logger.info("Start updateMenu by applicationUserId: $applicationUserId with request: $updateMenuRequest")

        return saveMenu(Menu().mergeDataMenu(updateMenuRequest, menu), applicationUserId)
            .doOnSuccess {
                logger.info("End updateMenu by applicationUserId: $applicationUserId with response: $it")
            }.doOnError {
                logger.error("Error updateMenu by applicationUserId: $applicationUserId with error: $it")
            }
    }

    fun removeMenu(deleteMenuRequest: DeleteRequest, applicationUserId: Long): Single<DeleteResponse> {
        logger.info("Start removeMenu by applicationUserId: $applicationUserId with request: $deleteMenuRequest")

        return findById(deleteMenuRequest.id)
            .filter {
                it.isPresent
            }.flatMapSingle {
                remove(deleteMenuRequest.id).map {
                    DeleteResponse().getDeleteEventResponse(deleteMenuRequest.id, applicationUserId, Translator.getMessage(SuccessCode.MENU_REMOVE))
                }
            }.doOnSuccess {
                logger.info("End removeMenu by applicationUserId: $applicationUserId with response: $it")
                logger.info("End removeMenu by applicationUserId: $applicationUserId with request: $it to feed")
            }.doOnError {
                logger.error("Error removeMenu by applicationUserId: $applicationUserId with error: ${it.getError()}")
            }.onErrorResumeNext {
                Single.error(MenuException("400", Translator.getMessage(ErrorCode.MENU_DOES_NOT_EXIST)))
            }
    }

    fun saveMenu(menu: Menu, applicationUserId: Long): Single<Menu> {
        logger.info("Start saveMenu by applicationUserId: $applicationUserId with request: $menu")

        return eventService.findById(menu.eventId)
            .filter {
                it.isPresent
            }.flatMapSingle {
                save(menu)
            }.doOnSuccess {
                logger.info("End saveMenu by applicationUserId: $applicationUserId with response: $it")
                logger.info("End saveMenu by applicationUserId: $applicationUserId with request: $it to feed")
            }.doOnError {
                logger.error("Error saveMenu by applicationUserId: $applicationUserId with error: ${it.getError()}")
            }.onErrorResumeNext {
                Single.error(MenuException("400", Translator.getMessage(ErrorCode.EVENT_DOES_NOT_EXIST)))
            }
    }

    private fun remove(id: String) = just(menuRepository.deleteById(id))

    private fun findById(id: String) = just(menuRepository.findById(id))

    private fun save(menu: Menu) = just(menuRepository.save(menu))
}
