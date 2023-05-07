package com.example.final_project.models;

import jakarta.persistence.*;

@Entity
@Table(name = "product_cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // id пользователя, к которому относится данная корзина товаров
    // по этому id будет выгружаться из БД корзина товаров в личном кабинете пользователя
    @Column(name = "person_id")
    private int personId;

    // id товара, который привязан к пользователю
    @Column(name = "product_id")
    private int productId;

    public Cart(int personId, int productId) {
        this.personId = personId;
        this.productId = productId;
    }

    public Cart() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
