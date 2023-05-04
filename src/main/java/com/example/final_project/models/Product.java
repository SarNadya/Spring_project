package com.example.final_project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false, columnDefinition = "text", unique = true)
    @NotEmpty(message = "Наименование товара не может быть пустым")
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    @NotEmpty(message = "Описание товара не может быть пустым")
    private String description;

    @Column(name = "price", nullable = false)
    @Min(value = 1 , message = "Цена товара не может быть отрицательной или нулевой")
    private float price;

    @Column(name = "warehouse", nullable = false)
    @NotEmpty(message = "Cклад товара не может быть пустым")
    private String warehouse;

    @Column(name = "seller", nullable = false)
    @NotEmpty(message = "Информация о продавце не может быть пустой")
    private String seller;

    @ManyToOne(optional = false)
    private Category category;

    private LocalDateTime dateTime;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private List<Image> imageList = new ArrayList<>();


    // Данный метод будет заполнять поле даты и времени при создании объекта класса
    @PrePersist
    private void init(){
        dateTime = LocalDateTime.now();
    }

    // Данный метод позволяет добавить фотографию в лист текущего продукта
    public void addImageToProduct(Image image){
        image.setProduct(this);
        imageList.add(image);
    }

    public Product(String title, String description, float price, String warehouse, String seller, Category category, LocalDateTime dateTime, List<Image> imageList) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.warehouse = warehouse;
        this.seller = seller;
        this.category = category;
        this.dateTime = dateTime;
        this.imageList = imageList;
    }

    public Product() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }
}
