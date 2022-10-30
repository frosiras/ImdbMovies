package movies.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import movies.exception.MovieNotFoundException;
import movies.models.ActorEntity;
import movies.models.MovieEntity;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class GetFromJSON {
    private static Logger logger = Logger.getLogger("MovieLibrary");

    public static MovieEntity getMovie(String json) throws MovieNotFoundException {
        if (json.contains("\"Response\":\"False\",\"Error\":\"Error getting data.\"")) {
            logger.error("movie hasn't been found by this json: " + json);
            throw new MovieNotFoundException();
        }
        MovieEntity movie = new MovieEntity();
        DocumentContext doc = JsonPath.parse(json);
        try {
            movie.setImdbId(doc.read("$..imdbID").toString().replaceAll("[\\[\"\\]]", ""));
            movie.setTitle(doc.read("$..Title").toString().replaceAll("[\\[\"\\]]", ""));
            movie.setYear(Integer.parseInt(doc.read("$..Year").toString().replaceAll("[\\[\"\\]]", "")));
            movie.setPlot(doc.read("$..Plot").toString().replaceAll("[\\[\"\\]\\\\]", ""));
            movie.setPosterURL(doc.read("$..Poster").toString().replaceAll("[\\[\"\\]\\\\]", ""));
            movie.setActors(getActors(doc));
        } catch (Exception e) {
            logger.error("Movie initialization has been shutted down " + e);
            throw e;
        }
        try {
            String awardsString = doc.read("$..Awards").toString().replaceAll(" & ", ". ").replaceAll("[\\[\"\\]]", "");
            String[] wons = awardsString.split("\\. ");
            int parsedWons = 0;
            for (int i = 0; i < wons.length - 1; i++) {
                String sentence = wons[i];
                parsedWons += Integer.parseInt(
                        sentence.replaceAll("\\D", "")
                                .replaceAll("[\\[\"\\]]", ""));
            }
            if (parsedWons == 0)
                logger.debug(movie.getTitle() + " hasn't won anything");
            movie.setAwardWins(parsedWons);
        } catch (Exception e) {
            logger.debug(movie.getTitle() + " hasn't won anything");
        }
        try {
            String[] temp = doc
                    .read("$..Awards").toString()
                    .replaceAll(" & ", ". ")
                    .replaceAll("[\\[\"\\]]", "")
                    .split("\\. ");
            String nominations = temp[temp.length - 1];
            int value = Integer.parseInt(nominations
                    .replaceAll("\\D", "")
                    .replaceAll("[\\[\"\\]]", ""));
            movie.setAwardNominations(value);
        } catch (Exception e) {
            logger.debug(movie.getTitle() + " hasn't got any nominations");
        }
        try {
            String list = doc.read("$.Ratings[1].Value")
                    .toString()
                    .replaceAll("[\\[\"\\]]", "");
            movie.setTomatoMeter(Double.parseDouble(list.substring(0, list.indexOf('%'))));
        } catch (Exception e) {
            logger.debug(movie.getTitle() + " hasn't got TomatoMeter");
        }
        logger.debug("Movie is formed - " + movie.toString());
        return movie;
    }

    public static List<ActorEntity> getActors(DocumentContext doc) {
        ArrayList<String> actorsMono = doc.read("$..Actors");
        ArrayList<ActorEntity> actors = new ArrayList<>();
        String[] actorsFullName = actorsMono.get(0).split(", ");
        for (int i = 0; i < actorsFullName.length; i++) {
            String fullName = actorsFullName[i];
            ActorEntity actor = new ActorEntity();
            if ("N/A".equals(fullName))
                continue;
            actor.setFullName(fullName);
            actors.add(actor);
        }
        logger.debug("actor have been created: " + actors.stream().sorted().toString());
        return actors;
    }
}
