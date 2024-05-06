package com.restaurant.service;

import com.restaurant.dao.MenuDAO;
import com.restaurant.dto.MenuDTO;
import com.restaurant.mapper.MenuMapper;
import com.restaurant.model.Menu;

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
}
