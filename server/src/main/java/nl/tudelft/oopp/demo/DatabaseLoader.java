package nl.tudelft.oopp.demo;

import nl.tudelft.oopp.demo.entities.Quote;
import nl.tudelft.oopp.demo.repositories.QuoteRepository;
import org.springframework.stereotype.Service;

@Service
public class DatabaseLoader {

    /**
     * Loads dummy data into the QuoteRepository.
     *
     * @param repo the repository to insert the quotes into.
     */
    public DatabaseLoader(QuoteRepository repo) {
        Quote q1 = new Quote(
                "A clever person solves a problem. A wise person avoids it.",
                "Albert Einstein"
        );

        Quote q2 = new Quote(
                "The computer was born to solve problems that did not exist before.",
                "Bill Gates"
        );

        Quote q3 = new Quote(
                "Tell me and I forget.  Teach me and I remember.  Involve me and I learn.",
                "Benjamin Franklin"
        );

        repo.save(q1);
        repo.save(q2);
        repo.save(q3);
    }

}
