package com.restaurant.mapper;

import com.restaurant.dto.OrderDTO;
import com.restaurant.dto.OrderReceiveDTO;
import com.restaurant.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDTO toOrderDTO(Order order);
    Order fromOrderReceive(OrderReceiveDTO order);
}
