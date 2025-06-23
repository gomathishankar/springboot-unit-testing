package org.gslearn.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.UUID;

@DataJpaTest
public class UsersRepositoryTest {

    private static UserEntity user;

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    UsersRepository usersRepository;

    @BeforeAll
    public static void setUp() {
        user = new UserEntity();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("test@test.com");
        user.setUserId(UUID.randomUUID().toString());
        user.setEncryptedPassword("Test12345678");
    }

    @Test
    @DisplayName("Find by email")
    public void testFindByEmailWhenValidUserEmailProvidedShouldReturnUserDetails() {
        entityManager.persistAndFlush(user);
        //Act
        UserEntity userEntity = usersRepository.findByEmail(user.getEmail());
        //Assert
        Assertions.assertNotNull(userEntity);
        Assertions.assertEquals(user.getEmail(), userEntity.getEmail(), "Email does not match");
    }

    @Test
    @DisplayName("Find by email not in database")
    public void testFindByEmailWhenInValidUserEmailProvidedShouldReturnError() {
        entityManager.persistAndFlush(user);
        //Act
        UserEntity userEntity =usersRepository.findByEmail("test1@test.com");
        // Assert
        Assertions.assertNull(userEntity, "User Entity does not contains user details");

    }

    @Test
    @DisplayName("Find by user first and lastname")
    public void testFindByFirstAndLastnameWhenValidUserFirstAndLastnameProvidedShouldReturnUserDetails() {
        //Arrange
        entityManager.persistAndFlush(user);
        //Act
        UserEntity userEntity = usersRepository.findByFirstNameAndLastName(user.getFirstName(),user.getLastName());
        //Assert
        Assertions.assertNotNull(userEntity);
        Assertions.assertEquals(user.getFirstName(), userEntity.getFirstName(), "FirstName does not match");
        Assertions.assertEquals(user.getLastName(), userEntity.getLastName(), "LastName does not match");
    }

    @Test
    @DisplayName("Find by user email domain")
    public void testFindUsersWithEmailDomainEndingWithWhenValidUsersEmailDomainProvidedShouldReturnUserDetails() {
        UserEntity secondUserEntity = new UserEntity();
        secondUserEntity.setFirstName("GS");
        secondUserEntity.setLastName("Doe");
        secondUserEntity.setEmail("test@gmail.com");
        secondUserEntity.setUserId(UUID.randomUUID().toString());
        secondUserEntity.setEncryptedPassword("Test12121212");
        entityManager.persistAndFlush(secondUserEntity);
        //Arrange
        entityManager.persistAndFlush(user);
        //Act
        List<UserEntity> listOfUsers = usersRepository.findUsersWithEmailDomainEndingWith("@gmail.com");
        //Assert
        Assertions.assertNotNull(listOfUsers);
        Assertions.assertEquals(1, listOfUsers.size(), "Users list does not contain expected users");
        Assertions.assertEquals(secondUserEntity.getEmail(),listOfUsers.getFirst().getEmail(), "Emails does not match");

    }
}
