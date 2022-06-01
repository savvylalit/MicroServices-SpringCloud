package com.savvy.moviecatalogservice.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.savvy.moviecatalogservice.models.CatalogItem;
import com.savvy.moviecatalogservice.models.Movie;
import com.savvy.moviecatalogservice.models.Rating;
import com.savvy.moviecatalogservice.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
				
		UserRating userRating = restTemplate.getForObject("http://movie-rating-service/ratingsdata/users/" + userId, UserRating.class);
		List<Rating> ratings = userRating.getUserRating();

		return ratings.stream().map(rating -> {
			Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
			

			return new CatalogItem(movie.getName(), "desc", rating.getRating());
		}).collect(Collectors.toList());
		
	}
}







