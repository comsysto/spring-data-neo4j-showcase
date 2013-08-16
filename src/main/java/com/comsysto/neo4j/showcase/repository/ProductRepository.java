package com.comsysto.neo4j.showcase.repository;

import com.comsysto.neo4j.showcase.model.Product;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends GraphRepository<Product> {

    Product findByProductId(String productId);

    List<Product> findByProductNameLike(String productName);


    @Query("START product=node(*) " +
            "WHERE HAS (product.productName)" +
            "RETURN product " +
            "ORDER BY product.productName")
    List<Product> findAllProductsSortedByName();


    @Query("START product=node:Product(productId={productId}) " +
            "MATCH product-[recommend:RECOMMEND]->otherProduct " +
            "RETURN otherProduct " +
            "ORDER BY recommend.count DESC " +
            "LIMIT 5 ")
    List<Product> findOtherUsersAlsoViewedProducts(@Param("productId") String productId);


    @Query("START product=node:Product(productId={productId}), user=node:User(userId={userId}) " +
            "MATCH product-[recommend:RECOMMEND]->otherProduct " +
            "WHERE not(user-[:CLICKED]->otherProduct) " +
            "RETURN otherProduct " +
            "ORDER BY recommend.count DESC " +
            "LIMIT 5 ")
    List<Product> findOtherUsersAlsoViewedProductsWithoutAlreadyViewed(@Param("productId") String productId, @Param("userId") String userId);

}
