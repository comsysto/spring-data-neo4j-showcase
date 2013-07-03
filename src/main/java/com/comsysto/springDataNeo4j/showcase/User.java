package com.comsysto.springDataNeo4j.showcase;

import org.springframework.data.neo4j.annotation.*;
import org.springframework.data.neo4j.support.index.IndexType;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class User extends IdentifiableEntity {

    @Indexed(indexName = "userId")
    private String userId;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "userName")
    private String userName;

    private Product clickedBofore = null;

    @RelatedToVia(type = RelationshipTypes.CLICKED)
    private Set<Product> clickedProducts = new HashSet<Product>();


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

    public Set<Product> getClickedProducts() {
        return clickedProducts;
    }

    public void addClickedProduct(Product clickedProduct)
    {

        if (this.clickedBofore != null) {
            this.clickedBofore.addProductViewed(clickedProduct);
        }

        this.clickedProducts.add(clickedProduct);

        //clickedProduct.addUserClicked(this);

        this.clickedBofore = clickedProduct;

    }

    @Override
    public String toString() {
        return "User{" +
                "graphId=" + this.getGraphId() +
                ", userId=" + this.userId +
                ", userName=" + this.userName +
                //", #clickedProducts=" + this.clickedProducts.size() +
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
