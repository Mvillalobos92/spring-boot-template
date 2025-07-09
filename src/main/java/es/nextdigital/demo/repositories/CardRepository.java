package es.nextdigital.demo.repositories;

import es.nextdigital.demo.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByAccount_Id(Long accountId);

}
