package es.nextdigital.demo.repositories;

import es.nextdigital.demo.entities.Movements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementsRepository extends JpaRepository<Movements, Long> {

    List<Movements> findBySourceAccountOrDestinationAccount(Long sourceAccount, Long destinationAccount);

}
