package com.qagile.qevent.api.controller

import com.qagile.qevent.api.domain.Menu
import com.qagile.qevent.api.entities.request.CreateMenuRequest
import com.qagile.qevent.api.entities.request.DeleteRequest
import com.qagile.qevent.api.entities.request.UpdateMenuRequest
import com.qagile.qevent.api.entities.response.DeleteResponse
import com.qagile.qevent.api.entities.response.MenuResponse
import com.qagile.qevent.api.routers.MenuRouter
import com.qagile.qevent.api.service.MenuService
import com.qagile.qevent.api.utils.toFutureResponse
import java.util.concurrent.Future
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class MenuController {

    @Autowired
    private lateinit var menuService: MenuService

    @GetMapping(MenuRouter.GET_MENU_ALL_V1)
    fun getMenuAll(
        @PathVariable id: String,
        @RequestHeader(value = "user_id") userId: Long,
        @RequestHeader(value = "apikey") apiKey: String
    ): Future<MenuResponse> {
        return menuService.checkMenuAll(id, userId).toFutureResponse()
    }

    @GetMapping(MenuRouter.GET_MENU_V1)
    fun getMenu(
        @PathVariable id: String,
        @RequestHeader(value = "user_id") userId: Long,
        @RequestHeader(value = "apikey") apiKey: String
    ): Future<Menu> {
        return menuService.checkMenu(id, userId).toFutureResponse()
    }

    @PutMapping(MenuRouter.UPDATE_MENU_V1)
    fun updateMenu(
        @PathVariable id: String,
        @Valid @RequestBody updateMenuRequest: UpdateMenuRequest,
        @RequestHeader(value = "user_id") userId: Long,
        @RequestHeader(value = "apikey") apiKey: String
    ): Future<Menu> {

        return menuService.checkUpdateMenu(updateMenuRequest.get(id), userId).toFutureResponse()
    }

    @PostMapping(MenuRouter.CREATE_MENU_V1)
    fun createMenu(
        @Valid @RequestBody createMenuRequest: CreateMenuRequest,
        @RequestHeader(value = "user_id") userId: Long,
        @RequestHeader(value = "apikey") apiKey: String
    ): Future<Menu> {

        return menuService.checkCreateMenu(createMenuRequest, userId).toFutureResponse()
    }

    @DeleteMapping(MenuRouter.DELETE_MENU_V1)
    fun deleteMenu(
        @PathVariable id: String,
        @RequestHeader(value = "user_id") userId: Long,
        @RequestHeader(value = "apikey") apiKey: String
    ): Future<DeleteResponse> {

        return menuService.removeMenu(DeleteRequest(id), userId).toFutureResponse()
    }
}
