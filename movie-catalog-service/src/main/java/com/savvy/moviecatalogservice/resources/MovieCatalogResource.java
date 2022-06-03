package com.savvy.moviecatalogservice.resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.savvy.moviecatalogservice.models.CatalogItem;
import com.savvy.moviecatalogservice.models.UserRating;
import com.savvy.moviecatalogservice.services.MovieInfo;
import com.savvy.moviecatalogservice.services.UserRatingInfo;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	@Autowired
	UserRatingInfo userRatingInfo;
	
	@Autowired
	MovieInfo movieInfo;
	
	@Autowired
	private RestTemplate restTemplate;
	
//	@Autowired 
//	private WebClient.Builder webClientBuilder;

	@RequestMapping("/{userId}")
	//@HystrixCommand(fallbackMethod = "getFallbackCatalog") // This was used for high level non-granular circuit breaking
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
				
		UserRating userRating = userRatingInfo.getUserRating(userId);

		return userRating.getRatings().stream().map(rating -> movieInfo.getCatalogItem(rating)).collect(Collectors.toList());
		// for each movie id, call movie info service and get details

		// put them all together
		
	}

	/*
	 * @HystrixCommand(fallbackMethod = "getFallbackCatalogItem") private
	 * CatalogItem getCatalogItem(Rating rating) { Movie movie =
	 * restTemplate.getForObject("http://movie-info-service/movies/" +
	 * rating.getMovieId(), Movie.class);
	 * 
	 * //WebClient way of calling service. This is asynchronous way. // Movie movie
	 * = webClientBuilder.build() // .get() // .uri("http://localhost:8082/movies/"
	 * + rating.getMovieId()) // .retrieve() // .bodyToMono(Movie.class) //
	 * .block();
	 * 
	 * return new CatalogItem(movie.getName(), "desc", rating.getRating()); }
	 * 
	 * private CatalogItem getFallbackCatalogItem(Rating rating) { return new
	 * CatalogItem("No Movie", "Movie unavailable", 0); }
	 * 
	 * @HystrixCommand(fallbackMethod = "getFallbackUserRating") private UserRating
	 * getUserRating(String userId) { return
	 * restTemplate.getForObject("http://movie-rating-service/ratingsdata/users/" +
	 * userId, UserRating.class); }
	 * 
	 * private UserRating getFallbackUserRating(String userId) { UserRating
	 * userRating = new UserRating(); userRating.setUserId(userId);
	 * userRating.setRatings(Arrays.asList(new Rating("0", 0))); return userRating;
	 * }
	 */
	
	// This was used for high level non-granular circuit breaking
	public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId) {
		return Arrays.asList(new CatalogItem("No movie", "", 0));
	}
}







