package com.app.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.ResponseCourseDto;
import com.app.mapper.CourseMapper;
import com.app.model.Course;
import com.app.model.CourseFile;
import com.app.repository.CourseRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseService {

	@Autowired
	CourseRepository repo;

	@Autowired
	CourseMapper mapper;

	public Boolean createCourse(Course course, MultipartFile image) {
		log.debug("Starting Course creation...");

		if (course == null) {
			log.debug("null value provided: FAILURE");
			return false;
		}
		Integer id = course.getClass_code();
		
		log.info("Inspecting database...");
		
		Optional<Course> courseOpt = repo.findById(id);

		if (courseOpt.isPresent()) {
			log.info("Course already present in the db: FAILURE");
			return false;
		}
		log.info("Course not found, course is new, saving into the db: OK");
		try {
			course.setImage(image.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repo.save(course);
		return true;
	}

	public Optional<ResponseCourseDto> getOneCourse(Integer course_code) {

		log.info("Starting Course "+String.valueOf(course_code)+" retrieval...");
		
		log.info("Inspecting database...");
		Optional<Course> courseOpt = repo.findById(course_code);

		if (courseOpt.isEmpty()) {

			log.info("Course not present in the db: FAILURE");
			return Optional.empty();
		}

		Course course = courseOpt.get();

		log.info("Course found, retrieving course from db: OK");
		
		return Optional.of(mapper.toResponseDto(course));

	}

	public List<List<ResponseCourseDto>> getAllCourses() {

		log.info("Starting to retrieve all courses...");
		List<List<ResponseCourseDto>> res = new ArrayList<>(3);
		
		log.info("First Year courses...");
		
		
		List<ResponseCourseDto> first_year = mapper.toListOfResponseDto(repo.findAllByYear(1) );
		
		log.info("Second Year courses...");
		
		List<ResponseCourseDto> second_year = mapper.toListOfResponseDto(repo.findAllByYear(2) );

		log.info("Third Year courses...");
		List<ResponseCourseDto> third_year = mapper.toListOfResponseDto(repo.findAllByYear(3) );
		res.add(first_year);
		res.add(second_year);
		res.add(third_year);
		
		return res;
	}

	public Boolean storeFile(MultipartFile file, Integer course_code) {

		if(repo.existsById(course_code) == false) return false;
		
		CourseFile course_file = mapper.fromMultipartFileToCourseFile(file);
		
		Course course = repo.findById(course_code).get();
		
		List<CourseFile> files =  course.getFiles();
		
		if(files.contains(course_file) == true ) return false;
		
		course.getFiles().add(course_file);
		
		repo.save(course);
		
		
		
		return true;
	}

	public CourseFile retrieveFile(String filename, Integer course_code) {

		Optional<Course> course= repo.findById(course_code);
		
		if(course.isEmpty() == true) return null;
		
		Optional<CourseFile> file = getFileFromCourse(filename,course.get());
		
		if(file.isEmpty()) return null;
		
		return file.get();
		
		
	}

	private Optional<CourseFile> getFileFromCourse(String filename, Course course) {

		List<CourseFile> files = course.getFiles();
		
		for (CourseFile file : files) {
			if(file.getFilename().equals(filename)) {
				return Optional.of(file);
			}
		}
		
		
		return Optional.empty();
	}
	
	
	
}
