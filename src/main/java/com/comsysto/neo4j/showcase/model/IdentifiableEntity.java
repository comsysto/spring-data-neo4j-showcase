package com.comsysto.neo4j.showcase.model;

import org.springframework.data.neo4j.annotation.GraphId;

/**
 * @author: rkowalewski
 */
public abstract class IdentifiableEntity {
    @GraphId
    private Long graphId;

    public Long getGraphId() {
        return graphId;
    }

    @Override
    public boolean equals( Object obj ) {
        return obj instanceof IdentifiableEntity && graphId.equals( ((IdentifiableEntity) obj).getGraphId() );
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
