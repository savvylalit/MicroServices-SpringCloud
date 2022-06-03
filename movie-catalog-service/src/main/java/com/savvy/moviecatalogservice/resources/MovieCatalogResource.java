package com.savvy.moviecatalogservice.resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.savvy.moviecatalogservice.models.CatalogItem;
import com.savvy.moviecatalogservice.models.Movie;
import com.savvy.moviecatalogservice.models.Rating;
import com.savvy.moviecatalogservice.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	@Autowired
	private RestTemplate restTemplate;
	
//	@Autowired 
//	private WebClient.Builder webClientBuilder;

	@RequestMapping("/{userId}")
	@HystrixCommand(fallbackMethod = "getFallbackCatalog")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
				
		UserRating userRating = restTemplate.getForObject("http://movie-rating-service/ratingsdata/users/" + userId, UserRating.class);
		List<Rating> ratings = userRating.getRatings();

		return ratings.stream().map(rating -> {
			Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
			
			//WebClient way of calling service. This is asynchronous way.
//			Movie movie = webClientBuilder.build()
//							.get()
//							.uri("http://localhost:8082/movies/" + rating.getMovieId())
//							.retrieve()
//							.bodyToMono(Movie.class)
//							.block();
			
			return new CatalogItem(movie.getName(), "desc", rating.getRating());
		}).collect(Collectors.toList());
		// for each movie id, call movie info service and get details

		// put them all together
		
	}
	
	public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId) {
		return Arrays.asList(new CatalogItem("No movie", "", 0));
	}
}







