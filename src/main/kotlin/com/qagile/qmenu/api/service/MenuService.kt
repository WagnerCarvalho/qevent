package com.qagile.qmenu.api.service

import com.qagile.qmenu.api.domain.Menu
import com.qagile.qmenu.api.entities.exception.MenuException
import com.qagile.qmenu.api.entities.request.CreateMenuRequest
import com.qagile.qmenu.api.repository.MenuRepository
import com.qagile.qmenu.api.utils.ErrorCode
import com.qagile.qmenu.api.utils.Translator
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

    fun checkCreateMenu(createMenuRequest: CreateMenuRequest, applicationUserId: Long): Single<Menu> {
        logger.info("Start checkcreateMenu by applicationUserId: $applicationUserId with request: $createMenuRequest")

        return saveMenu(Menu().convertToMenu(createMenuRequest), applicationUserId)
    }

    fun saveMenu(menu: Menu, applicationUserId: Long): Single<Menu> {
        logger.info("Start saveEvent by applicationUserId: $applicationUserId with request: $menu")

        return save(menu)
            .doOnSuccess {
                logger.info("End saveEvent by applicationUserId: $applicationUserId with response: $it")
                logger.info("End saveEvent by applicationUserId: $applicationUserId with request: $it to feed")
            }.doOnError {
                logger.error("Error saveEvent by applicationUserId: $applicationUserId with error: $it")
            }.onErrorResumeNext {
                Single.error(MenuException("400", Translator.getMessage(ErrorCode.UNREGISTERED_PRODUCT)))
            }
    }

    private fun save(menu: Menu) = just(menuRepository.save(menu))
}
