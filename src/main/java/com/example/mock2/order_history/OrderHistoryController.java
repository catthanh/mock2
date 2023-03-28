package com.example.mock2.order_history;

import com.example.mock2.common.dto.request.PaginationQuery;
import com.example.mock2.common.dto.response.Response;
import com.example.mock2.order_history.dto.response.OrderHistoryResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orderhistory")
@Validated
@AllArgsConstructor
public class OrderHistoryController {
    private final OrderHistoryService orderHistoryService;
    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public Response<OrderHistoryResponse> createInvoice(){
        return Response.success(OrderHistoryResponse.of(orderHistoryService.createInvoice()));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public Response<List<OrderHistoryResponse>> getInvoice(@RequestParam(value = "page", required = false) Integer page,
                                                     @RequestParam(value = "size", required = false) Integer size){
        PaginationQuery paginationQuery = new PaginationQuery();
        if (page != null && size != null) {
            paginationQuery.setPageRequest(PageRequest.of(page, size));
        }
        return Response.paging(orderHistoryService.getOrderHistoryForUser(paginationQuery), OrderHistoryResponse::of);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Response<List<OrderHistoryResponse>> getAllOrderHistory(@RequestParam(value = "page", required = false) Integer page,
                                                              @RequestParam(value = "size", required = false) Integer size){
        PaginationQuery paginationQuery = new PaginationQuery();
        if (page != null && size != null) {
            paginationQuery.setPageRequest(PageRequest.of(page, size));
        }
        return Response.paging(orderHistoryService.getAllOrderHistoryForAdmin(paginationQuery), OrderHistoryResponse::of);
    }
}
