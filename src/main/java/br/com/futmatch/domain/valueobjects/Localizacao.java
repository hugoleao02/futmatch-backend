package br.com.futmatch.domain.valueobjects;

import lombok.Value;

@Value
public class Localizacao {
    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;
    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LONGITUDE = 180.0;

    double latitude;
    double longitude;

    public Localizacao(double latitude, double longitude) {
        if (latitude < MIN_LATITUDE || latitude > MAX_LATITUDE) {
            throw new IllegalArgumentException(
                String.format("Latitude deve estar entre %.1f e %.1f", MIN_LATITUDE, MAX_LATITUDE)
            );
        }
        if (longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE) {
            throw new IllegalArgumentException(
                String.format("Longitude deve estar entre %.1f e %.1f", MIN_LONGITUDE, MAX_LONGITUDE)
            );
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double distanciaEmKm(Localizacao outra) {
        final int R = 6371; // Raio da Terra em km

        double lat1 = Math.toRadians(this.latitude);
        double lat2 = Math.toRadians(outra.latitude);
        double deltaLat = Math.toRadians(outra.latitude - this.latitude);
        double deltaLon = Math.toRadians(outra.longitude - this.longitude);

        double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
                  Math.cos(lat1) * Math.cos(lat2) *
                  Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        
        return R * c;
    }
} 