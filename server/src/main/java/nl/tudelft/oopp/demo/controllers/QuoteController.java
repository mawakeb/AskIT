package nl.tudelft.oopp.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nl.tudelft.oopp.demo.entities.Quote;
import nl.tudelft.oopp.demo.repositories.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("quote")
public class QuoteController {

    @Autowired
    private QuoteRepository repo;

    @GetMapping("search") // for /quote/search
    @ResponseBody
    public List<Quote> searchQuotes(@RequestParam String q) {
        return repo.findAllByQuoteContains(q);
    }


    @GetMapping("send/question") // for /quote/search
    @ResponseBody
    public void sendQuestion(@RequestParam String question) {

    }


    /**
     * GET Endpoint to retrieve a random quote.
     *
     * @return randomly selected {@link Quote}.
     */

    @GetMapping
    @ResponseBody
    public Quote getRandomQuote() {
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

        ArrayList<Quote> quotes = new ArrayList<>();
        quotes.add(q1);
        quotes.add(q2);
        quotes.add(q3);

        return quotes.get(new Random().nextInt(quotes.size()));
    }
}
