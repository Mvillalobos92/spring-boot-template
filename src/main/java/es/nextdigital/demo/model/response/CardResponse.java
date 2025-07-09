package es.nextdigital.demo.model.response;

import es.nextdigital.demo.entities.Account;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardResponse {

    private Long id;

    private Long cardType;

    private BigDecimal limit;

    private BigDecimal credit;

    private Account account;

    private boolean active;

    private String pin;
}
