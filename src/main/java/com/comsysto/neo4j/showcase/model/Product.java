package com.comsysto.neo4j.showcase.model;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.data.neo4j.support.index.IndexType;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Product extends IdentifiableEntity {

    @Indexed(unique = true)
    private String productId;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "productName")
    private String productName;

    @RelatedToVia(type = RelationshipTypes.RECOMMEND)
    private Set<RecommendRelationship> productsRecommendRelationships = new HashSet<RecommendRelationship>();


    public Product() {/* NOOP */}

    public Product(String productId, String productName) {
        super();

        this.productId = productId;
        this.productName = productName;

    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Set<RecommendRelationship> getProductsRecommendRelationships() {
        return productsRecommendRelationships;
    }

    public Set<Product> getAllProductsRecommendations() {

        Set recommendProducts = new HashSet<Product>();

        for (RecommendRelationship recommendRelationship : this.productsRecommendRelationships) {
            recommendProducts.add(recommendRelationship.getProductEnd());
        }

        return recommendProducts;
    }

    public void setProductsRecommendRelationships(Set<RecommendRelationship> productsRecommendRelationships) {
        this.productsRecommendRelationships = productsRecommendRelationships;
    }

    public void addProductRecommend(Product productRecommend)
    {
        RecommendRelationship recommendRelationship = new RecommendRelationship(this, productRecommend);

        if (this.productsRecommendRelationships.contains(recommendRelationship))
        {
            for(RecommendRelationship recommendRel : this.productsRecommendRelationships) {
                if (recommendRel.getProductEnd().equals(productRecommend)) {
                    recommendRel.incrementCount();
                    break;
                }
            }
        }
        else {
            productsRecommendRelationships.add(recommendRelationship);
        }
    }


    @Override
    public String toString() {
        return "Product{" +
                "graphId=" + this.getGraphId() +
                ", productId=" + productId +
                ", productName=" + productName +
                //", #productsRecommendRelationships=" + productsRecommendRelationships.size() +
                //", #userClicked=" + usersClicked.size() +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Product product = (Product) o;

        if (productId != null ? !productId.equals(product.productId) : product.productId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        return result;
    }

}
