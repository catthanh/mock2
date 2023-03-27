package com.example.mock2.review;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.mock2.common.exception.BadRequestException;
import com.example.mock2.common.exception.NotFoundException;
import com.example.mock2.product.ProductRepository;
import com.example.mock2.product.model.Product;
import com.example.mock2.review.dto.response.ReviewResponse;
import com.example.mock2.review.model.ProductReview;
import com.example.mock2.security.config.AuthenticationPrinciple;
import com.example.mock2.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductReviewService {
  private final ProductReviewRepository repository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;

  public ReviewResponse getReview(Integer productId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    AuthenticationPrinciple user = (AuthenticationPrinciple) authentication.getPrincipal();

    ProductReview review = repository.findByUserIdAndProductId(user.getId(), productId).orElse(null);
    if (review == null) {
      return ReviewResponse.builder().star(Math.round(5 * 10) / 10).createdDate(new Date())
          .build();
    }
    return ReviewResponse.builder().star(Math.round(review.getStar() * 10) / 10).createdDate(review.getCreationDate())
        .build();
  }

  public ReviewResponse addReview(Integer productId, double star) {
    // check whether bought this product or not
    // if (!isBoughtProduct(productId, getCurrentUser().getId())) {
    // throw new BadRequestException("You have to buy this product to review");
    // }

    ProductReview review = new ProductReview();
    review.setStar(star);
    review.setUser(userRepository.getReferenceById(getCurrentUser().getId()));
    review.setProduct(productRepository.getReferenceById(productId));
    review.setCreatedBy(getCurrentUser().getUsername());
    review.setModifiedBy(getCurrentUser().getUsername());
    repository.save(review);

    Product product = productRepository.findById(productId).get();
    product.setReviewScore();
    productRepository.save(product);
    return ReviewResponse.builder().star(star).createdDate(new Date()).build();
  }

  public ReviewResponse updateReview(Integer productId, double star) {
    // check product existed or not
    checkProductExisted(productId);

    ProductReview existProductReview = repository.findByUserIdAndProductId(getCurrentUser().getId(), productId)
        .orElseThrow(() -> new NotFoundException("not found review"));

    existProductReview.setLastModifiedDate(new Date());
    existProductReview.setModifiedBy(getCurrentUser().getUsername());
    existProductReview.setStar(star);
    repository.save(existProductReview);
    Product product = productRepository.findById(productId).get();
    product.setReviewScore();
    productRepository.save(product);
    return ReviewResponse.builder().star(star).createdDate(new Date()).build();
  }

  public void deleteReview(Integer productId) {
    // check whether product exists
    checkProductExisted(productId);

    ProductReview existProductReview = repository.findByUserIdAndProductId(getCurrentUser().getId(), productId)
        .orElseThrow(() -> new NotFoundException("not found review"));
    repository.delete(existProductReview);
  }

  private void checkProductExisted(Integer productId) {
    productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Not found product"));
  }

  private boolean isBoughtProduct(Integer productId, Integer userId) {
    return repository.findByUserIdAndProductId(userId, productId).isPresent();
  }

  private AuthenticationPrinciple getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    AuthenticationPrinciple user = (AuthenticationPrinciple) authentication.getPrincipal();
    return user;
  }
}
