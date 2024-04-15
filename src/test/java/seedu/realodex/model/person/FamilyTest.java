package seedu.realodex.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.realodex.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class FamilyTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Family(null));
    }

    @Test
    public void constructor_invalidFamily_throwsIllegalArgumentException() {
        String invalidFamily = "";
        assertThrows(IllegalArgumentException.class, () -> new Family(invalidFamily));

        String invalidFamilyWithSpaces = " ";
        assertThrows(IllegalArgumentException.class, () -> new Family(invalidFamilyWithSpaces));
    }

    @Test
    public void isValidFamily() {
        // null family size
        assertThrows(NullPointerException.class, () -> Family.isValidFamily(null));

        // invalid family sizes
        assertFalse(Family.isValidFamily("0")); // less than 1
        assertFalse(Family.isValidFamily("-1")); // less than 1
        assertFalse(Family.isValidFamily("-2")); // less than 1
        assertFalse(Family.isValidFamily("-12313231231313")); // very negative

        // valid family numbers
        assertTrue(Family.isValidFamily("2")); // more than 1
        assertTrue(Family.isValidFamily("93121")); // huge family
        assertTrue(Family.isValidFamily("12422131313131938")); // very huge family
    }

    @Test
    public void isValidFamily_invalidFamilySizes_returnsFalse() {
        assertFalse(Family.isValidFamily("")); // Empty string
        assertFalse(Family.isValidFamily(" ")); // Whitespace string
        assertFalse(Family.isValidFamily("-1")); // Negative number
        assertFalse(Family.isValidFamily("0")); // Zero
    }

    @Test
    public void isValidFamily_validFamilySizes_returnsTrue() {
        assertTrue(Family.isValidFamily("1")); // Minimum valid value
        assertTrue(Family.isValidFamily("123")); // Positive integer
        assertTrue(Family.isValidFamily("999999999999999")); // Large positive integer
        assertTrue(Family.isValidFamily("01")); // Prepended 0s
        assertTrue(Family.isValidFamily("001")); // Double prepended 0s
        assertTrue(Family.isValidFamily("0001")); // Three prepended 0s
        assertTrue(Family.isValidFamily("00000000000000000000000000001")); // Many prepended 0s
        assertTrue(Family.isValidFamily("01232132131313312313131")); // Single prepended 0 with large value


        assertFalse(Family.isValidFamily("0000000000000000000000000000")); // Many prepended 0s with no 1-9 value
        assertFalse(Family.isValidFamily("-0000000000000000000000000000")); // - + Many prepended 0s with no 1-9 value
        assertFalse(Family.isValidFamily("-0000000000000000000010000000")); // - + Many prepended 0s with some 1-9 value
    }


    @Test
    public void equals() {
        Family family = new Family("999");

        // same values -> returns true
        assertTrue(family.equals(new Family("999")));

        // same object -> returns true
        assertTrue(family.equals(family));

        // null -> returns false
        assertFalse(family.equals(null));

        // different types (integer vs float) -> returns false
        assertFalse(family.equals(5.0f));

        //different types (integer vs words) -> returns false
        assertFalse(family.equals("OKKK"));

        // different values -> returns false
        assertFalse(family.equals(new Family("995")));
    }
}
