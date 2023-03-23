package com.example.mock2.gallery;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.mock2.gallery.model.Gallery;
import com.example.mock2.product.ProductRepository;
import com.example.mock2.product.model.Product;
import com.example.mock2.user.UserRepository;
import com.example.mock2.user.model.User;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GalleryService {
    @Autowired
    private final GalleryRepository galleryRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ProductRepository productRepository;
    private final Path root = Paths.get("src/main/resources/uploads");

    // Save avatar for user
    public Gallery saveFileForUser(MultipartFile file, Integer id) throws Exception {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new Exception("Not found user"));
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));

            String path = slug(this.root.resolve(file.getOriginalFilename()).toString());

            Gallery gallery = new Gallery();
            gallery.setUser(user);
            gallery.setCreatedBy(user.getName());
            gallery.setModifiedBy(user.getName());
            gallery.setPath(path);

            galleryRepository.save(gallery);
            return gallery;
        } catch (IOException e) {
            throw new IOException("Duplicate file name");
        }
    }

    // Save product images
    public Gallery saveFileForProduct(MultipartFile file, Integer id) throws Exception {
        try {

            Product product = productRepository.findById(id).orElseThrow(() -> new Exception("Not found product"));
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
            String path = slug(this.root.resolve(file.getOriginalFilename()).toString());

            Gallery gallery = new Gallery();
            gallery.setProduct(product);
            gallery.setCreatedBy(product.getName());
            gallery.setModifiedBy(product.getName());
            gallery.setPath(path);

            galleryRepository.save(gallery);
            return gallery;
        } catch (IOException e) {
            throw new IOException("Duplicate file name");
        }
    }

    // remove sign in Vietnamese
    private String deAccent(String str) {
        String normalization = Normalizer.normalize(str, Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalization).replaceAll("");
    }

    // turn space into this character "-"
    private String slug(String str) {
        return deAccent(str.replaceAll("\s", "-"));
    }
}
