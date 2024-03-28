package seedu.realodex.logic.parser;

import java.util.stream.Stream;

import seedu.realodex.logic.Messages;
import seedu.realodex.logic.parser.exceptions.ParseException;

/**
 * A utility class to check the presence and uniqueness of prefixes in an {@code ArgumentMultimap}.
 * This class provides methods to verify if specific prefixes are provided by the user,
 * ensure no duplicate prefixes are used, and more.
 */
public class PrefixChecker {

    private final ArgumentMultimap argumentMultimap;

    /**
     * Constructs a {@code PrefixChecker} with the provided {@code ArgumentMultimap}.
     *
     * @param argumentMultimap The {@code ArgumentMultimap} to be used for prefix checks.
     */
    public PrefixChecker(ArgumentMultimap argumentMultimap) {
        this.argumentMultimap = argumentMultimap;
    }

    /**
     * Checks if any of the provided prefixes are present in the {@code ArgumentMultimap}.
     *
     * @param prefixes The prefixes to check for presence.
     * @return {@code true} if any of the prefixes are present, {@code false} otherwise.
     */
    public boolean anyPrefixesPresent(Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(this::isPrefixPresent);
    }

    /**
     * Determines if more than one of the provided prefixes are present in the {@link ArgumentMultimap}.
     *
     * @param prefixes The prefixes to check.
     * @return {@code true} if more than one prefix is present, {@code false} otherwise.
     */

    public boolean moreThanOnePrefixPresent(Prefix... prefixes) {
        return Stream.of(prefixes).filter(this::isPrefixPresent).count() > 1;
    }

    /**
     * Checks if the preamble (text before the first valid prefix) of the {@code ArgumentMultimap} is empty.
     *
     * @return {@code true} if the preamble is empty, {@code false} otherwise.
     */
    public boolean checkEmptyPreamble() {
        return argumentMultimap.getPreamble().isEmpty();
    }

    /**
     * Verifies that no duplicate prefixes are present for the provided prefixes.
     * A duplicate prefix is defined as the same prefix being used multiple times with different values.
     *
     * @param prefixes The prefixes to check for duplicates.
     * @throws ParseException if duplicate prefixes are found.
     */
    public void checkNoDuplicatePrefix(Prefix...prefixes) throws ParseException {
        Prefix[] duplicatedPrefixes = Stream.of(prefixes).distinct()
                .filter(prefix -> argumentMultimap.containsPrefix(prefix)
                        && argumentMultimap.getAllValues(prefix).size() > 1)
                .toArray(Prefix[]::new);

        if (duplicatedPrefixes.length > 0) {
            throw new ParseException(Messages.getErrorMessageForDuplicatePrefixes(duplicatedPrefixes));
        }
    }

    /**
     * Checks if a specific prefix is present in the {@code ArgumentMultimap}.
     *
     * @param prefix The prefix to check for presence.
     * @return {@code true} if the prefix is present, {@code false} otherwise.
     */
    public boolean isPrefixPresent(Prefix prefix) {
        return argumentMultimap.containsPrefix(prefix);
    }

    /**
     * Finds and returns the first prefix that is present in the {@code ArgumentMultimap}
     * from the given list of prefixes.
     *
     * @param prefixes The prefixes to search through.
     * @return The first present prefix, or {@code null} if none are present.
     */
    public Prefix findPresentPrefix(Prefix...prefixes) {
        for (Prefix prefix : prefixes) {
            if (isPrefixPresent(prefix)) {
                return prefix;
            }
        }
        return null;
    }
}
