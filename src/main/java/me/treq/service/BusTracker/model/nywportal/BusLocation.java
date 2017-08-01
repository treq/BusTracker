package me.treq.service.BusTracker.model.nywportal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class BusLocation {
    private int x;

    private int y;

    // not sure what this is. maybe ID.
    private int o;

    @JsonProperty("i")
    private int directionOffset;
}
