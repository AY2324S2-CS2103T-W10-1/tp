package seedu.realodex.logic.parser;

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

    public void checkNoDuplicatePrefix() throws ParseException {
        argumentMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_REMARK);
    }

    //this will always return false for empty `r/` field
    public boolean isPrefixPresent(Prefix prefix) {
        if (prefix.equals(PREFIX_REMARK)) {
            return argumentMultimap.getValue(prefix).map(value -> !value.trim().isEmpty()).orElse(false);
        } else {
            return argumentMultimap.getValue(prefix).isPresent();
        }
    }

    //this will always return false for empty `r/` field
    public Prefix findPresentPrefix(Prefix...prefixes) {
        for (Prefix prefix : prefixes) {
            if (isPrefixPresent(prefix)) {
                return prefix;
            }
        }
        return null;
    }
}
