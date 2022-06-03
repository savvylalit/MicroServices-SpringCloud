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
	
	@RequestMapping("/{userId}")
	//@HystrixCommand(fallbackMethod = "getFallbackCatalog") // This was used for high level non-granular circuit breaking
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
				
		UserRating userRating = userRatingInfo.getUserRating(userId);

		return userRating.getRatings().stream().map(rating -> movieInfo.getCatalogItem(rating)).collect(Collectors.toList());

	}

	// This was used for high level non-granular circuit breaking
	public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId) {
		return Arrays.asList(new CatalogItem("No movie", "", 0));
	}
}







