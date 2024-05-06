package com.restaurant.mapper;

import com.restaurant.dto.OrderDTO;
import com.restaurant.dto.OrderReceiveDTO;
import com.restaurant.dto.PaymentDTO;
import com.restaurant.model.Order;
import com.restaurant.model.Payment;
import org.mapstruct.factory.Mappers;

public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    PaymentDTO toPaymentDTO(Payment payment);
}
