package movies.services;

import com.jayway.jsonpath.JsonPath;
import movies.dao.ActorDao;
import movies.dao.MovieDao;
import movies.exception.MovieNotFoundException;
import movies.models.ActorEntity;
import movies.models.MovieEntity;
import movies.utils.FormatQuery;
import movies.utils.GetFromJSON;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ImplMovieLibrary implements MovieLibrary {
    private static Logger logger = Logger.getLogger("MovieLibrary");

    private ActorDao actorDao;
    private MovieDao movieDao;

    @Autowired
    public void setActorDao(ActorDao actorDao) {
        this.actorDao = actorDao;
    }

    @Autowired
    public void setMovieDao(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    /**
     * Loads movie by its imdb id
     *
     * @param imdbId - id of movie
     * @return movie information
     */
    public MovieEntity getMovieById(String imdbId) throws MovieNotFoundException {
        MovieEntity movie = new MovieEntity();
        try {
            movie = GetFromJSON.getMovie(FormatQuery.getJSONfromURL(("i=" + imdbId)));
        } catch (MovieNotFoundException e) {
            logger.error("Movie not found");
            throw e;
        } catch (IOException e) {
            logger.error("Произошла ошибка: " + e);
        }
        return movie;
    }

    /**
     * Finds movies using by part of its name
     *
     * @param movieName - search string
     * @return list of found movies
     */
    public List<MovieEntity> findMoviesByName(String movieName) throws MovieNotFoundException {
        ArrayList<MovieEntity> movies = new ArrayList<>();
        try {
            String json = FormatQuery.getJSONfromURL("s=" + movieName);
            ArrayList<String> imdbID = JsonPath.parse(json).read("$.Search[*].imdbID");
            imdbID.forEach(titleId ->
            {
                try {
                    movies.add(GetFromJSON.getMovie(FormatQuery.getJSONfromURL(("i=" + titleId))));
                } catch (Exception e) {
                    logger.error(e);
                }
            });
        } catch (IOException e) {
            logger.error("Произошла ошибка: " + e);
        }
        logger.debug(movies);
        return movies;
    }

    /**
     * Adds film in personal collection by imdb id
     *
     * @param imdbId - id of film
     * @return true if this film was already in collection, false - otherwise
     */
    public boolean addMovieToFavoriteById(String imdbId) throws MovieNotFoundException {
        MovieEntity movie = getMovieById(imdbId);
        try {
            movieDao.save(movie);
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
        return true;
    }

    /**
     * Loads random movie from movie database
     *
     * @return movie information
     */
    public MovieEntity getRandomMovie() {
        logger.debug("getRandomMovie() has been called");
        MovieEntity movie = new MovieEntity();
        boolean point = true;
        do {
            String randomValue = String.valueOf((int) (Math.ceil(Math.random() * 10000000)));
            while (randomValue.length() < 7)
                randomValue = "0" + randomValue;
            String json = "Season";
            try {
                json = FormatQuery.getJSONfromURL("i=tt" + randomValue);
            } catch (IOException e) {
                logger.error("getRandomMovie json hasn't been created");
            }
            logger.debug("tt" + randomValue);
            if (!json.contains("Season") && !json.contains("\"Response\":\"False\",\"Error\":\"Error getting data.\"")) {
                try {
                    movie = GetFromJSON.getMovie(json);
                } catch (Exception e) {
                    logger.debug("Такого фильма нет, ищу еще");
                }
                point = false;
            }
        } while (point);
        logger.debug(movie.toString());
        return movie;
    }

    public void removeMovieFromFavorite(String imdbId) {
        try {
            MovieEntity movie = movieDao.findById(imdbId);
            movieDao.delete(movie);
            logger.debug(movie.getTitle() + " has been deleted");
        } catch (Exception e) {
            logger.error(e);
        }

    }

    public List<MovieEntity> getFavoriteFilms() {
        try {
            return movieDao.getAllMovies();
        } catch (Exception e) {
            logger.error(e);
        }
        return new ArrayList<>();
    }

    public boolean addMovieToFavoriteByName(String movieName) throws MovieNotFoundException {
        try {
            MovieEntity movie = GetFromJSON.getMovie(FormatQuery.getJSONfromURL("t=" + movieName));
            movieDao.save(movie);
        } catch (IOException e) {
            logger.error("getRandomMovie json hasn't been created");
            return false;
        } catch (MovieNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e);
        }
        return true;
    }

    public List<MovieEntity> getFavoriteFilmsWithActor(String fullName) {
        ActorEntity actor = new ActorEntity();
        actor.setFullName(fullName);
        return actorDao.findAllFilmsByFullName(actor);
    }

    public List<MovieEntity> getFavoriteFilmsWithActor(String fullName1, String fullName2) {
        ActorEntity actor1 = new ActorEntity();
        actor1.setFullName(fullName1);
        ActorEntity actor2 = new ActorEntity();
        actor2.setFullName(fullName2);
        try {
            return actorDao.findAllFilmsByFullName(actor1, actor2);
        } catch (Exception e) {
            logger.error(e);
        }
        return new ArrayList<MovieEntity>();
    }
}