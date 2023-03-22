package com.example.mock2.user;

import com.example.mock2.cart.model.Cart;
import com.example.mock2.common.dto.request.PaginationQuery;
import com.example.mock2.common.exception.CommonLogicException;
import com.example.mock2.common.exception.NotFoundException;
import com.example.mock2.security.config.AuthenticationPrinciple;
import com.example.mock2.user.dto.request.UserRequest;
import com.example.mock2.user.model.Role;
import com.example.mock2.user.model.RoleEnum;
import com.example.mock2.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username).get();
        return new AuthenticationPrinciple(user.getUsername(), user.getPassword(), getAuthorities(user)).setId(user.getId());
    }

    public User create(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new CommonLogicException("username already exists");
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new CommonLogicException("email already exists");
        }
        if (userRepository.existsByPhone(userRequest.getPhone())) {
            throw new CommonLogicException("phone already exists");
        }
        User user = new User()
                .setUsername(userRequest.getUsername())
                .setPassword(passwordEncoder.encode(userRequest.getPassword()))
                .setName(userRequest.getName())
                .setEmail(userRequest.getEmail())
                .setPhone(userRequest.getPhone())
                .setAddress(userRequest.getAddress())
                .setSex(userRequest.getSex())
                .setBirthdate(userRequest.getBirthdate())
                .setRoles(userRequest.getRoles().stream().map(role -> new Role().setName(role)).collect(Collectors.toSet()))
                .setCart(new Cart());

        return userRepository.save(user);
    }

    public User getById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("user not found"));
    }

    public User edit(UserRequest userRequest, Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("user not found");
        }
        if (userRepository.existsByUsernameAndIdIsNot(userRequest.getUsername(), userOptional.get().getId())) {
            throw new CommonLogicException("username already exists");
        }
        if (userRepository.existsByEmailAndIdIsNot(userRequest.getEmail(), userOptional.get().getId())) {
            throw new CommonLogicException("email already exists");
        }
        if (userRepository.existsByPhoneAndIdIsNot(userRequest.getPhone(), userOptional.get().getId())) {
            throw new CommonLogicException("phone already exists");
        }
        User user = userOptional.get()
                .setUsername(userRequest.getUsername())
                .setName(userRequest.getName())
                .setEmail(userRequest.getEmail())
                .setPhone(userRequest.getPhone())
                .setAddress(userRequest.getAddress())
                .setSex(userRequest.getSex())
                .setBirthdate(userRequest.getBirthdate());

        return userRepository.save(user);
    }

    public User remove(Integer id) {
        User employee = userRepository.findById(id).orElseThrow(() ->new NotFoundException("user not found"));
        userRepository.delete(employee);
        return employee;
    }

    public Page<User> getAll(PaginationQuery paginationQuery) {
        Pageable pageable = paginationQuery.getPageRequest();

        return userRepository.findAll(pageable);
    }
}
