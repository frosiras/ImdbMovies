package movies;

import movies.exception.MovieNotFoundException;
import org.junit.BeforeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import movies.models.MovieEntity;
import movies.services.ImplMovieLibrary;
import movies.services.MovieLibrary;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImplMovieLibraryTest {
    private static MovieLibrary library;
    private static MovieEntity mov1 = new MovieEntity();
    private static MovieEntity mov2 = new MovieEntity();
    @BeforeClass
    public static void setUp() throws MovieNotFoundException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("bean.xml");
        library = ctx.getBean("MovieLibrary", MovieLibrary.class);
        // movie 1
        mov1.setTitle("Carmencita");
        mov1.setImdbId("tt0000001");
        mov1.setYear(1894);
        mov1.setPlot("Performing on what looks like a small wooden stage, wearing a dress with a hoop skirt and white high-heeled pumps, Carmencita does a dance with kicks and twirls, a smile always on her face.");
        mov1.setPosterURL("https://m.media-amazon.com/images/M/MV5BZmUzOWFiNDAtNGRmZi00NTIxLWJiMTUtYzhkZGRlNzg1ZjFmXkEyXkFqcGdeQXVyNDE5MTU2MDE@._V1_SX300.jpg");
        mov1.setActors(new ArrayList<>());
        mov1.setAwardWins(0);
        mov1.setAwardNominations(0);
        mov1.setTomatoMeter(0.0);

        // movie 2
        mov2.setTitle("Joker");
        mov2.setImdbId("tt7286456");
        mov2.setYear(2019);
        mov2.setPlot("In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker.");
        mov2.setPosterURL("https://m.media-amazon.com/images/M/MV5BNGVjNWI4ZGUtNzE0MS00YTJmLWE0ZDctN2ZiYTk2YmI3NTYyXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_SX300.jpg");
        mov2.setActors(new ArrayList<>());
        mov2.setAwardWins(106);
        mov2.setAwardNominations(218);
        mov2.setTomatoMeter(68.0);

        // MovieLibrary
        library.addMovieToFavoriteById("tt0000001");
        library.addMovieToFavoriteById("tt7286456");
        library.addMovieToFavoriteById("tt0181852");
        library.addMovieToFavoriteById("tt1340138");
        library.addMovieToFavoriteById("tt0088247");
        library.addMovieToFavoriteById("tt0103064");
    }
    {
        try {
            setUp();
        } catch (MovieNotFoundException e) {
            e.printStackTrace();
        }
    }
    @DisplayName("Test 1 ImplMovieLibrary.getMovieById()")
    @Test
    void testGetMovieById1() throws MovieNotFoundException {
        assertEquals(mov1.toString(), new ImplMovieLibrary().getMovieById("tt0000001").toString());
    }

    @DisplayName("Test 2 ImplMovieLibrary.getMovieById()")
    @Test
    void testGetMovieById2() throws MovieNotFoundException {
        assertEquals(mov2.toString(), new ImplMovieLibrary().getMovieById("tt7286456").toString());
    }

    @DisplayName("Test 1 ImplMovieLibrary.getFavoriteFilmsWithActor()")
    @Test
    void testGetFavoriteFilmsWithActor1() throws Exception {
        assertEquals(4, library.getFavoriteFilmsWithActor("Arnold Schwarzenegger").size(), "Number of Schwarzenegger's films is 4");
    }

    @DisplayName("Test 2 ImplMovieLibrary.getFavoriteFilmsWithActor()")
    @Test
    void testGetFavoriteFilmsWithActor2() throws Exception {
        assertEquals(2, library.getFavoriteFilmsWithActor("Arnold Schwarzenegger", "Linda Hamilton").size(), "Number of Schwarzenegger's films with Linda Hamilton is 2");
    }

    @DisplayName("Test 1 ImplMovieLibrary.removeMovieFromFavorite()")
    @Test
    void testRemoveMovieFromFavorite() throws Exception {
        library.removeMovieFromFavorite("tt0103064");
        assertEquals(3,library.getFavoriteFilmsWithActor("Arnold Schwarzenegger").size());
        library.addMovieToFavoriteById("tt0103064");
    }

    @DisplayName("Test 1 ImplMovieLibrary.getFavoriteFilms()")
    @Test
    void testGetFavoriteFilms() {
        assertEquals(6, library.getFavoriteFilms().size());
    }

}


