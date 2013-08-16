package com.comsysto.neo4j.showcase.model;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.data.neo4j.support.index.IndexType;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class User extends IdentifiableEntity {

    @Indexed(unique = true)
    private String userId;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "userName")
    private String userName;

    private Product clickedBefore;

    @RelatedToVia(type = RelationshipTypes.CLICKED)
    private Set<ClickedRelationship> clickedProductsRelationships = new HashSet<ClickedRelationship>();


    public User() {/* NOOP */}

    public User(String userId, String name) {
        super();

        this.userId = userId;
        this.userName = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Product getClickedBefore() {
        return clickedBefore;
    }

    public Set<Product> getAllClickedProducts() {
        Set clickedProducts = new HashSet<Product>();

        for (ClickedRelationship clickedRelationship : this.clickedProductsRelationships) {
            clickedProducts.add(clickedRelationship.getProduct());
        }

        return clickedProducts;
    }

    public Set<ClickedRelationship> getClickedProductsRelationships() {
        return clickedProductsRelationships;
    }

    public void addClickedProduct(Product clickedProduct)
    {
        ClickedRelationship clickedRelationship = new ClickedRelationship(this, clickedProduct);

        if (this.clickedBefore != null) {
            this.clickedBefore.addProductRecommend(clickedProduct);
        }

        if (this.clickedProductsRelationships.contains(clickedRelationship))
        {
             for(ClickedRelationship clickRel : this.clickedProductsRelationships) {
                 if (clickRel.getProduct().equals(clickedProduct)) {
                     clickRel.incrementCount();
                     break;
                 }
             }
        }
        else {
            this.clickedProductsRelationships.add(clickedRelationship);
        }

        this.clickedBefore = clickedProduct;

    }

    @Override
    public String toString() {
        return "User{" +
                "graphId=" + this.getGraphId() +
                ", userId=" + this.userId +
                ", userName=" + this.userName +
                //", #clickedProductsRelationships=" + this.clickedProductsRelationships.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        if (userId != null ? !userId.equals(user.userId) : user.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
