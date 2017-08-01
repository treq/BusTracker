package me.treq.service.BusTracker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/")
public class RootController {
    private static final String DOMAIN = "localhost:8000/";

    @RequestMapping
    public String printUsage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usage: \n");
        sb.append("  GET " + DOMAIN + "buses \n");
        sb.append("  GET " + DOMAIN + "buses/{ID} \n");
        sb.append("  GET " + DOMAIN + "buses?path={pathId} \n");

        return sb.toString();
    }
}
