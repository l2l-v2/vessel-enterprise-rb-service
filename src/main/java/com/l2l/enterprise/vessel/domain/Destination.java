package com.l2l.enterprise.vessel.domain;

public class Destination {
    private String name;
    private String estiAnchorTime;
    private String estiArrivalTime;
    private String estiDepartureTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEstiAnchorTime() {
        return estiAnchorTime;
    }

    public void setEstiAnchorTime(String estiAnchorTime) {
        this.estiAnchorTime = estiAnchorTime;
    }

    public String getEstiArrivalTime() {
        return estiArrivalTime;
    }

    public void setEstiArrivalTime(String estiArrivalTime) {
        this.estiArrivalTime = estiArrivalTime;
    }

    public String getEstiDepartureTime() {
        return estiDepartureTime;
    }

    public void setEstiDepartureTime(String estiDepartureTime) {
        this.estiDepartureTime = estiDepartureTime;
    }
}
