package com.comsysto.springDataNeo4j.showcase;

import org.springframework.data.neo4j.annotation.*;


@RelationshipEntity(type = RelationshipTypes.CLICKED)
public class ClickedRelationship
{
    @GraphId
    private Long graphId;

    @StartNode
    private User user;

    @EndNode
    @Fetch
    private Product product;

    private Integer count;

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


    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClickedRelationship that = (ClickedRelationship) o;

        if (product != null ? !product.equals(that.product) : that.product != null) return false;
        if (count != null ? !count.equals(that.count) : that.count != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        return result;
    }
}
