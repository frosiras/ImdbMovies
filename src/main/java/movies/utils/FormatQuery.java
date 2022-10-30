package movies.utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FormatQuery {
    private static final String startAPI = "http://www.omdbapi.com/?apikey=d3b8c0ed&r=json&";
    private static Logger logger = Logger.getLogger("MovieLibrary");

    public static String getJSONfromURL(String option) throws IOException {
        URL url;
        try {
            url = new URL(startAPI +
                    "type=movie" + "&" +
                    option);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String json = in.readLine();
            logger.debug("json from getJSONfromURL - " + json);
            in.close();
            return json;
        } catch (IOException e) {
            logger.error(e);
            throw e;
        }
    }
}
