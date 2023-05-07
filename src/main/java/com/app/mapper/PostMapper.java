package com.app.mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.RequestPostDto;
import com.app.dto.ResponsePostDto;
import com.app.model.Category;
import com.app.model.Post;
import com.app.model.PostImage;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PostMapper {

	public Optional<Post> fromRequestPostDtoToModel(RequestPostDto request, Category category, PostImage image) {
		log.info("mapping...");

		Date date = new Date();

		String preview = request.getBody();

		preview = preview.substring(0, 40) + "...";

		
		

		return Optional.of(Post.builder().author(request.getAuthor()).title(request.getTitle()).body(request.getBody())
				.category(category)
				.event_date(request.getEvent_date())
				.image(image)
				.published_at(date)
				.preview(preview)
				.build());
	}

	public Optional<PostImage> fromMultiPartFileToModel(MultipartFile image_data) {
		PostImage image;
		try {
			image = new PostImage(image_data.getBytes(), image_data.getResource().getFilename());
		} catch (IOException e) {
			return Optional.empty();
		}
		return Optional.of(image);
	}

	public Optional<ResponsePostDto> fromModelToResponsePostDto(Post post) {

		return Optional.of(ResponsePostDto.builder().author(post.getAuthor()).title(post.getTitle())
				.body(post.getBody())
				.category(post.getCategory()
				.getName())
				.event_date(post.getEvent_date())
				.image(post.getImage())
				.published_at(post.getPublished_at())
				.preview(post.getPreview())

				.build());
	}

	public List<ResponsePostDto> fromModelToResponsePostDtoList(List<Post> posts, String category) {

		List<ResponsePostDto> resp = new ArrayList<>(posts.size());

		if (category == null) {

			for (Post post : posts) {
				Optional<ResponsePostDto> p = this.fromModelToResponsePostDto(post);
				if (p.isEmpty() == false)
					resp.add(p.get());
			}

		} else {
			
			for (Post post : posts) {
				
				if(post.getCategory().getName().equals(category) == true) {
					
					Optional<ResponsePostDto> p = this.fromModelToResponsePostDto(post);
					if (p.isEmpty() == false)
						resp.add(p.get());
				}
			}
			
		}
		
		return resp;

	}

}
