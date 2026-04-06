package br.com.futmatch.domain.valueobjects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class LocalizacaoTest {

    @Test
    void shouldCreateLocalizacaoWithValidCoords() {
        Localizacao loc = new Localizacao(-23.5505, -46.6333);

        assertEquals(-23.5505, loc.latitude());
        assertEquals(-46.6333, loc.longitude());
    }

    @Test
    void shouldCalculateDistanceInKm() {
        Localizacao sp = new Localizacao(-23.5505, -46.6333);
        Localizacao rj = new Localizacao(-22.9068, -43.1729);

        double distancia = sp.distanciaEmKm(rj);

        assertTrue(distancia > 350 && distancia < 450);
    }

    @Test
    void shouldCalculateZeroDistanceForSamePoint() {
        Localizacao loc1 = new Localizacao(-10.0, -50.0);
        Localizacao loc2 = new Localizacao(-10.0, -50.0);

        assertEquals(0.0, loc1.distanciaEmKm(loc2));
    }

    @ParameterizedTest
    @CsvSource({
        "91.0, 0.0",
        "-91.0, 0.0",
        "0.0, 181.0",
        "0.0, -181.0",
        "100.0, 200.0",
    })
    void shouldRejectInvalidCoords(double latitude, double longitude) {
        assertThrows(IllegalArgumentException.class, () ->
            new Localizacao(latitude, longitude));
    }
}
