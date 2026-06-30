import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

public class WeatherService {

    private static final String API_KEY = "65dc88316f6a594610763d1021c69280";

    public static WeatherData getWeather(String city) {

    try {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);

        String urlString =
                "https://api.openweathermap.org/data/2.5/weather?q="
                        + encodedCity
                        + "&appid=" + API_KEY
                        + "&units=metric";

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // ⭐ IMPORTANT: prevent infinite loading
        connection.setConnectTimeout(5000); // 5 sec connect timeout
        connection.setReadTimeout(5000);    // 5 sec read timeout

        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        InputStreamReader streamReader;

        // handle error properly
        if (responseCode != 200) {
            System.out.println("API Error Code: " + responseCode);
            streamReader = new InputStreamReader(connection.getErrorStream());
        } else {
            streamReader = new InputStreamReader(connection.getInputStream());
        }

        BufferedReader reader = new BufferedReader(streamReader);

        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        System.out.println("API Response: " + response); // ⭐ DEBUG LINE

        JSONObject json = new JSONObject(response.toString());

        JSONObject main = json.getJSONObject("main");
        JSONObject wind = json.getJSONObject("wind");

        String condition = json.getJSONArray("weather")
                .getJSONObject(0)
                .getString("main");

        String iconCode = json.getJSONArray("weather")
                .getJSONObject(0)
                .getString("icon");

        return new WeatherData(
                main.getDouble("temp"),
                main.getInt("humidity"),
                wind.getDouble("speed"),
                condition,
                iconCode
        );

    } catch (Exception e) {
        System.out.println("Weather fetch error: " + e.getMessage());
        return null;
    }
}
}