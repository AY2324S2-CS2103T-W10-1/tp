package seedu.realodex.model.person.predicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.realodex.logic.commands.FilterCommand;
import seedu.realodex.logic.parser.Prefix;
import seedu.realodex.logic.parser.exceptions.ParseException;
import seedu.realodex.model.person.HousingType;
import seedu.realodex.model.person.Person;
import seedu.realodex.testutil.PersonBuilder;

class PredicateProducerTest {

    @Test
    void createPredicate_validNamePrefix_createsCorrectPredicate() throws ParseException {
        PredicateProducer predicateProducer = new PredicateProducer();
        List<String> keyphrase = List.of("Alice");

        Person alice = new PersonBuilder().withName("Alice").withRemark("She is a lice").build();
        assertEquals(predicateProducer.createPredicate(PREFIX_NAME, keyphrase),
                new NameContainsKeyphrasePredicate("Alice"));
        assertTrue(predicateProducer.createPredicate(PREFIX_NAME, keyphrase).test(alice));
    }

    @Test
    void createPredicate_validRemarkPrefix_createsCorrectPredicate() throws ParseException {
        PredicateProducer predicateProducer = new PredicateProducer();
        List<String> keyphrase = List.of("She is a lice");

        Person alice = new PersonBuilder().withName("Alice").withRemark("She is a lice").build();
        assertEquals(predicateProducer.createPredicate(PREFIX_REMARK, keyphrase),
                new RemarkContainsKeyphrasePredicate("She is a lice"));
        assertTrue(predicateProducer.createPredicate(PREFIX_REMARK, keyphrase).test(alice));
    }

    @Test
    void createPredicate_validTagPrefix_createsCorrectPredicate() throws ParseException {
        PredicateProducer predicateProducer = new PredicateProducer();
        List<String> keyphrases = List.of("buyer");

        Person bob = new PersonBuilder().withName("Bob").withTags("buyer").build();
        assertTrue(predicateProducer.createPredicate(PREFIX_TAG, keyphrases).test(bob));
    }

    @Test
    void createPredicate_validMultipleTagPrefixes_createsCorrectPredicate() throws ParseException {
        PredicateProducer predicateProducer = new PredicateProducer();
        List<String> keyphrases = List.of("buyer", "seller");

        Person bob = new PersonBuilder().withName("Bob").withTags("buyer", "seller").build();
        assertTrue(predicateProducer.createPredicate(PREFIX_TAG, keyphrases).test(bob));
    }

    @Test
    void createMatchTagPredicate_invalidTagString_assertionErrorWhenInvalidPrefix() {
        PredicateProducer predicateProducer = new PredicateProducer();
        List<String> keyphrases = List.of("customer");

        assertNull(predicateProducer.createMatchTagsPredicate(keyphrases));
    }

    @Test
    void createHousingTypeMatchPredicate_validHousingTypeStrings_createsCorrectPredicate() {
        PredicateProducer predicateProducer = new PredicateProducer();
        List<String> keyphrases = List.of("hdb");
        HousingType housingType = new HousingType("hdb");
        assertEquals(predicateProducer.createHousingTypeMatchPredicate(keyphrases),
                new HousingTypeMatchPredicate(housingType));
    }

    @Test
    void createHousingTypeMatchPredicate_invalidHousingTypeStrings_assertionErrorWhenInvalidPrefix() {
        PredicateProducer predicateProducer = new PredicateProducer();
        List<String> keyphrases = List.of("hdbb");

        assertNull(predicateProducer.createHousingTypeMatchPredicate(keyphrases));
    }

    @Test
    void createPredicate_emptyKeyphrase_throwsParseException() {
        PredicateProducer predicateProducer = new PredicateProducer();
        List<String> keyphrase = List.of("");

        ParseException exception = assertThrows(ParseException.class, () ->
                predicateProducer.createPredicate(PREFIX_NAME, keyphrase));

        assertTrue(exception.getMessage().contains(FilterCommand.MESSAGE_USAGE));
    }

    @Test
    void createPredicate_returnsNullWhenInvalidPrefix() {
        PredicateProducer predicateProducer = new PredicateProducer();
        Prefix unhandledPrefix = new Prefix("unhandled/");
        List<String> keyphrase = List.of("keyphrase");

        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);

        assertThrows(AssertionError.class, () -> predicateProducer.createPredicate(unhandledPrefix, keyphrase));
    }

}
