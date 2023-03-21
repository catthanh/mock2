package com.example.mock2.user;

import com.example.mock2.common.dto.request.PaginationQuery;
import com.example.mock2.common.dto.response.Response;
import com.example.mock2.user.dto.request.UserRequest;
import com.example.mock2.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    public Response createEmployee(
            @Valid
            @RequestBody UserRequest userRequest) {
        User createdEmployee = userService.create(userRequest);
        return Response.success(createdEmployee);
    }

    @GetMapping("/{id}")
    public Response get(@PathVariable Integer id) {
        return Response.success(userService.getById(id));
    }

    @PutMapping("/{id}")
    public Response edit(@PathVariable Integer id,
                                 @Valid @RequestBody UserRequest userRequest) {
        return Response.success(userService.edit(userRequest, id));
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable Integer id) {
        User removed = userService.remove(id);
        return Response.success(removed);
    }

    @GetMapping
    public Response getAll(@RequestParam(value = "page", required = false) Integer page,
                           @RequestParam(value = "size", required = false) Integer size) {
        PaginationQuery paginationQuery = new PaginationQuery();
        if (page != null && size != null) {
            paginationQuery.setPageRequest(PageRequest.of(page, size));
        }
        return Response.paging(userService.getAll(paginationQuery));
    }

}
