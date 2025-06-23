package org.gslearn.ui.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Disabled
public class UserControllerTestContainerTest {

    @Container
    //@ServiceConnection
    private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.4.0")
            .withUsername("gomathi")
            .withPassword("gomathi");

    static {
        mySQLContainer.start();
    }
//            .withDatabaseName("test_database")
//            .withUsername("gomathi")
//            .withPassword("gomathi");
//
//    @DynamicPropertySource // To dynamically override env values.
//    private static void overrideProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", mySQLContainer::getUsername);
//        registry.add("spring.datasource.password", mySQLContainer::getPassword);
//    }

    @Test
    @DisplayName("Test if test container connect works")
    public void testUserController() {
        Assertions.assertTrue(mySQLContainer.isCreated());
        Assertions.assertTrue(mySQLContainer.isRunning(), "MySQL is not running");
    }
}
