package es.nextdigital.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovementsResponse {

    private Long id;

    private Long destinationAccount;

    private Long sourceAccount;

    private String typeMovement;

    private BigDecimal amount;
}
