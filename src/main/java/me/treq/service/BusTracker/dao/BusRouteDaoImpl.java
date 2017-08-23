package me.treq.service.BusTracker.dao;

import com.google.common.collect.ImmutableMap;
import me.treq.service.BusTracker.model.BusRoute;
import me.treq.service.BusTracker.model.nywportal.MapTranslation;
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

@Repository("busRouteDao")
public class BusRouteDaoImpl implements BusRouteDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusRouteDaoImpl.class);

    private static final String QUERY_PARAM_ID = "id";

    private final Map<Integer, BusRoute> routeById;

    private final RestTemplate restTemplate;

    private final URI mapTranslationBaseUri;

    public BusRouteDaoImpl(List<BusRoute> busRoutes, RestTemplate restTemplate, URI mapTranslationBaseUri) {
        Validate.notNull(busRoutes);

        this.restTemplate = Objects.requireNonNull(restTemplate);
        this.mapTranslationBaseUri = Objects.requireNonNull(mapTranslationBaseUri);

        this.routeById =
                ImmutableMap.copyOf(busRoutes.stream().collect(Collectors.toMap(BusRoute::getRouteId, busRoute -> busRoute)));
    }

    @Override
    public BusRoute getRouteById(int routeId) {
        URI uri =
                UriComponentsBuilder.fromUri(this.mapTranslationBaseUri).queryParam(QUERY_PARAM_ID, routeId).build().toUri();

        MapTranslation mapTranslation;
        try {
            mapTranslation = this.restTemplate.getForObject(uri, MapTranslation.class);
        } catch (HttpServerErrorException e) {
            LOGGER.info("mapTranslation not found: routeId {}", routeId);
            return null;
        }

        BusRoute busRoute = this.routeById.get(routeId);
        busRoute.setRouteViewCenter(LocationTranslationUtil.getCentralGeoLocation(mapTranslation));
        busRoute.setRouteSpanLatitude(mapTranslation.getMapBoundsMaxY() - mapTranslation.getMapBoundsMinY());
        busRoute.setRouteSpanLongitude(mapTranslation.getMapBoundsMaxX() - mapTranslation.getMapBoundsMinX());

        return busRoute;
    }

    @Override
    public Set<Integer> getAvailableRoutes() {
        return this.routeById.keySet();
    }

}
