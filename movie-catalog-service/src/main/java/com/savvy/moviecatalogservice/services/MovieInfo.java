package com.savvy.moviecatalogservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.savvy.moviecatalogservice.models.CatalogItem;
import com.savvy.moviecatalogservice.models.Movie;
import com.savvy.moviecatalogservice.models.Rating;

@Service
public class MovieInfo {
	
	@Autowired
	private RestTemplate restTemplate;

	//	@Autowired 
	//	private WebClient.Builder webClientBuilder;

	@HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
	public CatalogItem getCatalogItem(Rating rating) {
		Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);

		// WebClient way of calling service. This is asynchronous way.
		// Movie movie = webClientBuilder.build()
		// .get()
		// .uri("http://localhost:8082/movies/" + rating.getMovieId())
		// .retrieve()
		// .bodyToMono(Movie.class)
		// .block();

		return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
	}

	public CatalogItem getFallbackCatalogItem(Rating rating) {
		return new CatalogItem("Movie name not found", "", rating.getRating());
	}
}
