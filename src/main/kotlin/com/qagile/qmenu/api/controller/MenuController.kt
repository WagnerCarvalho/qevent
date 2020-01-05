package com.qagile.qmenu.api.controller

import com.qagile.qmenu.api.domain.Menu
import com.qagile.qmenu.api.entities.request.CreateMenuRequest
import com.qagile.qmenu.api.routers.MenuRouter
import com.qagile.qmenu.api.service.MenuService
import com.qagile.qmenu.api.utils.toFutureResponse
import java.util.concurrent.Future
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class MenuController {

    @Autowired
    private lateinit var menuService: MenuService

    @PostMapping(MenuRouter.CREATE_MENU_V1)
    fun createMenu(
        @Valid @RequestBody createMenuRequest: CreateMenuRequest,
        @RequestHeader(value = "user_id") applicationUserId: Long
    ): Future<Menu> {

        return menuService.checkCreateMenu(createMenuRequest, applicationUserId).toFutureResponse()
    }
}
