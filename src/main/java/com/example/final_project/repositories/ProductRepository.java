package com.example.final_project.repositories;

import com.example.final_project.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Поиск всех продуктов по части наименования продукта в не зависомости от регистра
    List<Product> findByTitleContainingIgnoreCase(String name);

    // Поиск по наименованию и фильтрация по диапазону цены
    @Query(value = "select * from product where ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3)", nativeQuery = true)
    List<Product> findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(String title, float min, float max);

    // Поиск по наименованию и фильтрация по диапазону цены, а также сортировка по возрастанию цены
    @Query(value = "select * from product where (lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1') and (price >= ?2 and price <= ?3) order by price",nativeQuery = true)
    List<Product> findByTitleOrderByPriceAsc(String title, float min, float max);

    // Поиск по наименованию и фильтрация по диапазону цены, а также сортировка по убыванию цены
    @Query(value = "select * from product where (lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1') and (price >= ?2 and price <= ?3) order by price desc",nativeQuery = true)
    List<Product> findByTitleOrderByPriceDesc(String title, float min, float max);

    // Поиск по наименованию и фильтрация по диапазону цены, сортировка по возрастанию цены,  а также фильтрация по категории
    @Query(value = "select * from product where category_id = ?4 and(lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1') and (price >= ?2 and price <= ?3) order by price",nativeQuery = true)
    List<Product> findByTitleAndCategoryOrderByPriceAsc(String title, float min, float max, int category);

    // Поиск по наименованию и фильтрация по диапазону цены, сортировка по убыванию цены,  а также фильтрация по категории
    @Query(value = "select * from product where category_id = ?4 and(lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1') and (price >= ?2 and price <= ?3) order by price desc",nativeQuery = true)
    List<Product> findByTitleAndCategoryOrderByPriceDesc(String title, float min, float max, int category);
}
