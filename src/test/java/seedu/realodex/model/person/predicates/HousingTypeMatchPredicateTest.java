package seedu.realodex.model.person.predicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.realodex.model.person.HousingType;
import seedu.realodex.testutil.PersonBuilder;



public class HousingTypeMatchPredicateTest {

    @Test
    public void equals() {
        HousingType housingTypeOne = new HousingType("hdb");
        HousingType housingTypeTwo = new HousingType("Good Class Bungalow");

        HousingTypeMatchPredicate firstPredicate = new HousingTypeMatchPredicate(housingTypeOne);
        HousingTypeMatchPredicate secondPredicate = new HousingTypeMatchPredicate(housingTypeTwo);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        HousingTypeMatchPredicate firstPredicateCopy =
                new HousingTypeMatchPredicate(housingTypeOne);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_housingTypeMatch_returnsTrue() {
        // Housing Type matches HDB
        HousingTypeMatchPredicate predicate = new HousingTypeMatchPredicate(new HousingType("hdb"));
        assertTrue(predicate.test(new PersonBuilder().withHousingType("hdb").build()));

        // Housing Type matches HDB
        predicate = new HousingTypeMatchPredicate(new HousingType("Condominium"));
        assertTrue(predicate.test(new PersonBuilder().withHousingType("Condominium").build()));

        // Housing Type matches HDB
        predicate = new HousingTypeMatchPredicate(new HousingType("Good Class Bungalow"));
        assertTrue(predicate.test(new PersonBuilder().withHousingType("Good Class Bungalow").build()));

        // Housing Type matches HDB
        predicate = new HousingTypeMatchPredicate(new HousingType("Landed Property"));
        assertTrue(predicate.test(new PersonBuilder().withHousingType("Landed Property").build()));

    }

    @Test
    public void test_housingTypeDoesNotMatch_returnsFalse() {

        // Housing Type does not match HDB
        HousingTypeMatchPredicate predicate = new HousingTypeMatchPredicate(new HousingType("hdb"));
        assertFalse(predicate.test(new PersonBuilder().withHousingType("Condominium").build()));

        // Housing Type matches Condominium
        predicate = new HousingTypeMatchPredicate(new HousingType("Condominium"));
        assertFalse(predicate.test(new PersonBuilder().withHousingType("Good Class Bungalow").build()));

        // Housing Type matches Good Class Bungalow
        predicate = new HousingTypeMatchPredicate(new HousingType("Good Class Bungalow"));
        assertFalse(predicate.test(new PersonBuilder().withHousingType("Landed Property").build()));

        // Housing Type matches Landed Property
        predicate = new HousingTypeMatchPredicate(new HousingType("Landed Property"));
        assertFalse(predicate.test(new PersonBuilder().withHousingType("hdb").build()));

    }

    @Test
    public void toStringMethod() {
        String housingTypeString = "hdb";
        HousingType housingType = new HousingType(housingTypeString);
        HousingTypeMatchPredicate predicate = new HousingTypeMatchPredicate(housingType);
        String expected = HousingTypeMatchPredicate.class.getCanonicalName() + "{Housing Type=" + housingType + "}";
        assertEquals(expected, predicate.toString());
    }
}
