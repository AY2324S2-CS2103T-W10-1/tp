package seedu.realodex.logic.parser;

import static seedu.realodex.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_REMARK;

import java.util.function.Predicate;

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

    /**
     * Checks if exactly one prefix is present in the input arguments. This method ensures that
     * the command includes at least one recognized prefix and that no more than one is specified,
     * to maintain the command's specificity.
     *
     * @param prefixChecker the {@link PrefixChecker} used to validate the presence of prefixes.
     * @throws ParseException if no prefixes are present or more than one prefix is found.
     */
    private void checkOnlyOnePrefixPresent(PrefixChecker prefixChecker) throws ParseException {
        // Check for at least one prefix present and no empty preamble
        if (!prefixChecker.anyPrefixesPresent(POSSIBLE_PREFIXES)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
        if (prefixChecker.moreThanOnePrefixPresent(POSSIBLE_PREFIXES)) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_FILTER_CONFLICT
            ));
        }
    }

    /**
     * Checks if the preamble (text before the first valid prefix) is empty. This method ensures
     * that there are no extraneous text before the command's key information, adhering to the
     * command's syntax requirements.
     *
     * @param prefixChecker the {@link PrefixChecker} used to examine the preamble.
     * @throws ParseException if the preamble is not empty.
     */
    private void checkEmptyPreamble(PrefixChecker prefixChecker) throws ParseException {
        // Check for at least one prefix present and no empty preamble
        if (!prefixChecker.checkEmptyPreamble()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Checks for duplicate prefixes in the input arguments. Duplicate prefixes can lead to ambiguity
     * in the command's intention, so this method ensures that a recognized prefix is used at most
     * once in the command.
     *
     * @param prefixChecker the {@link PrefixChecker} used to detect duplicate prefixes.
     * @throws ParseException if duplicate prefixes are found.
     */
    private void checkNoDuplicatePrefix(PrefixChecker prefixChecker) throws ParseException {
        prefixChecker.checkNoDuplicatePrefix(POSSIBLE_PREFIXES);
    }

}
