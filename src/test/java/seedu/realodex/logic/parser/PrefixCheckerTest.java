package seedu.realodex.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.realodex.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.realodex.logic.parser.exceptions.ParseException;

public class PrefixCheckerTest {

    @Test
    public void anyPrefixesPresent_withMultiplePrefixes_returnsTrue() {
        String argsString = " n/John Doe e/johnd@example.com ";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString, PREFIX_NAME, PREFIX_EMAIL);
        PrefixChecker prefixChecker = new PrefixChecker(argMultimap);

        assertTrue(prefixChecker.anyPrefixesPresent(PREFIX_NAME, PREFIX_EMAIL));
    }

    @Test
    public void anyPrefixesPresent_withNoPrefixes_returnsFalse() {
        String argsString = " ";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString, PREFIX_NAME, PREFIX_EMAIL);
        PrefixChecker prefixChecker = new PrefixChecker(argMultimap);

        assertFalse(prefixChecker.anyPrefixesPresent(PREFIX_NAME, PREFIX_EMAIL));
    }
    @Test
    public void anyPrefixesPresent_withNoRelevantPrefixes_returnsFalse() {
        String argsString = " n/John Doe ";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString, PREFIX_NAME);
        PrefixChecker prefixChecker = new PrefixChecker(argMultimap);

        assertFalse(prefixChecker.anyPrefixesPresent(PREFIX_PHONE, PREFIX_ADDRESS));
    }

    @Test
    public void moreThanOnePrefixPresent_withMultiplePrefixes_returnsTrue() {
        String argsString = " n/John Doe e/johnd@example.com ";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString, PREFIX_NAME, PREFIX_EMAIL);
        PrefixChecker prefixChecker = new PrefixChecker(argMultimap);

        assertTrue(prefixChecker.moreThanOnePrefixPresent(PREFIX_NAME, PREFIX_EMAIL));
    }

    @Test
    public void moreThanOnePrefixPresent_withSinglePrefix_returnsFalse() {
        String argsString = " n/John Doe ";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString, PREFIX_NAME);
        PrefixChecker prefixChecker = new PrefixChecker(argMultimap);

        assertFalse(prefixChecker.moreThanOnePrefixPresent(PREFIX_NAME, PREFIX_EMAIL));
    }

    @Test
    public void checkEmptyPreamble_withEmptyPreamble_returnsTrue() {
        String argsString = " n/John Doe";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString, PREFIX_NAME);
        PrefixChecker prefixChecker = new PrefixChecker(argMultimap);

        assertTrue(prefixChecker.checkEmptyPreamble());
    }


    @Test
    public void checkEmptyPreamble_withNonEmptyPreamble_returnsFalse() {
        String argsString = "Random Preamble n/John Doe ";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString, PREFIX_NAME);
        PrefixChecker prefixChecker = new PrefixChecker(argMultimap);

        assertFalse(prefixChecker.checkEmptyPreamble());
    }

    @Test
    public void checkNoDuplicatePrefix_noDuplicates_doesNotThrowException() {
        String argsString = " n/John Doe e/johnd@example.com ";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString, PREFIX_NAME, PREFIX_EMAIL);
        PrefixChecker prefixChecker = new PrefixChecker(argMultimap);

        assertDoesNotThrow(() -> prefixChecker.checkNoDuplicatePrefix(PREFIX_NAME, PREFIX_EMAIL));
    }

    @Test
    public void checkNoDuplicatePrefix_oneDuplicate_throwsParseException() {
        String argsString = " n/John Doe n/Jane Doe ";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString, PREFIX_NAME);
        PrefixChecker prefixChecker = new PrefixChecker(argMultimap);

        assertThrows(ParseException.class, () -> prefixChecker.checkNoDuplicatePrefix(PREFIX_NAME));
    }

    @Test
    public void checkNoDuplicatePrefix_multipleDuplicates_throwsParseException() {
        String argsString = " n/John Doe e/johnd@example.com n/Jane Doe e/janed@example.com ";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsString, PREFIX_NAME, PREFIX_EMAIL);
        PrefixChecker prefixChecker = new PrefixChecker(argMultimap);

        assertThrows(ParseException.class, () -> prefixChecker.checkNoDuplicatePrefix(PREFIX_NAME, PREFIX_EMAIL));
    }

}
