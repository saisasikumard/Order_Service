package com.example.order_Service.service;

import com.example.order_Service.common.Payment;
import com.example.order_Service.common.TransactionRequest;
import com.example.order_Service.common.TransactionResponse;
import com.example.order_Service.entity.Order;
import com.example.order_Service.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {
    @Autowired
    OrderRepo orderRepo;

    @Autowired
    RestTemplate restTemplate;

    public TransactionResponse saveOrder(TransactionRequest transactionRequest){
        String message;
        Order order=transactionRequest.getOrder();
        Payment payment = transactionRequest.getPayment();
        //place rest call to payment


        payment.setAmount(order.getPrice()* order.getQty());
        HttpEntity<Payment> requestEntity = new HttpEntity<>(payment);
        Order savedOrder=orderRepo.save(order);
        payment.setOrderId(savedOrder.getId());
        Payment paymentResponse = restTemplate.postForObject("http://PAYMENT-SERVICE/payment/doPayment",payment, Payment.class);
       // System.out.println("Order ID: " + order.getId() + ", Amount: " + order.getPrice());
        // Create HttpEntity with the request body and headers

        message= paymentResponse.getPaymentStatus().equals("Success")?"Success your order placed successfully":"payment error ,order added to cart";

        System.out.println("Order ID: " + order.getId() + ", Amount: " + order.getPrice());
        System.out.println(paymentResponse.getAmount()+" "+ paymentResponse.getOrderId());
        TransactionResponse transactionResponse=new TransactionResponse();
        transactionResponse.setOrder(savedOrder);
        transactionResponse.setAmount(paymentResponse.getAmount());
        transactionResponse.setTransactionId(paymentResponse.getTranscationId());
        transactionResponse.setMessage(message);
        return transactionResponse;
    }
}
