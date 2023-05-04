package com.example.final_project.models;

import jakarta.persistence.*;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fileName;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Product product;

    public Image(int id, String fileName, Product product) {
        this.id = id;
        this.fileName = fileName;
        this.product = product;
    }

    public Image() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
