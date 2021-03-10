package nl.tudelft.oopp.demo;

import nl.tudelft.oopp.demo.entities.Quote;
import nl.tudelft.oopp.demo.repositories.QuoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class QuoteTest {
    @Autowired
    private QuoteRepository quoteRepository;

    @Test
    public void saveAndRetrieveQuote() {
        String quoteText = "Tell me and I forget. Teach me and I remember. Involve me and I learn.";
        String quoteAuthor = "Benjamin Franklin";
        Quote quote = new Quote(quoteText, quoteAuthor);
        quoteRepository.save(quote);

        Quote quote2 = quoteRepository.findQuoteById(quote.getId());
        assertEquals(quote, quote2);
    }
}