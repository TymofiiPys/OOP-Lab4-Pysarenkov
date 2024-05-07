package com.restaurant.mapper;

import com.restaurant.dto.PaymentCreateDTO;
import com.restaurant.dto.PaymentDisplayDTO;
import com.restaurant.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    PaymentCreateDTO toPaymentDTO(Payment payment);
    PaymentDisplayDTO toPaymentDisplayDTO(Payment payment);
}
