package seedu.realodex.model.person.predicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.realodex.model.person.Person;
import seedu.realodex.testutil.PersonBuilder;



public class RemarkContainsKeyphrasePredicateTest {

    @Test
    public void equals() {
        String firstPredicateKeyphrase = "first";
        String secondPredicateKeyphrase = "first second";

        RemarkContainsKeyphrasePredicate firstPredicate =
                new RemarkContainsKeyphrasePredicate(firstPredicateKeyphrase);
        RemarkContainsKeyphrasePredicate secondPredicate =
                new RemarkContainsKeyphrasePredicate(secondPredicateKeyphrase);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        RemarkContainsKeyphrasePredicate firstPredicateCopy =
                new RemarkContainsKeyphrasePredicate(firstPredicateKeyphrase);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        //different predicate type -> returns false
        NameContainsKeyphrasePredicate copy =
                new NameContainsKeyphrasePredicate(firstPredicateKeyphrase);
        assertFalse(firstPredicate.equals(copy));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeyphrase_returnsTrue() {
        // Keyphrase is one word
        RemarkContainsKeyphrasePredicate predicate = new RemarkContainsKeyphrasePredicate("Nice");
        assertTrue(predicate.test(new PersonBuilder().withRemark("Nice").build()));

        // Keyphrase is more than one word
        predicate = new RemarkContainsKeyphrasePredicate("Nicely done");
        assertTrue(predicate.test(new PersonBuilder().withRemark("Nicely done").build()));

        // Remark does not start with matching keyphrase
        predicate = new RemarkContainsKeyphrasePredicate("mala");
        assertTrue(predicate.test(new PersonBuilder().withRemark("James cannot eat mala").build()));

        // Keyphrase is not the full word
        predicate = new RemarkContainsKeyphrasePredicate("me");
        assertTrue(predicate.test(new PersonBuilder().withRemark("James cannot eat mala").build()));

        // Not exact word and not starting with keyphrase
        predicate = new RemarkContainsKeyphrasePredicate("lice");
        assertTrue(predicate.test(new PersonBuilder().withRemark("Annoying Alice").build()));

        // Mixed-case keyphrase
        predicate = new RemarkContainsKeyphrasePredicate("tHiS dOEs NoT lOoK aCoUstIc");
        assertTrue(predicate.test(new PersonBuilder().withRemark("this does not look acoustic").build()));

        // Symbols in keyphrase
        predicate = new RemarkContainsKeyphrasePredicate("tH1S d0e$ nOt L0ok @cOu$t1c");
        assertTrue(predicate.test(new PersonBuilder().withRemark("tH1S d0e$ nOt L0ok @cOu$t1c").build()));
    }

    @Test
    public void test_nameDoesNotContainKeyphrase_returnsFalse() {

        // Non-matching keyphrase
        RemarkContainsKeyphrasePredicate predicate = new RemarkContainsKeyphrasePredicate("CS2103T");
        assertFalse(predicate.test(new PersonBuilder().withRemark("is not fun!").build()));

        Person alice = new PersonBuilder().withName("Alice").withPhone("12345")
                .withEmail("alice@email.com").withAddress("Main Street").withRemark("She is not a lice").build();

        // Keyphrase match name but does not match remark
        predicate = new RemarkContainsKeyphrasePredicate("Alice");
        assertFalse(predicate.test(alice));

        // Keyphrase match phone but does not match remark
        predicate = new RemarkContainsKeyphrasePredicate("12345");
        assertFalse(predicate.test(alice));

        // Keyphrase match email but does not match remark
        predicate = new RemarkContainsKeyphrasePredicate("alice@email.com");
        assertFalse(predicate.test(alice));

        // Keyphrase match address but does not match remark
        predicate = new RemarkContainsKeyphrasePredicate("Main");
        assertFalse(predicate.test(alice));
        predicate = new RemarkContainsKeyphrasePredicate("Street");
        assertFalse(predicate.test(alice));

    }

    @Test
    public void toStringMethod() {
        String keyphrase = "keyphrase yapyap";
        RemarkContainsKeyphrasePredicate predicate = new RemarkContainsKeyphrasePredicate(keyphrase);
        String expected = RemarkContainsKeyphrasePredicate.class.getCanonicalName() + "{keyphrase=" + keyphrase + "}";
        assertEquals(expected, predicate.toString());
    }
}
