package com.comsysto.springDataNeo4j.showcase;

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

    @RelatedToVia(type = RelationshipTypes.VIEWED)
    private Set<ViewedRelationship> productsViewedRelationships = new HashSet<ViewedRelationship>();


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

    public Set<ViewedRelationship> getProductsViewedRelationships() {
        return productsViewedRelationships;
    }

    public Set<Product> getAllProductsViewed() {

        Set viewedProducts = new HashSet<Product>();

        for (ViewedRelationship viewedRelationship : this.productsViewedRelationships) {
            viewedProducts.add(viewedRelationship.getProductEnd());
        }

        return viewedProducts;
    }

    public void setProductsViewedRelationships(Set<ViewedRelationship> productsViewedRelationships) {
        this.productsViewedRelationships = productsViewedRelationships;
    }

    public void addProductViewed(Product productViewed)
    {
        ViewedRelationship viewedRelationship = new ViewedRelationship(this, productViewed);

        if (this.productsViewedRelationships.contains(viewedRelationship))
        {
            for(ViewedRelationship viewedRel : this.productsViewedRelationships) {
                if (viewedRel.getProductEnd().equals(productViewed)) {
                    viewedRel.incrementCount();
                    break;
                }
            }
        }
        else {
            productsViewedRelationships.add(viewedRelationship);
        }
    }


    @Override
    public String toString() {
        return "Product{" +
                "graphId=" + this.getGraphId() +
                ", productId=" + productId +
                ", productName=" + productName +
                //", #productsViewedRelationships=" + productsViewedRelationships.size() +
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
