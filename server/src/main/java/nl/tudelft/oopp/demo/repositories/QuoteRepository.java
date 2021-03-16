package nl.tudelft.oopp.demo.repositories;

import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.demo.entities.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface QuoteRepository extends JpaRepository<Quote, Long> {
    @Query("SELECT q FROM Quote q WHERE q.id = ?1")
    Quote findQuoteById(UUID uuid);

    List<Quote> findAllByQuoteContains(String query);

}
