package me.treq.service.BusTracker.model.nywportal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties
@Data
public class MapTranslation {
    public MapTranslation() {}

    private double mapConversionX;

    private double mapConversionY;

    private double mapBoundsMinX;

    private double mapBoundsMinY;

    private double mapBoundsMaxX;

    private double mapBoundsMaxY;
}
