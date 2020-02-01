package com.qagile.qmenu.api.controller

import com.qagile.qmenu.api.domain.Menu
import com.qagile.qmenu.api.entities.request.CreateMenuRequest
import com.qagile.qmenu.api.entities.request.DeleteRequest
import com.qagile.qmenu.api.entities.request.UpdateMenuRequest
import com.qagile.qmenu.api.entities.response.DeleteResponse
import com.qagile.qmenu.api.routers.MenuRouter
import com.qagile.qmenu.api.service.MenuService
import com.qagile.qmenu.api.utils.toFutureResponse
import java.util.concurrent.Future
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class MenuController {

    @Autowired
    private lateinit var menuService: MenuService

    @PutMapping(MenuRouter.UPDATE_MENU_V1)
    fun updateMenu(
        @Valid @RequestBody updateMenuRequest: UpdateMenuRequest,
        @RequestHeader(value = "user_id") applicationUserId: Long
    ): Future<Menu> {

        return menuService.checkUpdateMenu(updateMenuRequest, applicationUserId).toFutureResponse()
    }

    @PostMapping(MenuRouter.CREATE_MENU_V1)
    fun createMenu(
        @Valid @RequestBody createMenuRequest: CreateMenuRequest,
        @RequestHeader(value = "user_id") applicationUserId: Long
    ): Future<Menu> {

        return menuService.checkCreateMenu(createMenuRequest, applicationUserId).toFutureResponse()
    }

    @DeleteMapping(MenuRouter.DELETE_MENU_V1)
    fun deleteMenu(
        @Valid @RequestBody deleteMenuRequest: DeleteRequest,
        @RequestHeader(value = "user_id") applicationUserId: Long
    ): Future<DeleteResponse> {

        return menuService.removeMenu(deleteMenuRequest, applicationUserId).toFutureResponse()
    }
}
