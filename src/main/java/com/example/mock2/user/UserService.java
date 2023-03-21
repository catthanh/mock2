package com.example.mock2.user;

import com.example.mock2.common.dto.request.PaginationQuery;
import com.example.mock2.user.dto.request.UserRequest;
import com.example.mock2.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public List<GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username).get();
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(getAuthorities(user))
                .build();
    }

    public User create(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new RuntimeException("username already exists");
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("email already exists");
        }
        if (userRepository.existsByPhone(userRequest.getPhone())) {
            throw new RuntimeException("phone already exists");
        }
        User user = new User()
                .setUsername(userRequest.getUsername())
                .setPassword(userRequest.getPassword())
                .setName(userRequest.getName())
                .setEmail(userRequest.getEmail())
                .setPhone(userRequest.getPhone())
                .setAddress(userRequest.getAddress())
                .setSex(userRequest.getSex())
                .setBirthdate(userRequest.getBirthdate());
        return userRepository.save(user);
    }

    public User getById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
    }

    public User edit(UserRequest userRequest, Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("user not found");
        }
        if (userRepository.existsByUsernameAndIdIsNot(userRequest.getUsername(), userOptional.get().getId())) {
            throw new RuntimeException("username already exists");
        }
        if (userRepository.existsByEmailAndIdIsNot(userRequest.getEmail(), userOptional.get().getId())) {
            throw new RuntimeException("email already exists");
        }
        if (userRepository.existsByPhoneAndIdIsNot(userRequest.getPhone(), userOptional.get().getId())) {
            throw new RuntimeException("phone already exists");
        }
        User user = userOptional.get()
                .setUsername(userRequest.getUsername())
                .setPassword(userRequest.getPassword())
                .setName(userRequest.getName())
                .setEmail(userRequest.getEmail())
                .setPhone(userRequest.getPhone())
                .setAddress(userRequest.getAddress())
                .setSex(userRequest.getSex())
                .setBirthdate(userRequest.getBirthdate());

        return userRepository.save(user);
    }

    public User remove(Integer id) {
        User employee = userRepository.findById(id).orElseThrow(() ->new RuntimeException("user not found"));
        userRepository.delete(employee);
        return employee;
    }

    public Page<User> getAll(PaginationQuery paginationQuery) {
        Pageable pageable = paginationQuery.getPageRequest();

        return userRepository.findAll(pageable);
    }
}
