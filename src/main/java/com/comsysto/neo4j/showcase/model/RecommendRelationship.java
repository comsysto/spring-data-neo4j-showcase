package com.comsysto.neo4j.showcase.model;

import org.springframework.data.neo4j.annotation.*;


@RelationshipEntity(type = RelationshipTypes.RECOMMEND)
public class RecommendRelationship
{
    @GraphId
    private Long graphId;

    @StartNode
    @Fetch
    private Product productStart;

    @EndNode
    @Fetch
    private Product productEnd;

    private int count = 1;

    public RecommendRelationship() {/* NOOP */}

    public RecommendRelationship(Product productStart, Product productEnd)
    {
        this.productStart = productStart;
        this.productEnd = productEnd;
    }


    public Product getProductEnd() {
        return productEnd;
    }

    public void setProductEnd(Product productEnd) {
        this.productEnd = productEnd;
    }

    public Product getProductStart() {
        return productStart;
    }

    public void setProductStart(Product productStart) {
        this.productStart = productStart;
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

        RecommendRelationship that = (RecommendRelationship) o;

        if (productEnd != null ? !productEnd.equals(that.productEnd) : that.productEnd != null) return false;
        if (productStart != null ? !productStart.equals(that.productStart) : that.productStart != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (productStart != null ? productStart.hashCode() : 0);
        result = 31 * result + (productEnd != null ? productEnd.hashCode() : 0);
        return result;
    }
}
