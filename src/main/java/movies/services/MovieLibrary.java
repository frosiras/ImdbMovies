package movies.services;

import movies.exception.MovieNotFoundException;
import movies.models.MovieEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MovieLibrary {

    /**
     * Finds movies using by part of its name
     *
     * @param movieName - search string
     * @return list of found movies
     */
    List<MovieEntity> findMoviesByName(String movieName) throws MovieNotFoundException;

    /**
     * Loads movie by its imdb id
     *
     * @param imdbId - id of movie
     * @return movie information
     */
    MovieEntity getMovieById(String imdbId) throws MovieNotFoundException;

    /**
     * Adds film in personal collection by full movie name
     *
     * @param movieName - name of movie
     * @return true if this film was already in collection, false - otherwise
     */
    boolean addMovieToFavoriteByName(String movieName) throws MovieNotFoundException;

    /**
     * Adds film in personal collection by imdb id
     *
     * @param imdbId - id of film
     * @return true if this film was already in collection, false - otherwise
     */
    boolean addMovieToFavoriteById(String imdbId) throws MovieNotFoundException;

    /*
     * Loads collection of favorite movies
     * @return list of movies
     */
    List<MovieEntity> getFavoriteFilms();

    /**
     * Gets films from personal collections which have specified actor
     *
     * @param fullName - first and second name of actor
     * @return list of movies
     */
    List<MovieEntity> getFavoriteFilmsWithActor(String fullName);

    List<MovieEntity> getFavoriteFilmsWithActor(String fullName1, String fullName2);

    /**
     * Removes film from collection of favorite movies
     *
     * @param imdbId - id of deleted movie
     */
    void removeMovieFromFavorite(String imdbId) throws MovieNotFoundException;


    /**
     * Loads random movie from movie database
     *
     * @return movie information
     */
    MovieEntity getRandomMovie();

}
