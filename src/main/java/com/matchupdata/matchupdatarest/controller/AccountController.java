package com.matchupdata.matchupdatarest.controller;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

@RestController
public class AccountController
{
    @GetMapping("/api/GetAccountPuuid")
    public JSONObject AccountController(@RequestParam String gameName, @RequestParam String tagLine) throws IOException {
        try {
            String baseUrl = "https://na1.api.riotgames.com";
            String endpoint = baseUrl + "/riot/account/v1/accounts/by-riot-id/" + gameName + "/" + tagLine;

            URL url = new URL(endpoint);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Set the API key as a query parameter
            connection.setRequestProperty("api_key", System.getenv("LeagueAPIKey"));

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

                // Create a JSON response with the "puuid" key
                JSONObject responseJson = new JSONObject();
                responseJson.put("puuid", puuid);

                // Print the JSON response
                return responseJson;
            } else {
                JSONObject responseJson = new JSONObject();
                responseJson.put("error", "Failed to make a GET request. Response code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();

            JSONObject responseJson = new JSONObject();
            responseJson.put("error", exceptionAsString);

            return responseJson;
        }

        JSONObject responseJson = new JSONObject();
        responseJson.put("puuid", "");
        return responseJson;
    }
}
