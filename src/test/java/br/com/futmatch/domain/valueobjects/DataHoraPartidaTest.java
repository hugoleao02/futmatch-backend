package br.com.futmatch.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class DataHoraPartidaTest {

    @Test
    void shouldCreateValidDataHoraPartida() {
        LocalDateTime future = LocalDateTime.now().plusDays(2).withHour(14).withMinute(0);
        DataHoraPartida dataHora = new DataHoraPartida(future);

        assertEquals(future, dataHora.valor());
    }

    @Test
    void shouldRejectNull() {
        assertThrows(IllegalArgumentException.class, () -> new DataHoraPartida(null));
    }

    @Test
    void shouldRejectIfTooEarly() {
        LocalDateTime early = LocalDateTime.now().plusDays(3).withHour(5).withMinute(0);
        assertThrows(IllegalArgumentException.class, () -> new DataHoraPartida(early));
    }

    @Test
    void shouldRejectIfTooLate() {
        LocalDateTime late = LocalDateTime.now().plusDays(3).withHour(23).withMinute(0);
        assertThrows(IllegalArgumentException.class, () -> new DataHoraPartida(late));
    }

    @Test
    void shouldRejectIfTooSoon() {
        LocalDateTime tomorrow = LocalDateTime.now().plusHours(5);
        assertThrows(IllegalArgumentException.class, () -> new DataHoraPartida(tomorrow));
    }

    @Test
    void shouldRejectIfTooFarInFuture() {
        LocalDateTime far = LocalDateTime.now().plusDays(31).withHour(14).withMinute(0);
        assertThrows(IllegalArgumentException.class, () -> new DataHoraPartida(far));
    }

    @Test
    void shouldAcceptMinAntecedencia() {
        LocalDateTime valid = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0);
        assertDoesNotThrow(() -> new DataHoraPartida(valid));
    }

    @Test
    void shouldAcceptMaxAntecedencia() {
        LocalDateTime valid = LocalDateTime.now().plusDays(29).withHour(14).withMinute(0);
        assertDoesNotThrow(() -> new DataHoraPartida(valid));
    }

    @Test
    void shouldAcceptEarlyMorningBoundary() {
        LocalDateTime valid = LocalDateTime.now().plusDays(5).withHour(6).withMinute(0).withSecond(0).withNano(0);
        assertDoesNotThrow(() -> new DataHoraPartida(valid));
    }

    @Test
    void shouldAcceptLateEveningBoundary() {
        LocalDateTime valid = LocalDateTime.now().plusDays(5).withHour(22).withMinute(0).withSecond(0).withNano(0);
        assertDoesNotThrow(() -> new DataHoraPartida(valid));
    }
}
