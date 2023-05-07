package com.app.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.model.Course;

@Transactional
@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>{

	
	@Query("select c from Course c where c.year = :year")
	List<Course> findAllByYear(@Param("year")int i);

}
