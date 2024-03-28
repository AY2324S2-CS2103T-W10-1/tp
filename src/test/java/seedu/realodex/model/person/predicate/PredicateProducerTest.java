package seedu.realodex.model.person.predicate;

import org.junit.jupiter.api.Test;
import seedu.realodex.logic.commands.FilterCommand;
import seedu.realodex.logic.parser.Prefix;
import seedu.realodex.logic.parser.exceptions.ParseException;
import seedu.realodex.model.person.Person;
import seedu.realodex.model.person.predicates.NameContainsKeyphrasePredicate;
import seedu.realodex.model.person.predicates.PredicateProducer;
import seedu.realodex.model.person.predicates.RemarkContainsKeyphrasePredicate;
import seedu.realodex.testutil.PersonBuilder;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static seedu.realodex.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_REMARK;

class PredicateProducerTest {

    @Test
    void createPredicate_validNamePrefix_createsCorrectPredicate() throws ParseException {
        PredicateProducer predicateProducer = new PredicateProducer();
        String keyphrase = "Alice";
        Predicate<Person> predicate = predicateProducer.createPredicate(PREFIX_NAME, keyphrase);

        Person alice = new PersonBuilder().withName("Alice").withRemark("She is a lice").build();
        assertEquals(predicateProducer.createPredicate(PREFIX_NAME, keyphrase), new NameContainsKeyphrasePredicate("Alice"));
        assertTrue(predicateProducer.createPredicate(PREFIX_NAME, keyphrase).test(alice));
  }

    @Test
    void createPredicate_validRemarkPrefix_createsCorrectPredicate() throws ParseException {
        PredicateProducer predicateProducer = new PredicateProducer();
        String keyphrase = "She is a lice";
        Predicate<Person> predicate = predicateProducer.createPredicate(PREFIX_REMARK, keyphrase);

        Person alice = new PersonBuilder().withName("Alice").withRemark("She is a lice").build();
        assertEquals(predicateProducer.createPredicate(PREFIX_REMARK, keyphrase), new RemarkContainsKeyphrasePredicate("She is a lice"));
        assertTrue(predicateProducer.createPredicate(PREFIX_REMARK, keyphrase).test(alice));
    }

    @Test
    void createPredicate_emptyKeyphrase_throwsParseException() {
        PredicateProducer predicateProducer = new PredicateProducer();
        String keyphrase = "";

        ParseException exception = assertThrows(ParseException.class, () ->
                predicateProducer.createPredicate(PREFIX_NAME, keyphrase));

        assertTrue(exception.getMessage().contains(FilterCommand.MESSAGE_USAGE));
    }

    @Test
    void createPredicate_unhandledPrefix_throwsParseException() {
        PredicateProducer predicateProducer = new PredicateProducer();
        String keyphrase = "Alice";
        Prefix unhandledPrefix = new Prefix("unhandled/");

        ParseException exception = assertThrows(ParseException.class, () ->
                predicateProducer.createPredicate(unhandledPrefix, keyphrase));

        assertTrue(exception.getMessage().equals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, "Unhandled prefix: " + unhandledPrefix)));
    }

}
