package com.comsysto.springDataNeo4j.showcase;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends GraphRepository<Product> {

    Product findByProductId(String productId);

    List<Product> findByProductNameLike(String productName);

    @Query("START product=node:Product(productId='{productId}') " +
            "MATCH product-[viewed:VIEWED]->otherProduct " +
            "RETURN distinct otherProduct " +
            "ORDER BY viewed.count desc " +
            "LIMIT 5")
    List<Product> findOtherUsersAlsoViewedProducts(@Param("productId") String productId);

    @Query("START product=node:Product(productId='*') " +
            "RETURN distinct product " +
            "ORDER BY product.productName")
    List<Product> findAllProductsSortedByName();

    @Query("START product=node:Product(productId='{productId}'), user=node:User(userId='{userId}') " +
            "MATCH user-[clicked:CLICKED]->product-[viewed:VIEWED]->otherProduct " +
            "WHERE not(user-[:CLICKED]->otherProduct) " +
            "RETURN distinct otherProduct " +
            "ORDER BY viewed.count DESC " +
            "LIMIT 5")
    List<Product> findOtherUsersAlsoViewedProductsWithoutAlreadyViewed(@Param("productId") String productId, @Param("userId") String userId);

}
