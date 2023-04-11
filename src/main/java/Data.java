import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;

@AllArgsConstructor
@lombok.Data
public class Data {
    @JsonProperty("tempCurrent")
    private BigInteger tempCurrent;

    @JsonProperty("tempMax")
    private BigInteger tempMax;

    @JsonProperty("tempMin")
    private BigInteger tempMin;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("timeStamp")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss:SSSZ")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")//, timezone = "UTC")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-DD'T'HH:MM:SS.mmmZ")
    private Date timeStamp;

    @JsonProperty("city")
    private String city;

    @JsonProperty("cityId")
    private BigInteger cityId;

    @Override
    public String toString(){
        return "Stadt:"+city+"\n"+
                "StadtID:"+cityId+"\n"+
                "Zeitstempel:"+timeStamp+"\n"+
                "Kommentar:"+comment+"\n"+
                "Aktuelle Temperatur:"+tempCurrent+"\n"+
                "Maximale Temperatur:"+tempMax+"\n"+
                "Minimale Temperatur:"+tempMin+"\n";
    }
}

