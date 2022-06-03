package com.savvy.movieratingservice.resources;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.savvy.movieratingservice.models.Rating;
import com.savvy.movieratingservice.models.UserRating;

@RestController
@RequestMapping("/ratingsdata")
public class RatingsResource {
	
	
	
	@RequestMapping("/{movieId}")
	public Rating getRating(@PathVariable("movieId") String movieId) {
		return new Rating(movieId, 4);
	}
	
	@RequestMapping("users/{userId}")
	public UserRating getUserRating(@PathVariable("userId") String userId) {
		UserRating userRating = new UserRating();
        userRating.initData(userId);
		return userRating;
	}

}
