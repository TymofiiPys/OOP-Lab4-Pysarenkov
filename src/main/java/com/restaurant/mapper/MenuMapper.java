package com.restaurant.mapper;

import com.restaurant.dto.MenuDTO;
import com.restaurant.model.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuMapper {
    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

    MenuDTO toMenuDTO(Menu menu);
}
