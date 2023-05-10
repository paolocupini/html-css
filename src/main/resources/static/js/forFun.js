function createPost() {
	const formData = new FormData();


	const post = {
		author: $('#author').val(),
		title: $('#title').val(),
		category: $('#category').val(),
		body: $('#body').val(),
		event_date: $('#date').val()
	};
	console.log(JSON.stringify(post));

	if (validatePost(post) == true) {
		console.log("Post is correct");

		formData.append('image', $('#uploadBtn')[0].files[0]);
		formData.append('post', new Blob([JSON.stringify(post)], { type: 'application/json' }));
		sendRequestPostDto(formData);
		
	} else {
		console.log("post not correct");
		return false;
	}



}

$(document).ready(function() {
	$(author_lb).text("");
	$(title_lb).text("");
	$(body_lb).text("");

	
	$('#postForm').on('submit', function (event) {
		event.preventDefault();
		createPost();
	});
	$('#postForm').on('reset', function (event) {
	
		resetForm();
		
	});


});

function resetForm() {
	var elems = document.querySelectorAll(".error");
	elems.forEach(itm => {
	  document.getElementById(itm.id).innerHTML = ''
	})
  }


function validatePost(post) {
	var res2 = true;
	var res1 = checkFormFields(post);

	var formData = new FormData();
	formData.append('image', $('#uploadBtn')[0].files[0]);
	formData.append('post', new Blob([JSON.stringify(post)], { type: 'application/json' }));

	$.ajax({
		url: 'http://localhost:8080/api/post/validate',
		type: 'POST',
		data: formData,
		contentType: false,
		processData: false,
		success: function(response) {


			if (response !== "") {
				console.log(response);
				console.log("Errors in the form input, displaying errors");
				displayErrors(response);
				res2 = false;
			} else {
				res2 = true;
			}

			//   console.log('Request sent successfully:', response);
		},
		error: function(xhr, status, error) {
			console.error('Error creating post:', error);
		}
	});

	if (res1 == false || res2 == false) {
		return false;
	} else {
		return true;
	}
}

function sendRequestPostDto(formData) {
	$.ajax({
		url: 'http://localhost:8080/api/post/create',
		type: 'POST',
		data: formData,
		contentType: false,
		processData: false,
		success: function(response) {
			console.log('Post created successfully:', response);
			location.reload(true);
		},
		error: function(xhr, status, error) {
			console.error('Error creating post:', error);
		}
	});
	// RELOAD DOPO UPDATE CON SUCCESSO
	

}

function displayErrors(response) {

	if (response.event_date !== null) {
		$(date_lb).text(response.event_date);
	}
	if (response.image !== null) {
		$(image_lb).text(response.image);

	}
	if (response.category !== null) {
		$(category_lb).text(response.category);
	}

}

function checkFormFields(post) {
	$(author_lb).text("");
	$(title_lb).text("");
	$(body_lb).text("");

	var res = true;
	if (post["title"].length > 255) {
		$(title_lb).text("Max title length is 255");
		res = false;
	}
	if (post["title"] == "") {
		$(title_lb).text("Title cannot be empty");
		res = false;
	}
	if (post["author"] == "") {
		$(author_lb).text("Author cannot be empty");
		res = false;
	}
	if (post["author"].length < 3 || post["author"].length > 255) {
		$(author_lb).text("Author name should be at least 3 characters long and 255 maximum long");
		res = false;
	}
	if (post["body"] == "") {
		$(body_lb).text("body cannot be empty");
		res = false;
	}
	if (post["body"].length < 50 || post["body"].length > 1000) {
		$(body_lb).text("Body should be at least 50 characters long and 1000 maximum long");
		res = false;
	}
	


	return res;




}