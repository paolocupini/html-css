package com.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.model.PostImage;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Integer>{

	Optional<PostImage> getByFilename(String originalFilename);

}
