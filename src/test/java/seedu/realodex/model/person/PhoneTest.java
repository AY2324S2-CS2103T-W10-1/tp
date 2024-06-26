package seedu.realodex.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.realodex.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
        String invalidPhone2 = " ";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone2));
    }

    @Test
    public void isValidPhone_equivalencePartitioning() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // invalid phone numbers
        assertFalse(Phone.isValidPhone("")); // empty string
        assertFalse(Phone.isValidPhone(" ")); // spaces only
        assertFalse(Phone.isValidPhone("91")); // less than 3 numbers
        assertFalse(Phone.isValidPhone("phone")); // non-numeric
        assertFalse(Phone.isValidPhone("9011p041")); // alphabets within digits
        assertFalse(Phone.isValidPhone("9312 1534")); // spaces within digits

        // valid phone numbers
        assertTrue(Phone.isValidPhone("911")); // exactly 3 numbers
        assertTrue(Phone.isValidPhone("93121534"));
        assertTrue(Phone.isValidPhone("124293842033123")); // long phone numbers

        // EP: phone number with exactly 3 digits (should be valid)
        assertTrue(Phone.isValidPhone("911"));

        // EP: phone number with more than 3 digits (should be valid)
        assertTrue(Phone.isValidPhone("93121534")); // 8 digits
        assertTrue(Phone.isValidPhone("124293842033123")); // 15 digits

        // EP: phone number with less than 3 digits (should be invalid)
        assertFalse(Phone.isValidPhone("91")); // 2 digits
        assertFalse(Phone.isValidPhone("12")); // 2 digits

        // EP: phone number with alphabets (should be invalid)
        assertFalse(Phone.isValidPhone("9011p041")); // alphabets within digits

        // EP: phone number with special characters (should be invalid)
        assertFalse(Phone.isValidPhone("9312 1534")); // spaces within digits

        // EP: empty string (should be invalid)
        assertFalse(Phone.isValidPhone("")); // empty string
    }

    @Test
    public void equals() {
        Phone phone = new Phone("999");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("999")));

        // same object -> returns true
        assertTrue(phone.equals(phone));

        // null -> returns false
        assertFalse(phone.equals(null));

        // different types -> returns false
        assertFalse(phone.equals(5.0f));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("995")));
    }

}
