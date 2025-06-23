package org.gslearn.io;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByFirstNameAndLastName(String firstName, String lastName);
    @Query("select user from UserEntity user where user.email like %:emailDomain")
    List<UserEntity> findUsersWithEmailDomainEndingWith(@Param("emailDomain") String emailDomain);
}
