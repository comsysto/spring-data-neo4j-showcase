package com.comsysto.springDataNeo4j.showcase;

import org.springframework.data.neo4j.repository.GraphRepository;


public interface UserRepository extends GraphRepository<User>{

    User findByUserId(String userId);

    Iterable<User> findByUserNameLike(String userName);
}
