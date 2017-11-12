package me.treq.service.BusTracker.njtransit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

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
    }

    @Data
    @JacksonXmlRootElement(localName = "pt")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        @JacksonXmlProperty(localName = "lat")
        double lat;

        @JacksonXmlProperty(localName = "lon")
        double lon;
    }
}
