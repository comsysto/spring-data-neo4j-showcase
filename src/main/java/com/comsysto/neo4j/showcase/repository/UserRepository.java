package com.comsysto.neo4j.showcase.repository;

import com.comsysto.neo4j.showcase.model.User;
import org.springframework.data.neo4j.repository.GraphRepository;


public interface UserRepository extends GraphRepository<User>{

    User findByUserId(String userId);

    Iterable<User> findByUserNameLike(String userName);
}
