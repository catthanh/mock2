package com.example.mock2.review;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mock2.common.dto.response.Response;
import com.example.mock2.review.dto.request.ReviewRequest;
import com.example.mock2.review.dto.response.ReviewResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class ProductReviewController {
  private final ProductReviewService service;

  @GetMapping("/review")
  @PreAuthorize("hasAuthority('USER')")
  public Response<ReviewResponse> getReview(@RequestParam Integer productId) {
    ReviewResponse review = service.getReview(productId);
    return Response.success(review);
  }

  @PostMapping("/review")
  @PreAuthorize("hasAuthority('ADMIN')")
  public Response<ReviewResponse> addReview(@RequestBody ReviewRequest reviewRequest, @RequestParam Integer productId) {
    ReviewResponse review = service.addReview(productId, reviewRequest.getStar());
    return Response.success(review);
  }

  @PutMapping("/review/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public Response<ReviewResponse> updateReview(@RequestBody ReviewRequest reviewRequest,
      @PathVariable(name = "id") Integer productId) {
    ReviewResponse review = service.updateReview(productId, reviewRequest.getStar());
    return Response.success(review);
  }

  @DeleteMapping("/review/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public Response<Void> deleteReview(@PathVariable(name = "id") Integer productId) {
    service.deleteReview(productId);
    return Response.success(null);
  }
}
