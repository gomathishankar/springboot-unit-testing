package org.gslearn.io;

import jakarta.persistence.PersistenceException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class UserEntityTest {

    @Autowired
    public TestEntityManager entityManager;

    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("test@test.com");
        userEntity.setEncryptedPassword("12345678");
    }

    @Test
    @DisplayName("User Entity should be persisted")
    public void testUserEntityWhenValidUserDetailsShouldReturnPersistedUserEntityDetails() {
        //Arrange

        //Act
        UserEntity savedUserEntity = entityManager.persistAndFlush(userEntity);
        //Assert
        Assertions.assertTrue(savedUserEntity.getId() > 0, "User ID is not generated");
        Assertions.assertEquals(savedUserEntity.getFirstName(), userEntity.getFirstName(), "First Name not generated");
    }

    @Test
    @DisplayName("First name should be less than 50 character")
    public void testUserEntityWhenFirstNameIsMoreThan50CharacterShouldReturnPersistenceException() {
        // Arrange
        userEntity.setFirstName(RandomStringUtils.random(52, true, false));

        //Act & Assert
        Assertions.assertThrows(PersistenceException.class,
                () -> entityManager.persistAndFlush(userEntity), "First name should be less than 50 character");

    }

    @Test
    @DisplayName("User Id should be unique")
    public void testUserEntityWhenSameUserIdProvidedShouldThrowException() {
        //Arrange
        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setFirstName("John");
        newUserEntity.setLastName("Doe");
        newUserEntity.setEmail("test@test.com");
        newUserEntity.setEncryptedPassword("12345678");
        newUserEntity.setUserId("TestUser");

        //Act & Assert
        UserEntity savedUserEntity = entityManager.persistAndFlush(newUserEntity);
        Assertions.assertTrue(savedUserEntity.getId() > 0, "User ID is not generated");

        userEntity.setUserId("TestUser");
        Assertions.assertThrows(PersistenceException.class,
                () -> entityManager.persistAndFlush(userEntity), "User ID should be unique");
    }
}
