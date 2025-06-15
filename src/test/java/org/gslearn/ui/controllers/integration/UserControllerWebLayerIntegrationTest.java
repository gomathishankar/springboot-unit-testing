package org.gslearn.ui.controllers.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
properties = {"server.port=8081"})
public class UserControllerWebLayerIntegrationTest {


    @Test
    public void testUserController() {

    }
}
