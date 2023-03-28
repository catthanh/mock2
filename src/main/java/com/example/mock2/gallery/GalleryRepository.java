package com.example.mock2.gallery;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.mock2.gallery.model.Gallery;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {

  Optional<Gallery> findByUserId(Integer id);

  @Query(value = "SELECT * FROM gallery where product_id = ?1 order by creation_date desc limit 1;", nativeQuery = true)
  Optional<Gallery> getPath(Integer productId);
  @Query(value = "SELECT path FROM gallery where user_id = ?1 order by creation_date desc limit 1;", nativeQuery = true)
  String findPathByUserId(Integer userId);

  Optional<Gallery> findByNameAndProductId(String fileName, Integer productId);

  List<Gallery> findByProductId(Integer productId);

  Integer countByProductId(Integer id);
}
