package com.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.tomcat.util.file.ConfigurationSource.Resource;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.ResponseCourseDto;
import com.app.model.Course;
import com.app.model.CourseFile;
import com.app.service.CourseService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author angelo C -> create single Course from object R -> show all Courses as
 *         list of lists : List<ResponseCourseDto> : [0 => first year , 1 =>
 *         second_year, 2 => third_year] or show one by class code U - D-> Not
 *         yet supported
 * 
 *         +
 * 
 *         I -> insert material into Course folder
 * 
 *         Each class has its own folder named after its Course code in which
 *         class related material is stored course_code |-------- | storage
 *         |------- files
 */
@RestController
@Slf4j
@RequestMapping("api/course")
public class ApiCourseController {

	@Autowired
	CourseService service;

//	CREATE
	@PostMapping("create")
	public ResponseEntity<Boolean> create(@Valid @RequestPart("course") Course course, @RequestPart("image") MultipartFile image) {

		Boolean resp = service.createCourse(course,image);

		return new ResponseEntity<>(resp, HttpStatus.OK);

	}

//	FILE HANDLING

	@PostMapping(path="{course_code}/upload")
	public ResponseEntity<Boolean> upload(@PathVariable("course_code") Integer course_code,
			@RequestParam("file") MultipartFile file) {

		boolean resp = service.storeFile(file, course_code);
		return new ResponseEntity<Boolean>(resp, HttpStatus.OK);

	}

	@GetMapping(path="{course_code}/download")
	public ResponseEntity<byte[]> download(@PathVariable("course_code") Integer course_code,
			@RequestParam("filename") String filename) {
		
		HttpHeaders headers = new HttpHeaders();
		CourseFile resp = service.retrieveFile(filename,course_code);
		
		if(resp == null) {
			
			return ResponseEntity.ok().headers(headers).body(null);
		}
		
		headers.setContentDisposition( ContentDisposition.attachment().build()) ; 	
		headers.setContentType(MediaType.valueOf(resp.getType()));
		
		return ResponseEntity.ok().headers(headers).body(resp.getData());
	}

//	READ
	@GetMapping("show")
	public ResponseEntity<List<List<ResponseCourseDto>>> showAll() {

		List<List<ResponseCourseDto>> resp = service.getAllCourses();
		return new ResponseEntity<>(resp, HttpStatus.OK);

	}

	@GetMapping("{course_code}/show")
	public ResponseEntity<ResponseCourseDto> showOne(@PathVariable("course_code") Integer course_code) {

		Optional<ResponseCourseDto> courseOpt = service.getOneCourse(course_code);
		if (courseOpt.isEmpty()) {
			return new ResponseEntity<ResponseCourseDto>(HttpStatus.OK);
		}
		return new ResponseEntity<>(courseOpt.get(), HttpStatus.OK);

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<HashMap<String, String>>> handleParameterNotValid(MethodArgumentNotValidException e) {
		log.error("Argument not valid from controller callback");

		List<HashMap<String, String>> res = new ArrayList<>();

		for (FieldError r : e.getFieldErrors()) {

			HashMap<String, String> map = new HashMap<>(2);
			map.put("field", r.getField());
			map.put("message", r.getDefaultMessage());
			res.add(map);
		}

		return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(FileSizeLimitExceededException.class)
	public ResponseEntity<String> handleParameterNotValid(FileSizeLimitExceededException e) {
		log.error("File Size limit exceeded");

		return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);

	}
}
