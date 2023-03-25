package com.example.mock2.product;

import com.example.mock2.common.dto.request.PaginationQuery;
import com.example.mock2.common.dto.response.Response;
import com.example.mock2.product.dto.request.ProductRequest;
import com.example.mock2.product.dto.response.ProductResponse;
import com.example.mock2.product.model.Product;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@Validated
@AllArgsConstructor
public class ProductController {
    public final ProductService productService;
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Response<ProductResponse> addProduct(
            @Valid
            @RequestBody ProductRequest productRequest
            ){
        Product product = productService.addNewProduct(productRequest);
        return Response.success(ProductResponse.of(product));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Response<ProductResponse> getProductById(@PathVariable Integer id){
        return Response.success(ProductResponse.of(productService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Response<List<ProductResponse>> getAllProduct(@RequestParam(value = "page", required = false) Integer page,
                                                        @RequestParam(value = "size", required = false) Integer size){
        PaginationQuery paginationQuery = new PaginationQuery();
        if (page != null && size != null) {
            paginationQuery.setPageRequest(PageRequest.of(page, size));
        }
        return Response.paging(productService.getAllProduct(paginationQuery), ProductResponse::of);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Response<ProductResponse> updateProductById(@PathVariable Integer id, @RequestBody ProductRequest productRequest){
        System.out.println("hehehehehehe");
        return Response.success(ProductResponse.of(productService.editProduct(productRequest, id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Response<ProductResponse> deleteProductById(@PathVariable Integer id){
        return Response.success(ProductResponse.of(productService.deleteProduct(id)));
    }
}
