package seedu.realodex.model.person.predicates;

import java.util.function.Predicate;

import seedu.realodex.commons.util.ToStringBuilder;
import seedu.realodex.model.person.Person;

/**
 * Tests that a {@code Person}'s {@code Remark} contains the keyphrase given.
 */
public class RemarkContainsKeyphrasePredicate implements Predicate<Person> {
    private final String keyphrase;

    public RemarkContainsKeyphrasePredicate(String keyphrase) {
        this.keyphrase = keyphrase;
    }

    @Override
    public boolean test(Person person) {
        String remarkInLowerCase = person.getRemark().remarkName.toLowerCase();
        String keyphraseInLowerCase = keyphrase.toLowerCase();
        return remarkInLowerCase.contains(keyphraseInLowerCase);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RemarkContainsKeyphrasePredicate)) {
            return false;
        }

        RemarkContainsKeyphrasePredicate otherNameContainsKeyphrasePredicate = (RemarkContainsKeyphrasePredicate) other;
        return keyphrase.equals(otherNameContainsKeyphrasePredicate.keyphrase);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keyphrase", keyphrase).toString();
    }
}
