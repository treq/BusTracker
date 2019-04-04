package me.treq.service.BusTracker.njtransit;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "route")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NJBusRoute {
    @JacksonXmlProperty(localName = "id")
    int routeId;

    @JacksonXmlProperty(localName = "nm")
    String description;

    @JacksonXmlElementWrapper(localName = "pas")
    PAS pas;

    @Data
    @JacksonXmlRootElement(localName = "pas")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PAS {
        @JacksonXmlProperty(localName = "pa")
        @JacksonXmlElementWrapper(useWrapping = false)
        List<PA> paList;
    }

    @Data
    @JacksonXmlRootElement(localName = "pa")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PA {
        @JacksonXmlProperty(localName = "pt")
        @JacksonXmlElementWrapper(useWrapping = false)
        List<Location> locations;

        @JacksonXmlProperty(localName = "id")
        long id;

        @JacksonXmlProperty(localName = "d")
        String description;
    }

    @Data
    @JacksonXmlRootElement(localName = "bs")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Stop {
        @JacksonXmlProperty(localName = "id")
        long id;

        @JacksonXmlProperty(localName = "st")
        String stopName;
    }

    @Data
    @JacksonXmlRootElement(localName = "pt")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        @JacksonXmlProperty(localName = "lat")
        double lat;

        @JacksonXmlProperty(localName = "lon")
        double lon;

        @JacksonXmlProperty(localName = "bs")
        Stop stop;
    }

}
