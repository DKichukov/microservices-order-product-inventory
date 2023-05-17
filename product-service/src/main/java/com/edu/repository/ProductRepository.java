package com.edu.repository;

import com.edu.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("select p from Product p where p.name=?1")
    Product findByName(String name);
}
