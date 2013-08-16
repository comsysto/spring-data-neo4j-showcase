package com.comsysto.neo4j.showcase.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.*;

/**
 * @author: rkowalewski
 */
public class Main {

    private static final String CLASSPATH_LOCATION = "classpath:com/comsysto/neo4j/showcase/main/related-to-via-test-context.xml";

    public static void main(String[] args) throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CLASSPATH_LOCATION);

        JtaTransactionManager tx = (JtaTransactionManager) context.getBean("neo4jTransactionManager");
        Neo4jPersister neo4jPersister = (Neo4jPersister) context.getBean("neo4jPersister");

        tx.getTransactionManager().begin();

        neo4jPersister.createTestData();

        tx.getTransactionManager().commit();
    }
}
