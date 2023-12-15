package com.matchupdata.matchupdatarest.dtos;

import java.io.Serializable;

public class PuuidDTO implements Serializable {
    private String puuid;

    public PuuidDTO(String Puuid) {
        this.puuid = Puuid;
    }

    public String getPuuid() {
        return this.puuid;
    }

    public void setPuuid(String Puuid) {
        this.puuid = Puuid;
    }
}
