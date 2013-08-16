package com.comsysto.neo4j.showcase.model;

import org.springframework.data.neo4j.annotation.*;


@RelationshipEntity(type = RelationshipTypes.CLICKED)
public class ClickedRelationship
{
    @GraphId
    private Long graphId;

    @StartNode
    @Fetch
    private User user;

    @EndNode
    @Fetch
    private Product product;

    private int count = 1;

    public ClickedRelationship() {/* NOOP */}

    public ClickedRelationship(User user, Product product)
    {
        this.user = user;
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void incrementCount () {
        this.count++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClickedRelationship that = (ClickedRelationship) o;

        if (product != null ? !product.equals(that.product) : that.product != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        return result;
    }
}
