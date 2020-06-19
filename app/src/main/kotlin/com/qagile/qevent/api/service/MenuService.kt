package com.qagile.qevent.api.service

import com.qagile.qevent.api.domain.Menu
import com.qagile.qevent.api.entities.exception.EventException
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

    fun checkCreateMenu(createMenuRequest: CreateMenuRequest, userId: Long): Single<Menu> {
        logger.info("Start checkCreateMenu by userId: $userId with request: $createMenuRequest")

        return saveMenu(Menu().convertToMenu(createMenuRequest), userId)
    }

    fun checkUpdateMenu(updateMenuRequest: UpdateMenuRequest, userId: Long): Single<Menu> {
        logger.info("Start checkUpdateMenu by userId: $userId with request: $updateMenuRequest")

        return findById(updateMenuRequest.id)
            .filter {
                it.isPresent
            }.flatMapSingle {
                updateMenu(updateMenuRequest, it.get(), userId)
            }.doOnSuccess {
                logger.info("End checkUpdateMenu by userId: $userId with response: $it")
            }.doOnError {
                logger.error("Error removeMenu by userId: $userId with error: ${it.getError()}")
            }.onErrorResumeNext {
                Single.error(MenuException("400", Translator.getMessage(ErrorCode.ERROR_TO_UPDATE_MENU)))
            }
    }

    fun updateMenu(updateMenuRequest: UpdateMenuRequest, menu: Menu, userId: Long): Single<Menu> {
        logger.info("Start updateMenu by userId: $userId with request: $updateMenuRequest")

        return saveMenu(Menu().mergeDataMenu(updateMenuRequest, menu), userId)
            .doOnSuccess {
                logger.info("End updateMenu by userId: $userId with response: $it")
            }.doOnError {
                logger.error("Error updateMenu by userId: $userId with error: $it")
            }
    }

    fun removeMenu(deleteMenuRequest: DeleteRequest, userId: Long): Single<DeleteResponse> {
        logger.info("Start removeMenu by userId: $userId with request: $deleteMenuRequest")

        return findById(deleteMenuRequest.id)
            .filter {
                it.isPresent
            }.flatMapSingle {
                remove(deleteMenuRequest.id).map {
                    DeleteResponse().getDeleteEventResponse(deleteMenuRequest.id, userId, Translator.getMessage(SuccessCode.MENU_REMOVE))
                }
            }.doOnSuccess {
                logger.info("End removeMenu by userId: $userId with response: $it")
                logger.info("End removeMenu by userId: $userId with request: $it to feed")
            }.doOnError {
                logger.error("Error removeMenu by userId: $userId with error: ${it.getError()}")
            }.onErrorResumeNext {
                Single.error(MenuException("400", Translator.getMessage(ErrorCode.MENU_DOES_NOT_EXIST)))
            }
    }

    fun saveMenu(menu: Menu, userId: Long): Single<Menu> {
        logger.info("Start saveMenu by userId: $userId with request: $menu")

        return eventService.findById(menu.eventId)
            .filter {
                it.isPresent
            }.flatMapSingle {
                save(menu)
            }.doOnSuccess {
                logger.info("End saveMenu by userId: $userId with response: $it")
            }.doOnError {
                logger.error("Error saveMenu by userId: $userId with error: ${it.getError()}")
            }.onErrorResumeNext {
                Single.error(MenuException("400", Translator.getMessage(ErrorCode.EVENT_DOES_NOT_EXIST)))
            }
    }

    fun checkMenuAll(id: String, userId: Long): Single<MutableList<Menu>> {
        logger.info("Start checkMenuAll by userId: $userId with id: $id")

        return findByEventId(id)
            .flatMap {
                just(it)
            }.doOnSuccess {
                logger.info("End checkMenuAll by userId: $userId with response: $it")
            }.doOnError {
                logger.error("Error checkMenuAll by userId: $userId with error: ${it.getError()}")
            }.onErrorResumeNext {
                Single.error(EventException("400", Translator.getMessage(ErrorCode.MENU_DOES_NOT_EXIST)))
            }
    }

    fun checkMenu(id: String, userId: Long): Single<Menu> {
        logger.info("Start checkMenu by userId: $userId with id: $id")

        return findById(id)
            .filter {
                it.isPresent
            }.flatMapSingle {
                just(it.get())
            }.doOnSuccess {
                logger.info("End checkMenu by userId: $userId with response: $it")
            }.doOnError {
                logger.error("Error checkMenu by userId: $userId with error: ${it.getError()}")
            }.onErrorResumeNext {
                Single.error(EventException("400", Translator.getMessage(ErrorCode.MENU_DOES_NOT_EXIST)))
            }
    }

    private fun findByEventId(id: String) = just(menuRepository.findByEventId(id))

    private fun remove(id: String) = just(menuRepository.deleteById(id))

    private fun findById(id: String) = just(menuRepository.findById(id))

    private fun save(menu: Menu) = just(menuRepository.save(menu))
}
