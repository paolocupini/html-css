package com.app.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.RequestPostDto;
import com.app.dto.ResponsePostDto;
import com.app.exception.ValidationException;
import com.app.mapper.PostMapper;
import com.app.model.Category;
import com.app.model.Post;
import com.app.model.PostImage;
import com.app.repository.CategoryRepository;
import com.app.repository.PostImageRepository;
import com.app.repository.PostRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostService {

	@Autowired
	PostRepository post_repo;

	@Autowired
	CategoryRepository category_repo;

	@Autowired
	PostImageRepository file_repo;

	@Autowired
	PostMapper mapper;

	public Boolean createPost(RequestPostDto request, MultipartFile file) {

		Optional<HashMap<String, String>> validation = validateRequest(request, file);
		if(validation.isEmpty() == false) {
			log.info("Validation failed");
			return false;
		}
		Optional<PostImage> image = mapper.fromMultiPartFileToModel(file);
		Optional<Category> category = category_repo.findById(request.getCategory());
		
		log.info("map RequestPostDto to Post model");
		Optional<Post> post = mapper.fromRequestPostDtoToModel(request, category.get(), image.get());

		if (post.isEmpty()) {
			log.info("post is empty error");
			return false;
		}

		log.info("saving post into the db...");
		post_repo.save(post.get());
		return true;
	}

	@Transactional
	public Optional<ResponsePostDto> getOnePost(Integer id) {

		log.info("Search into the db for the post with id: " + String.valueOf(id));
		Optional<Post> postOpt = post_repo.findById(id);
		if (postOpt.isEmpty() == true) {
			return Optional.empty();
		}

		Post post = postOpt.get();

		return mapper.fromModelToResponsePostDto(post);
	}

	/**
	 * @param filters["category", "event_date", "text"]
	 * @return
	 */
	@Transactional
	public List<ResponsePostDto> getAllPosts(HashMap<String, String> filters) {

		List<Post> posts;
		List<ResponsePostDto> resp;

		if (filters == null) {

			log.info("sorting by published_at, this is the default sorting strategy");
			posts = post_repo.findAllSortedByPublished_at();
			resp = mapper.fromModelToResponsePostDtoList(posts, null);
		} else {

			if (filters.get("event_date") == "true") {
				log.info("sorting by more recent event-date");
				posts = post_repo.findAllSortedByEvent_date();
			} else {
				posts = post_repo.findAllSortedByPublished_at();
			}

			if (filters.containsKey("category")) {
				log.info("filter by category");
				resp = mapper.fromModelToResponsePostDtoList(posts, filters.get("category"));
			}else {
				resp = mapper.fromModelToResponsePostDtoList(posts, null);

			}
		}

		return resp;

	}

	public Optional<PostImage> getFile(String originalFilename) {

		Optional<PostImage> image = file_repo.getByFilename(originalFilename);
		if (image.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(image.get());

	}

	public boolean saveImage(MultipartFile file) throws Exception {

		PostImage image = new PostImage(file.getBytes(), file.getName());

		file_repo.save(image);

		return true;

	}
	
	public Optional<HashMap<String,String>> validateRequest(RequestPostDto request, MultipartFile file){
		
		log.info("check if category exists");
		Optional<Category> category = category_repo.findById(request.getCategory());
		
		HashMap<String,String> map =  new HashMap<>();
		
		if (category.isPresent() == false) {
			log.info("category does not exists");
			map.put("category", "The selected category does not exists");
		}

		log.info("Map and save PostImage obj");
		
		if(file != null) {
			
		Optional<PostImage> image = mapper.fromMultiPartFileToModel(file);
		if (image.isEmpty() == true) {
			log.info("cannot save image into the db");
			map.put("image", "The selected image cannot be persisted");
		}
		}
		
		Date event_date = request.getEvent_date();
		Date now = new Date();
		if (event_date != null && event_date.before(now)) {
			log.info("event date in the past, this is not allowed!");
			log.info(event_date.toString() + now.toString());
			map.put("event_date", "Event date in the past, should be in the future");
		}
		
		if(map.isEmpty() == true) {
			return Optional.empty();
		}else {
			return Optional.of(map);
		}
		
	}

}
