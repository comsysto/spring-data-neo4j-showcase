package com.comsysto.springDataNeo4j.showcase;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/comsysto/springDataNeo4j/showcase/related-to-via-test-context.xml"})
@Transactional
public class SpringDataNeo4jProductUserTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    GraphDatabaseService graphDatabaseService;

    User jordan, pippen, miller;
    Product pizzaMargarita, pizzaFungi, pizzaSalami, pizzaVegitarian, pizzaRustica;

    public void createSzenario () {
        jordan = createUser("MJ", "Monika Jordan");
        pippen = createUser("SP", "Sandra Pippen");
        miller = createUser("JM", "John Miller");

        pizzaMargarita = createProduct("Pizza_1", "Pizza Margarita");
        pizzaFungi = createProduct("Pizza_1", "Pizza Fungi");
        pizzaSalami = createProduct("Pizza_1", "Pizza Salami");
        pizzaVegitarian = createProduct("Pizza_1", "Pizza Vegitarian");
        pizzaRustica = createProduct("Pizza_1", "Pizza Rustica");

        jordan.addClickedProduct(pizzaMargarita);
        jordan.addClickedProduct(pizzaFungi);
        jordan.addClickedProduct(pizzaSalami);

        pippen.addClickedProduct(pizzaMargarita);
        pippen.addClickedProduct(pizzaVegitarian);
        pippen.addClickedProduct(pizzaRustica);

        miller.addClickedProduct(pizzaFungi);

    }

    @Test
    public void testClickedRelationships() {

        createSzenario();

        //Load and check relations

        List<Player> allProducts = productRepository.findAll().as(List.class);
        assertEquals("there should be three products in the products repository", 5, allProducts.size());

        assertTrue("saved and loaded products should be equal",
                allProducts.contains(pizzaMargarita) && allProducts.contains(pizzaFungi) &&
                allProducts.contains(pizzaSalami) && allProducts.contains(pizzaVegitarian) &&
                allProducts.contains(pizzaRustica));


        List<User> allUsers = userRepository.findAll().as(List.class);
        assertEquals("there should be three users in the user repository", 3, allUsers.size());

        Set<Product> clickedProducts = allUsers.get(0).getClickedProducts();
        assertEquals("Monika Jordan should have three clicked products", 3, clickedProducts.size());
        assertTrue("The two products Monika Jordan clicked on should be pizza margarita and pizza fungi",
                clickedProducts.contains(pizzaMargarita) && clickedProducts.contains(pizzaFungi) && clickedProducts.contains(pizzaSalami));
        }

    @Test
    public void testNamedCypherQuerys() {

        createSzenario();

        List<Product> alsoViewedProducts = productRepository.findOtherUsersAlsoViewedProducts(pizzaMargarita.getProductId());
        assertTrue("using this cypher query should return a list with also viewed products",
                alsoViewedProducts.contains(pizzaFungi) && alsoViewedProducts.contains(pizzaVegitarian));

        List<Product> alsoViewedProductsWithoutAlreadyViewed = productRepository.findOtherUsersAlsoViewedProductsWithoutAlreadyViewed(pizzaMargarita.getProductId(), miller.getUserId());
        assertTrue("using this cypher query should return a list with also viewed products without the ones Miller already viewed",
                alsoViewedProducts.contains(pizzaVegitarian));


    }

    private Product createProduct(String id, String name) {
        return productRepository.save(new Product(id, name));
    }

    private User createUser(String id, String name) {
        return userRepository.save(new User(id, name));
    }

    @After
    public void cleanDB() {
        Neo4jHelper.cleanDb(graphDatabaseService);
    }
}
