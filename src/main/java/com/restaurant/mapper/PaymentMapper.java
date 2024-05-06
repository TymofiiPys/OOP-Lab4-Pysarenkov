package com.restaurant.mapper;

import com.restaurant.dto.PaymentDTO;
import com.restaurant.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    PaymentDTO toPaymentDTO(Payment payment);
}
