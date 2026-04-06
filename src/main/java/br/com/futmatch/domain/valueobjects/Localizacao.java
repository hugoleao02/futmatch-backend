package br.com.futmatch.domain.valueobjects;

import java.util.Objects;

public final class Localizacao {
    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;
    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LONGITUDE = 180.0;

    private final double latitude;
    private final double longitude;

    public static Localizacao criadaPara(double latitude, double longitude) {
        return new Localizacao(latitude, longitude);
    }

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

    public double latitude() {
        return latitude;
    }

    public double longitude() {
        return longitude;
    }

    public double distanciaEmKm(Localizacao outra) {
        final int R = 6371;

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

    @Override
    public String toString() {
        return String.format("(%s, %s)", latitude, longitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Localizacao that = (Localizacao) o;
        return Double.compare(that.latitude, latitude) == 0 &&
               Double.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
