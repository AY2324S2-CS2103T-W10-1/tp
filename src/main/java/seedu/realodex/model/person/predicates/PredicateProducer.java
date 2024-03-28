package seedu.realodex.model.person.predicates;

import seedu.realodex.logic.commands.FilterCommand;
import seedu.realodex.logic.parser.Prefix;
import seedu.realodex.logic.parser.exceptions.ParseException;
import seedu.realodex.model.person.Person;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static seedu.realodex.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.realodex.logic.parser.CliSyntax.PREFIX_REMARK;

public class PredicateProducer {
    private Map<Prefix, Function<String, Predicate<Person>>> predicateMap;

    public PredicateProducer() {
        predicateMap = new HashMap<>();
        initialize();
    }

    private void initialize() {
        predicateMap.put(PREFIX_NAME, NameContainsKeyphrasePredicate::new);
        predicateMap.put(PREFIX_REMARK, RemarkContainsKeyphrasePredicate::new);
    }

    public Predicate<Person> createPredicate(Prefix prefix, String keyphrase) throws ParseException {
        if (keyphrase == null || keyphrase.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        Function<String, Predicate<Person>> predicateCreator = predicateMap.get(prefix);
        if (predicateCreator != null) {
            return predicateCreator.apply(keyphrase.trim());
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, "Unhandled prefix: " + prefix));
    }

}
