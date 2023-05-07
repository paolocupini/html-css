package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{

	@Query("select p from Post p order by p.published_at desc")
	List<Post> findAllSortedByPublished_at();

	@Query("select p from Post p order by p.event_date desc")
	List<Post> findAllSortedByEvent_date();
}
