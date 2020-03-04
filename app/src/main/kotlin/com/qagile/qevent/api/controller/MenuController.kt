package com.qagile.qevent.api.controller

import com.qagile.qevent.api.domain.Menu
import com.qagile.qevent.api.entities.request.CreateMenuRequest
import com.qagile.qevent.api.entities.request.DeleteRequest
import com.qagile.qevent.api.entities.request.UpdateMenuRequest
import com.qagile.qevent.api.entities.response.DeleteResponse
import com.qagile.qevent.api.routers.MenuRouter
import com.qagile.qevent.api.service.MenuService
import com.qagile.qevent.api.utils.toFutureResponse
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
