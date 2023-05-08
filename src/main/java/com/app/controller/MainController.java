package com.app.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.dto.ResponseCourseDto;
import com.app.dto.ResponsePostDto;
import com.app.model.Category;
import com.app.model.CourseFile;
import com.app.repository.CategoryRepository;
import com.app.service.CourseService;
import com.app.service.PostService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainController {

	@Autowired
	CategoryRepository c_repo;

	@Autowired
	PostService p_service;
	
	@Autowired
	CourseService c_service;
	
	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	@GetMapping("/forFun")
	public String forFun(Model model) {
		List<Category> categories = c_repo.findAll();
		
		
		List<ResponsePostDto> posts = p_service.getAllPosts(null);
		
		model.addAttribute("categories", categories);
		model.addAttribute("posts",posts);
		return "forFun";
	}
	@GetMapping("/education")
	public String education(Model model) {
		
		List<List<ResponseCourseDto>> courses = c_service.getAllCourses();
		
		model.addAttribute("courses", courses);
		return "education";
	}
	
	@GetMapping("/course/{class_code}")
	public String education(Model model, @PathVariable("class_code") Integer class_code) {
		
		ResponseCourseDto course = c_service.getOneCourse(class_code).get();
		
		model.addAttribute("course", course);
		return "class_page";
	}
	
	@GetMapping(path="/course/{course_code}/download", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<org.springframework.core.io.Resource> download(@PathVariable("course_code") Integer course_code,
			@RequestParam("filename") String filename) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition( ContentDisposition.attachment().build()) ; 	
		CourseFile resp = c_service.retrieveFile(filename,course_code);
		
		if(resp == null) {
			
			return ResponseEntity.ok().headers(headers).body(null);
		}
				
		ByteArrayResource resource = new ByteArrayResource(resp.getData());
		
		MediaType type;
		
		if(resp.getType().equals("pdf")) {
			type = MediaType.APPLICATION_PDF;
		}
		else if (resp.getType().equals("txt")) {
			
			type = MediaType.TEXT_PLAIN;
		}else {
			type = MediaType.IMAGE_JPEG;
			
		}
		
		return ResponseEntity.ok().headers(headers).contentLength(resource.contentLength()).contentType(type).body(resource);
	}
	
	@GetMapping("/post/image/{id}")
	public void showPostImage(@PathVariable("id") Integer id, HttpServletResponse response) throws IOException {
		
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		
		ResponsePostDto post = p_service.getOnePost(id).get();
		
		InputStream is = new ByteArrayInputStream(post.getImage().getData());
		IOUtils.copy(is, response.getOutputStream());
		
	}
	
	@GetMapping("/course/{id}/image/")
	public void showCourseImage(@PathVariable("id") Integer id, HttpServletResponse response) throws IOException {
		
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		
		ResponseCourseDto dto = c_service.getOneCourse(id).get();
		if(dto.getImage() != null) {
			
		InputStream is = new ByteArrayInputStream(dto.getImage());
		IOUtils.copy(is, response.getOutputStream());
		}
		
	}
	
	
	
}
