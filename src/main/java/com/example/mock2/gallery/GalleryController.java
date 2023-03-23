package com.example.mock2.gallery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.mock2.common.dto.response.Response;
import com.example.mock2.gallery.model.Gallery;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class GalleryController {
    @Autowired
    public final GalleryService galleryService;

    @PostMapping("/user/gallery")
    public Response saveFileForUser(
            @RequestParam Integer id, @RequestParam("file") MultipartFile file) throws Exception {
        Gallery gallery = galleryService.saveFileForUser(file, id);
        return Response.success(gallery);
    }

    @PostMapping("/product/gallery")
    public Response saveFileForProduct(
            @RequestParam Integer id, @RequestParam("file") MultipartFile file) throws Exception {
        Gallery gallery = galleryService.saveFileForProduct(file, id);
        return Response.success(gallery);
    }

}
