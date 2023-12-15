package com.matchupdata.matchupdatarest.controller;

import com.matchupdata.matchupdatarest.dtos.PuuidDTO;
import com.matchupdata.matchupdatarest.util.LeagueUtil;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.matchupdata.matchupdatarest.util.LeagueUtil.*;

@RestController
public class SummonerController
{
    @GetMapping("/api/GetSummonerPuuid")
    public ResponseEntity<PuuidDTO> AccountController(@RequestParam String summonerName) throws IOException {

        var playerList = LeagueUtil.getPlayers("RANKED_SOLO_5x5", "SILVER", "I");
        String puuid = LeagueUtil.getPuuid(summonerName);

        return ResponseEntity.ok(new PuuidDTO(puuid));
    }
}
