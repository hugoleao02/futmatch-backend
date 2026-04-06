package br.com.futmatch.domain.valueobjects;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public final class DataHoraPartida {
    private static final LocalTime HORA_INICIO_PERMITIDO = LocalTime.of(6, 0);
    private static final LocalTime HORA_FIM_PERMITIDO = LocalTime.of(22, 0);
    private static final int DIAS_MINIMOS_ANTECEDENCIA = 1;
    private static final int DIAS_MAXIMOS_ANTECEDENCIA = 30;

    private final LocalDateTime valor;

    public DataHoraPartida(LocalDateTime valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Data e hora não podem ser nulas");
        }

        LocalDateTime agora = LocalDateTime.now();
        LocalTime horaPartida = valor.toLocalTime();

        if (horaPartida.isBefore(HORA_INICIO_PERMITIDO) ||
            horaPartida.isAfter(HORA_FIM_PERMITIDO)) {
            throw new IllegalArgumentException(
                String.format("Horário deve estar entre %s e %s",
                    HORA_INICIO_PERMITIDO, HORA_FIM_PERMITIDO)
            );
        }

        if (valor.isBefore(agora.plusDays(DIAS_MINIMOS_ANTECEDENCIA))) {
            throw new IllegalArgumentException(
                String.format("Partida deve ser agendada com pelo menos %d dia(s) de antecedência",
                    DIAS_MINIMOS_ANTECEDENCIA)
            );
        }

        if (valor.isAfter(agora.plusDays(DIAS_MAXIMOS_ANTECEDENCIA))) {
            throw new IllegalArgumentException(
                String.format("Partida não pode ser agendada com mais de %d dias de antecedência",
                    DIAS_MAXIMOS_ANTECEDENCIA)
            );
        }

        this.valor = valor;
    }

    public LocalDateTime valor() {
        return valor;
    }

    public boolean isPassada() {
        return valor.isBefore(LocalDateTime.now());
    }

    public boolean isFutura() {
        return valor.isAfter(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return valor.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataHoraPartida that = (DataHoraPartida) o;
        return Objects.equals(valor, that.valor);
    }

    @Override
    public int hashCode() {
        return valor != null ? valor.hashCode() : 0;
    }
}
