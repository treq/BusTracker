package me.treq.service.BusTracker.nywaterway;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties
@Data
public class MapTranslation {
    private double mapConversionX;

    private double mapConversionY;

    private double mapBoundsMinX;

    private double mapBoundsMinY;

    private double mapBoundsMaxX;

    private double mapBoundsMaxY;
}
