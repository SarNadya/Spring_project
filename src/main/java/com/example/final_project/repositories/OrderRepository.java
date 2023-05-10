package com.example.final_project.repositories;

import com.example.final_project.enumeration.Status;
import com.example.final_project.models.Order;
import com.example.final_project.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByPerson(Person person);

    @Modifying
    @Query("UPDATE Order orders SET orders.status = ?2 WHERE orders.id = ?1")
    int updateOrderStatus(int id, Status status);

//    @Query(value = "select * from orders where (number LIKE %?)", nativeQuery = true)
//    List<Order> findByNumberOrder(String name);

    // Поиск всех заказов по части номера в конце
    List<Order> findByNumberEndingWith(String name);

}
