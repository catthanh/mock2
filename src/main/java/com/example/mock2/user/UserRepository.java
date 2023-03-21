package com.example.mock2.user;

import com.example.mock2.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByUsernameAndIdIsNot(String username, Integer id);

    boolean existsByEmailAndIdIsNot(String email, Integer id);

    boolean existsByPhoneAndIdIsNot(String phone, Integer id);
}
