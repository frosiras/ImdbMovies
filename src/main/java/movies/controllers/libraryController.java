package movies.controllers;

import movies.exception.MovieNotFoundException;
import movies.models.ActorEntity;
import movies.models.MovieEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import movies.services.MovieLibrary;


@Controller
public class libraryController {
    MovieLibrary movieLibrary;
    private static void setModel(Model model, MovieEntity movie){
        model.addAttribute("title", movie.getTitle());
        model.addAttribute("id", movie.getImdbId());
        model.addAttribute("year", movie.getYear());
        model.addAttribute("plot", movie.getPlot());
        model.addAttribute("posterURL", movie.getPosterURL());
        model.addAttribute("actors", movie.getActors());
        model.addAttribute("awardWins", movie.getAwardWins());
        model.addAttribute("awardNominations", movie.getAwardNominations());
        model.addAttribute("tomatoMeter", movie.getTomatoMeter());
    }

    @Autowired
    public void setMovieLibrary(MovieLibrary movieLibrary) {
        this.movieLibrary = movieLibrary;
    }

    // DONE
    @RequestMapping(value = "/getMovieById", method = RequestMethod.GET)
    public String getById(@RequestParam String id, Model model) throws MovieNotFoundException {
        MovieEntity movie = movieLibrary.getMovieById(id);
        setModel(model, movie);
        return "movie.html";
    }

    // DONE
    @RequestMapping(value="/getRandomMovie", method = RequestMethod.GET)
    public String getRandom(Model model){
        MovieEntity movie = movieLibrary.getRandomMovie();
        setModel(model, movie);
        return "movie.html";
    }

    // DONE
    @RequestMapping(value="getFavoriteFilms", method = RequestMethod.GET)
    public String getFavoriteFilms(Model model) throws MovieNotFoundException {
        model.addAttribute("list", movieLibrary.getFavoriteFilms());
        return "movielist";
    }

    // DONE
    @RequestMapping(value="findMoviesByName", method = RequestMethod.GET)
    public String findMoviesByName(@RequestParam String movieName, Model model) throws MovieNotFoundException {
        model.addAttribute("list", movieLibrary.findMoviesByName(movieName));
        return "movielist";
    }

    // DONE
    @RequestMapping(value="addMovieToFavoriteByName", method = RequestMethod.GET)
    public String findMovieToFavoriteByName(Model model){
        model.addAttribute("movie", new MovieEntity());
        model.addAttribute("addMovieToFavoriteByName", true);
        return "find";
    }

    // DONE
    @PostMapping("addMovieToFavoriteByName")
    @ResponseBody
    public String addMovieToFavoriteByName(@ModelAttribute("movie") MovieEntity mov) throws MovieNotFoundException {
        movieLibrary.addMovieToFavoriteByName(mov.getTitle());
        return "Movie has been added";
    }

    // DONE
    @RequestMapping(value="addMovieToFavoriteById", method = RequestMethod.GET)
    public String addMovieToFavoriteById(Model model){
        model.addAttribute("movie", new MovieEntity());
        model.addAttribute("addMovieToFavoriteById", true);
        return "find";
    }

    // DONE
    @PostMapping("/addMovieToFavoriteById")
    @ResponseBody
    public String addMovieToFavoriteById(@ModelAttribute("movie") MovieEntity mov) throws MovieNotFoundException {
        movieLibrary.addMovieToFavoriteById(mov.getImdbId());
        return "Movie has been added";
    }

    // DONE
    @RequestMapping(value="getFavoriteFilmsWithActor", method = RequestMethod.GET)
    public String getFavoriteFilmsWithActor(Model model){
        model.addAttribute("actor", new ActorEntity());
        model.addAttribute("getFavoriteFilmsWithActor", true);
        return "find";
    }

    // DONE
    @PostMapping("getFavoriteFilmsWithActor")
    public String getFavoriteFilmsWithActor(@ModelAttribute("actor") ActorEntity actor, Model model){
        model.addAttribute("list", movieLibrary.getFavoriteFilmsWithActor(actor.getFullName()));
        return "movielist";
    }

    // DONE
    @RequestMapping(value="removeMovieFromFavorite", method = RequestMethod.GET)
    public String removeMovieFromFavorite(Model model){
        model.addAttribute("movie", new MovieEntity());
        model.addAttribute("removeMovieFromFavorite", true);
        return "find";
    }

    // DONE
    @PostMapping("removeMovieFromFavorite")
    @ResponseBody
    public String removeMovieFromFavorite(@ModelAttribute("movie") MovieEntity mov) throws MovieNotFoundException {
        movieLibrary.removeMovieFromFavorite(mov.getImdbId());
        return "Movie has been removed";
    }

}
