package com.example.mock2.gallery;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.mock2.common.dto.response.Response;
import com.example.mock2.gallery.dto.response.FileUrlResponse;
import com.example.mock2.gallery.model.Gallery;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class GalleryController {
    @Autowired
    private final GalleryService galleryService;

    @PostMapping("/gallery/user")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public Response<Gallery> saveFileForUser(@RequestParam("file") MultipartFile file) throws Exception {
        Gallery gallery1 = galleryService.saveFileForUser(file);
        return Response.success(gallery1);
    }

    @PostMapping("/gallery/product/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Response<Gallery> saveFileForProduct(
            @PathVariable(name = "id") Integer productId, @RequestParam("file") MultipartFile file) throws Exception {
        Gallery gallery = galleryService.saveFileForProduct(file, productId);
        return Response.success(gallery);
    }

    @GetMapping("/gallery/product/{id}")
    public Response<List<FileUrlResponse>> loadImageForProduct(@PathVariable(name = "id") Integer productId) {
        return Response.success(galleryService.loadAllForProduct(productId));
    }

    @GetMapping("/gallery/user")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public Response<FileUrlResponse> loadImageForUser() {
        return Response.success(galleryService.loadImageForUser());
    }

    @DeleteMapping("gallery/product/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Response<String> deleteFileForProduct(@RequestParam String fileName, @RequestParam Integer productId) {
        String message = galleryService.deleteFileForProduct(fileName, productId);
        return Response.success(message);
    }

}
