package me.treq.service.BusTracker.njtransit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "buses")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NJBuses {
    @JacksonXmlProperty(localName = "time")
    String time;

    @JacksonXmlProperty(localName = "bus")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<NJBus> buses;
}
