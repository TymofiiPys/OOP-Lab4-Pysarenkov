package com.restaurant.service;

import com.restaurant.dao.MenuDAO;
import com.restaurant.dto.MenuCreateDTO;
import com.restaurant.dto.MenuDTO;
import com.restaurant.mapper.MenuMapper;
import com.restaurant.model.Menu;

import java.util.ArrayList;
import java.util.List;

public class MenuService {
    private final MenuDAO menuDAO = new MenuDAO();
    private final MenuMapper mapper = MenuMapper.INSTANCE;

    public List<MenuDTO> getMenu() {
//        log.info("Parsed menu from DB");
        List<Menu> menus = menuDAO.readMenu();
        if(menus == null) return null;
        return menuDAO.readMenu().stream().map(mapper::toMenuDTO).toList();
    }

    public List<MenuDTO> createMenu(List<MenuCreateDTO> menuCreateDTOS) {
        List<Menu> menu = menuCreateDTOS.stream().map(mapper::fromMenuCreate).toList();
        List<MenuDTO> createdMenu = new ArrayList<>();
        for (Menu item : menu) {
            Menu createdItem = menuDAO.createMenu(item);
            if (createdItem != null) {
                createdMenu.add(mapper.toMenuDTO(createdItem));
            }
        }
        return createdMenu;
    }

    public List<MenuDTO> updateMenu(List<MenuDTO> menuToUpdate) {
        List<Menu> menu = menuToUpdate.stream().map(mapper::fromMenuDTO).toList();
        List<MenuDTO> updatedMenu = new ArrayList<>();
        for (Menu item : menu) {
            Menu updatedItem = menuDAO.updateMenu(item);
            if (updatedItem != null) {
               updatedMenu.add(mapper.toMenuDTO(updatedItem));
            }
        }
        return updatedMenu;
    }
}
