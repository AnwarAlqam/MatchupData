package com.matchupdata.matchupdatarest.util;

import ch.qos.logback.core.util.TimeUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeagueUtil {

    static final String BASEURL = "https://na1.api.riotgames.com";
    private static final Logger logger = LoggerFactory.getLogger(LeagueUtil.class);
    public static List<Map<String, Object>> getPlayers(String queue, String tier, String division) {
        List<Map<String, Object>> masterList = new ArrayList<>();

        try {
            int page = 1;
            while (true) {
                logger.info(Integer.toString(page));
                List<String> result = new ArrayList<>();
                String endpoint = String.format("%s/lol/league/v4/entries/%s/%s/%s?page=%d&api_key=%s",
                        BASEURL, queue, tier, division, page, System.getenv("LeagueAPIKey"));

                URL url = new URL(endpoint);
                // Open a connection to the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Get the response code
                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {
                    // Reading the response from the API
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                    List<Map<String, Object>> resultList = gson.fromJson(response.toString(), listType);

                    if (resultList.size() == 0) {
                        break;
                    }

                    for (var element: resultList) {
                        masterList.add(element);
                    }

                    page++;
                } else {
                    break;
                }
            }

            logger.info(masterList.toString());
            logger.info(Integer.toString(page));

            return masterList;
        } catch (IOException e) {
            return null;
        }
    }
    public static String getPuuid(String summonerName) {
        try {
            String endpoint = BASEURL + "/lol/summoner/v4/summoners/by-name/" + URLEncoder.encode(summonerName, StandardCharsets.UTF_8) + "?api_key=" + System.getenv("LeagueAPIKey");

            URL url = new URL(endpoint);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                // Reading the response from the API
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON response to extract the "puuid" key
                JSONObject jsonResponse = new JSONObject(response.toString());
                String puuid = jsonResponse.getString("puuid");

                return puuid;
            } else {
                JSONObject responseJson = new JSONObject();
                responseJson.put("error", "Failed to make a GET request. Response code: " + responseCode);

                // Close the connection
                connection.disconnect();
                return "";
            }
        } catch (IOException e) {
            return e.toString();
        }
    }


}
