package com.example.mock2.user;

import com.example.mock2.common.dto.request.PaginationQuery;
import com.example.mock2.common.dto.response.Response;
import com.example.mock2.user.dto.request.UserRequest;
import com.example.mock2.user.dto.response.UserResponse;
import com.example.mock2.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Response<UserResponse> create(
            @Valid
            @RequestBody UserRequest userRequest) {
        User user = userService.create(userRequest);
        return Response.success(UserResponse.of(user));
    }

    @GetMapping("/{id}")
    public Response<UserResponse> get(@PathVariable Integer id) {
        return Response.success(UserResponse.of(userService.getById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Response<UserResponse> edit(@PathVariable Integer id,
                                 @Valid @RequestBody UserRequest userRequest) {
        return Response.success(UserResponse.of(userService.edit(userRequest, id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Response<UserResponse> delete(@PathVariable Integer id) {
        User removed = userService.remove(id);
        return Response.success(UserResponse.of(removed));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Response<List<UserResponse>> getAll(@RequestParam(value = "page", required = false) Integer page,
                                               @RequestParam(value = "size", required = false) Integer size) {
        PaginationQuery paginationQuery = new PaginationQuery();
        if (page != null && size != null) {
            paginationQuery.setPageRequest(PageRequest.of(page, size));
        }
        return Response.paging(userService.getAll(paginationQuery), UserResponse::of);
    }

}
