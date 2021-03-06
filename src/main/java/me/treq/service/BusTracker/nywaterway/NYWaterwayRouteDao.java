package me.treq.service.BusTracker.nywaterway;

import com.google.common.collect.ImmutableMap;
import me.treq.service.BusTracker.dao.BusRouteDao;
import me.treq.service.BusTracker.model.BusRoute;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class NYWaterwayRouteDao implements BusRouteDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(NYWaterwayRouteDao.class);

    private static final String QUERY_PARAM_ID = "id";

    private final Map<String, BusRoute> routeById;

    private final RestTemplate restTemplate;

    private final String mapTranslationBaseUri;

    public NYWaterwayRouteDao(List<BusRoute> busRoutes,
                              RestTemplate restTemplate,
                              String mapTranslationBaseUri) {
        Validate.notNull(busRoutes);

        this.restTemplate = Objects.requireNonNull(restTemplate);
        this.mapTranslationBaseUri = Objects.requireNonNull(mapTranslationBaseUri);

        this.routeById =
                ImmutableMap.copyOf(busRoutes.stream().collect(Collectors.toMap(BusRoute::getRouteId, busRoute -> busRoute)));
    }

    @Override
    public BusRoute getRouteById(String routeId) {
        BusRoute busRoute = this.routeById.get(routeId);
        if (busRoute == null) {
            LOGGER.debug("route not found {}", routeId);
            return null;
        }

        URI uri =
                UriComponentsBuilder.fromUriString(this.mapTranslationBaseUri).queryParam(QUERY_PARAM_ID, routeId).build().toUri();

        MapTranslation mapTranslation = null;
        try {
            mapTranslation = this.restTemplate.getForObject(uri, MapTranslation.class);
        } catch (HttpServerErrorException e) {
            LOGGER.info("mapTranslation not found: routeId {}", routeId);
            return null;
        }

        busRoute.setRouteViewCenter(ExternalModelsTranslationUtil.getCentralGeoLocation(mapTranslation));
        busRoute.setRouteSpanLatitude(mapTranslation.getMapBoundsMaxY() - mapTranslation.getMapBoundsMinY());
        busRoute.setRouteSpanLongitude(mapTranslation.getMapBoundsMaxX() - mapTranslation.getMapBoundsMinX());

        return busRoute;
    }

    @Override
    public Set<String> getAvailableRoutes() {
        return this.routeById.keySet();
    }

}
