package com.example.mock2.gallery;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.mock2.common.exception.CommonLogicException;
import com.example.mock2.common.exception.NotFoundException;
import com.example.mock2.gallery.dto.response.FileUrlResponse;
import com.example.mock2.gallery.model.Gallery;
import com.example.mock2.product.ProductRepository;
import com.example.mock2.product.model.Product;
import com.example.mock2.security.config.AuthenticationPrinciple;
import com.example.mock2.user.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class GalleryService {
    @Autowired
    private final GalleryRepository galleryRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ProductRepository productRepository;

    private final Path userRoot = Paths.get("src/main/resources/uploads/users");
    private final Path productRoot = Paths.get("src/main/resources/uploads/products");

    @PostConstruct
    public void init() {
        // create new folders
        try {
            Files.createDirectories(userRoot);
            Files.createDirectories(productRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save avatar for user
    public Gallery saveFileForUser(MultipartFile file) throws Exception {
        // get user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticationPrinciple user = (AuthenticationPrinciple) authentication.getPrincipal();
        try {
            if (!file.getContentType().startsWith("image/")) {
                log.error("Error Adding avatar: {} - by: {}", "Add wrong type", user.getUsername());
                throw new NotFoundException("Can only be image");
            }
            // copy file to destination
            String savedName = slug(file.getOriginalFilename());
            Files.copy(file.getInputStream(), this.productRoot.resolve(savedName));

            String path = this.productRoot.resolve(savedName).toString();

            // add or update avatar
            Gallery gallery = galleryRepository.findByUserId(user.getId())
                    .orElse(new Gallery(path, savedName, user.getUsername(), user.getUsername()));
            gallery.setPath(path);
            gallery.setUser(userRepository.getReferenceById(user.getId()));

            // logger
            log.trace("Upload avatar for user - Created by {} - File name: {}", user.getUsername(),
                    savedName);
            galleryRepository.save(gallery);
            return gallery;
        } catch (IOException e) {
            log.error("Error adding avatar: {} - By {}", "Duplicate image name", user.getUsername());
            throw new CommonLogicException("Duplicate image name");
        }
    }

    // Save product images
    public Gallery saveFileForProduct(MultipartFile file, Integer id) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticationPrinciple user = (AuthenticationPrinciple) authentication.getPrincipal();
        try {
            // check product existed
            Product product = productRepository.findById(id).orElseThrow(() -> {
                log.error("Error fail to get product - By {}", user.getUsername());
                return new Exception("Not found product");
            });

            // check the number of images in each product, max 10
            int galleries = galleryRepository.countByProductId(id);
            if (galleries == 10) {
                throw new CommonLogicException("Can not have more than 10 picture");
            }

            String savedName = slug(file.getOriginalFilename());
            Files.copy(file.getInputStream(), this.productRoot.resolve(savedName));

            String path = this.productRoot.resolve(savedName).toString();

            Gallery gallery = new Gallery(path, savedName, user.getUsername(), user.getUsername());
            gallery.setProduct(product);

            log.trace("Upload File for Product - File name:  {} - Created by {}", savedName, user.getUsername());
            galleryRepository.save(gallery);
            return gallery;
        } catch (IOException e) {
            log.error("Error Add file for product: duplicate name of file - Create by {}", user.getUsername());
            throw new CommonLogicException("Duplicate file name");
        }
    }

    // load the latest image
    public FileUrlResponse loadImageForProduct(Integer productId) {
        Gallery file = galleryRepository.getPath(productId)
                .orElseThrow(() -> new NotFoundException("not found product"));
        log.trace("Load File for product: {}. By: {}", file.getName(), getCurrentUser().getUsername());
        return getFilePath(file);
    }

    public FileUrlResponse loadImageForUser() {
        Gallery file = galleryRepository.findByUserId(getCurrentUser().getId())
                .orElseThrow(() -> new NotFoundException("Not found image"));
        return getFilePath(file);
    }

    // load all files of 1 product
    public List<FileUrlResponse> loadAllForProduct(Integer productId) {
        boolean status = galleryRepository.findByProductId(productId).isEmpty();
        if (!status)
            return galleryRepository.findByProductId(productId).stream().map((g) -> getFilePath(g))
                    .collect(Collectors.toList());
        throw new NotFoundException("No Product image");
    }

    // delete files of product
    public String deleteFileForProduct(String fileName, Integer productId) {
        log.trace("Delete File: {} - By: {}", fileName, getCurrentUser().getUsername());
        Gallery gallery = galleryRepository.findByNameAndProductId(fileName, productId)
                .orElseThrow(() -> new NotFoundException("not found image"));
        try {
            Files.deleteIfExists(Paths.get(gallery.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        galleryRepository.delete(gallery);

        return "Delete successfully";
    }

    // remove sign in Vietnamese
    private String deAccent(String str) {
        String normalization = Normalizer.normalize(str, Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalization).replaceAll("");
    }

    // turn space into this character "-"
    private String slug(String str) {
        SimpleDateFormat dfm = new SimpleDateFormat("ddMMHHmmss");
        String date = dfm.format(new Date());
        String slug = deAccent(str.replaceAll("\s", "-"));
        String saveName = date + "-" + slug;
        return saveName;
    }

    private FileUrlResponse getFilePath(Gallery file) {
        Path path = Paths.get(file.getPath());
        return new FileUrlResponse(file.getId(), ("http://localhost:8080/files/" + path.getFileName()));
    }

    private AuthenticationPrinciple getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticationPrinciple user = (AuthenticationPrinciple) authentication.getPrincipal();
        return user;
    }

}
