package com.comsysto.springDataNeo4j.showcase;

import org.junit.After;
import org.junit.Before;
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
import static org.junit.Assert.assertEquals;


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

    @Before
    public void createSzenario () {

        jordan = createUser("MJ", "Monika Jordan");
        pippen = createUser("SP", "Sandra Pippen");
        miller = createUser("JM", "John Miller");

        pizzaMargarita = createProduct("Pizza_1", "Pizza Margarita");
        pizzaFungi = createProduct("Pizza_2", "Pizza Fungi");
        pizzaSalami = createProduct("Pizza_3", "Pizza Salami");
        pizzaVegitarian = createProduct("Pizza_4", "Pizza Vegitarian");
        pizzaRustica = createProduct("Pizza_5", "Pizza Rustica");

        userClickedProduct(jordan, pizzaMargarita);
        userClickedProduct(jordan, pizzaFungi);
        userClickedProduct(jordan, pizzaSalami);

        userClickedProduct(pippen, pizzaMargarita);
        userClickedProduct(pippen, pizzaVegitarian);
        userClickedProduct(pippen, pizzaRustica);
        userClickedProduct(pippen, pizzaMargarita);
        userClickedProduct(pippen, pizzaVegitarian);

        userClickedProduct(miller, pizzaFungi);
    }

    @Test
    public void testProducts() {

        //Load and check relations
        List<Product> allProducts = productRepository.findAll().as(List.class);
        assertEquals("there should be five products in the products repository", 5, allProducts.size());

        assertTrue("saved and loaded products should be equal",
                allProducts.contains(pizzaMargarita) && allProducts.contains(pizzaFungi) &&
                allProducts.contains(pizzaSalami) && allProducts.contains(pizzaVegitarian) &&
                allProducts.contains(pizzaRustica));

    }

    @Test
    public void testProductsSortedByName() {

        //Load and check relations
        List<Product> allProducts = productRepository.findAllProductsSortedByName();
        assertEquals("there should be five products in the products repository", 5, allProducts.size());

        assertTrue("saved and loaded products should be equal",
                allProducts.contains(pizzaMargarita) && allProducts.contains(pizzaFungi) &&
                        allProducts.contains(pizzaSalami) && allProducts.contains(pizzaVegitarian) &&
                        allProducts.contains(pizzaRustica));

        assertTrue("first product should be fungi in sorted order", allProducts.get(0).equals(pizzaFungi));

        assertTrue("last product should be vegiatrian in sorted order", allProducts.get(allProducts.size()-1).equals(pizzaVegitarian));
    }

    @Test
    public void testUsers() {

        List<User> allUsers = userRepository.findAll().as(List.class);
        assertEquals("there should be three users in the user repository", 3, allUsers.size());

        Set<Product> clickedProducts = allUsers.get(0).getAllClickedProducts();
        assertEquals("Monika Jordan should have three clicked products", 3, clickedProducts.size());
        assertTrue("The two products Monika Jordan clicked on should be pizza margarita and pizza fungi",
                clickedProducts.contains(pizzaMargarita) && clickedProducts.contains(pizzaFungi) && clickedProducts.contains(pizzaSalami));
    }

    @Test
    public void testfindOtherUsersAlsoViewedProductsCypherQuery() {

        List<Product> alsoViewedProducts = productRepository.findOtherUsersAlsoViewedProducts(pizzaMargarita.getProductId());
        assertEquals("there should be two products recommended", 2, alsoViewedProducts.size());
        assertTrue("using this cypher query should return a list with also viewed products",
                alsoViewedProducts.contains(pizzaFungi) && alsoViewedProducts.contains(pizzaVegitarian));
    }
    @Test
    public void testfindOtherUsersAlsoViewedProductsWithoutAlreadyViewedCypherQuery() {

        List<Product> alsoViewedProductsWithoutAlreadyViewed = productRepository.findOtherUsersAlsoViewedProductsWithoutAlreadyViewed(pizzaMargarita.getProductId(), miller.getUserId());
        assertEquals("there should be one product recommended", 1, alsoViewedProductsWithoutAlreadyViewed.size());
        assertTrue("using this cypher query should return a list with also viewed products without the ones Miller already viewed",
                alsoViewedProductsWithoutAlreadyViewed.contains(pizzaVegitarian));

    }

    private Product createProduct(String id, String name) {
        return productRepository.save(new Product(id, name));
    }

    private User createUser(String id, String name) {
        return userRepository.save(new User(id, name));
    }

    private void userClickedProduct(User user, Product product) {

        user.addClickedProduct(product);

        userRepository.save(user);
        productRepository.save(product);
    }

    @After
    public void cleanDB() {
        Neo4jHelper.cleanDb(graphDatabaseService);
    }
}
