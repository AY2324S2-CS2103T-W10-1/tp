package seedu.realodex.logic.parser;

import static seedu.realodex.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_REMARK;

import java.util.function.Predicate;
import java.util.stream.Stream;

import seedu.realodex.logic.commands.FilterCommand;
import seedu.realodex.logic.parser.exceptions.ParseException;
import seedu.realodex.model.person.Person;
import seedu.realodex.model.person.predicates.PredicateProducer;

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    private static final Prefix[] POSSIBLE_PREFIXES = { PREFIX_NAME, PREFIX_REMARK };
    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, POSSIBLE_PREFIXES);
        PrefixChecker prefixChecker = new PrefixChecker(argMultimap);

        checkEmptyPreamble(prefixChecker);
        checkOnlyOnePrefixPresent(prefixChecker);
        checkNoDuplicatePrefix(prefixChecker);

        Prefix presentPrefix = prefixChecker.findPresentPrefix(POSSIBLE_PREFIXES);
        String keyphrase = argMultimap.getValue(presentPrefix).get().trim();
        PredicateProducer predicateProducer = new PredicateProducer();
        Predicate<Person> predicate = predicateProducer.createPredicate(presentPrefix, keyphrase);

        return new FilterCommand(predicate);
    }
    private void checkOnlyOnePrefixPresent(PrefixChecker prefixChecker) throws ParseException {
        // Check for at least one prefix present and no empty preamble
        if (!prefixChecker.anyPrefixesPresent(POSSIBLE_PREFIXES)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
        if (prefixChecker.moreThanOnePrefixPresent(POSSIBLE_PREFIXES)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_FILTER_CONFLICT));
        }
    }

    private void checkEmptyPreamble(PrefixChecker prefixChecker) throws ParseException {
        // Check for at least one prefix present and no empty preamble
        if (!prefixChecker.checkEmptyPreamble()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
    }

    private void checkNoDuplicatePrefix(PrefixChecker prefixChecker) throws ParseException {
        prefixChecker.checkNoDuplicatePrefix();
    }

}
