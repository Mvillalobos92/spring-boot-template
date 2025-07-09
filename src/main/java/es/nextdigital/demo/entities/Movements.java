package es.nextdigital.demo.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "movements")
public class Movements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "destination_account")
    private Long destinationAccount;

    @Column(name = "source_account")
    private Long sourceAccount;

    @Column(name = "type_movement")
    private String typeMovement;

    @Column(name = "amount")
    private BigDecimal amount;

    //TODO: faltan por añadir mas campos

}
