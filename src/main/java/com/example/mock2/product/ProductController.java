package com.example.mock2.product;

import com.example.mock2.common.dto.response.Response;
import com.example.mock2.product.dto.request.ProductRequest;
import com.example.mock2.product.model.Product;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@Validated
@AllArgsConstructor
public class ProductController {
    public final ProductService productService;
    @PostMapping
    public Response addProduct(
            @Valid
            @RequestBody ProductRequest productRequest
            ){
        Product product = productService.addNewProduct(productRequest);
        return Response.success(product);
    }

    @GetMapping("/{id}")
    public Response getProductById(@PathVariable Integer id){
        return Response.success(productService.getById(id));
    }

    @GetMapping
    public Response getAllProduct(){
        return Response.success(productService.getAllProduct());
    }

    @PutMapping("/{id}")
    public Response updateProductById(@PathVariable Integer id, @RequestBody ProductRequest productRequest){
        System.out.println("hehehehehehe");
        return Response.success(productService.editProduct(productRequest, id));
    }

    @DeleteMapping("/{id}")
    public Response deleteProductById(@PathVariable Integer id){
        return Response.success(productService.deleteProduct(id));
    }
}
