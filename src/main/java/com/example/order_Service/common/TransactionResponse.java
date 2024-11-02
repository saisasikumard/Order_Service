package com.example.order_Service.common;

import com.example.order_Service.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionResponse {
    Order order;
    double amount;
    String transactionId;
    String message;

}