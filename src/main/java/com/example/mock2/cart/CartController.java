package com.example.mock2.cart;

import com.example.mock2.cart.dto.request.AddProductRequest;
import com.example.mock2.cart.dto.request.RemoveProductRequest;
import com.example.mock2.cart.dto.request.UpdateProductQuantityRequest;
import com.example.mock2.cart.dto.response.CartProductResponse;
import com.example.mock2.common.dto.request.PaginationQuery;
import com.example.mock2.common.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Validated
public class CartController {
    private final CartService cartService;

    @PostMapping("/add-product")
    public Response<CartProductResponse> addProduct(@RequestBody AddProductRequest addProductRequest) {
        return Response.success(cartService.addProduct(addProductRequest), CartProductResponse::of);
    }

    @DeleteMapping("/remove-product")
    public Response<Void> removeProduct(@RequestBody RemoveProductRequest removeProductRequest) {
        cartService.removeProduct(removeProductRequest);
        return Response.success(null);
    }

    @PutMapping("/update-product-quantity")
    public Response<CartProductResponse> updateProductQuantity(@RequestBody UpdateProductQuantityRequest updateProductQuantityRequest) {
        return Response.success(cartService.updateProductQuantity(updateProductQuantityRequest), CartProductResponse::of);
    }

    @GetMapping()
    public Response<List<CartProductResponse>> getItems(PaginationQuery paginationQuery) {
        return Response.paging(cartService.getCartProducts(paginationQuery), CartProductResponse::of);
    }


}
