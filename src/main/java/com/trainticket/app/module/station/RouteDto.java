package com.trainticket.app.module.station;

public class RouteDto {
    private Long routeId;
    private Long sourceId;
    private Long destinationId;
    private int distance;
    private String name;
    
    public Long getRouteId() {
        return routeId;
    }
    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }
    public Long getSourceId() {
        return sourceId;
    }
    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }
    public Long getDestinationId() {
        return destinationId;
    }
    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }
    public int getDistance() {
        return distance;
    }
    public void setDistance(int distance) {
        this.distance = distance;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
}
