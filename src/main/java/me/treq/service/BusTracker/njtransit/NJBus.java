package me.treq.service.BusTracker.njtransit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import me.treq.service.BusTracker.model.Location;
import me.treq.service.BusTracker.model.Traceable;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "bus")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NJBus {

    @JacksonXmlProperty(localName = "lat")
    private double lat;

    @JacksonXmlProperty(localName = "lon")
    private double lon;

    @JacksonXmlProperty(localName = "dn")
    private String direction;

    @JacksonXmlProperty(localName = "id")
    private String id;

    @JacksonXmlProperty(localName = "dd")
    private String dest;

}
