package br.com.futmatch.domain.valueobjects;

import lombok.Value;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Value
public class DataHoraPartida {
    private static final LocalTime HORA_INICIO_PERMITIDO = LocalTime.of(6, 0); // 06:00
    private static final LocalTime HORA_FIM_PERMITIDO = LocalTime.of(22, 0);   // 22:00
    private static final int DIAS_MINIMOS_ANTECEDENCIA = 1;
    private static final int DIAS_MAXIMOS_ANTECEDENCIA = 30;

    LocalDateTime valor;

    public DataHoraPartida(LocalDateTime valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Data e hora não podem ser nulas");
        }

        LocalDateTime agora = LocalDateTime.now();
        LocalTime horaPartida = valor.toLocalTime();

        // Validação de horário permitido
        if (horaPartida.isBefore(HORA_INICIO_PERMITIDO) || 
            horaPartida.isAfter(HORA_FIM_PERMITIDO)) {
            throw new IllegalArgumentException(
                String.format("Horário deve estar entre %s e %s", 
                    HORA_INICIO_PERMITIDO, HORA_FIM_PERMITIDO)
            );
        }

        // Validação de antecedência mínima
        if (valor.isBefore(agora.plusDays(DIAS_MINIMOS_ANTECEDENCIA))) {
            throw new IllegalArgumentException(
                String.format("Partida deve ser agendada com pelo menos %d dia(s) de antecedência", 
                    DIAS_MINIMOS_ANTECEDENCIA)
            );
        }

        // Validação de antecedência máxima
        if (valor.isAfter(agora.plusDays(DIAS_MAXIMOS_ANTECEDENCIA))) {
            throw new IllegalArgumentException(
                String.format("Partida não pode ser agendada com mais de %d dias de antecedência", 
                    DIAS_MAXIMOS_ANTECEDENCIA)
            );
        }

        this.valor = valor;
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
} 