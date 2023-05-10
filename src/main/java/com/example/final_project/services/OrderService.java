package com.example.final_project.services;

import com.example.final_project.enumeration.Status;
import com.example.final_project.models.Order;
import com.example.final_project.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Данный метод позволяет получить список всех заказов
    public List<Order> getAllOrder(){
        return orderRepository.findAll();
    }

    // Данный метод позволяет получить заказ по id
    public Order getOrderId(int id){
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.orElse(null);
    }

    // Данный метод позволяет обновить статус заказа
    @Transactional
    public void updateOrderStatus(int id, Status status){
        orderRepository.updateOrderStatus(id, status);
    }

    // Данный метод позволяет найти заказ(ы) по части номера в конце
    public List<Order> findOrder(String search){
        return orderRepository.findByNumberEndingWith(search);
    }
}
