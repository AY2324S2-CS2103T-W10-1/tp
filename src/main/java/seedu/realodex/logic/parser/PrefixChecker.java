package seedu.realodex.logic.parser;

import seedu.realodex.logic.Messages;
import seedu.realodex.logic.parser.exceptions.ParseException;

import java.util.stream.Stream;

import static seedu.realodex.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_REMARK;
public class PrefixChecker {

    private final ArgumentMultimap argumentMultimap;

    public PrefixChecker(ArgumentMultimap argumentMultimap) {
        this.argumentMultimap = argumentMultimap;
    }

    public boolean anyPrefixesPresent(Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(this::isPrefixPresent);
    }

    public boolean moreThanOnePrefixPresent(Prefix... prefixes) {
        return Stream.of(prefixes).filter(this::isPrefixPresent).count() > 1;
    }

    public boolean checkEmptyPreamble() {
        return argumentMultimap.getPreamble().isEmpty();
    }

    public void checkNoDuplicatePrefix(Prefix...prefixes) throws ParseException {
        Prefix[] duplicatedPrefixes = Stream.of(prefixes).distinct()
                .filter(prefix -> argumentMultimap.containsPrefix(prefix) && argumentMultimap.getAllValues(prefix).size() > 1)
                .toArray(Prefix[]::new);

        if (duplicatedPrefixes.length > 0) {
            throw new ParseException(Messages.getErrorMessageForDuplicatePrefixes(duplicatedPrefixes));
        }
    }

    public boolean isPrefixPresent(Prefix prefix) {
        return argumentMultimap.containsPrefix(prefix);
    }

    public Prefix findPresentPrefix(Prefix...prefixes) {
        for (Prefix prefix : prefixes) {
            if (isPrefixPresent(prefix)) {
                return prefix;
            }
        }
        return null;
    }
}
