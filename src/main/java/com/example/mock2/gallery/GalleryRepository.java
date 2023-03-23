package com.example.mock2.gallery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mock2.gallery.model.Gallery;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
}
