package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Family;
import seedu.address.model.person.Income;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.remark.Remark;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"),
                       new Phone("87438807"),
                       new Income("1000"),
                       new Email("alexyeoh@example" + ".com"),
                       new Address("Blk 30 Geylang Street 29, #06-40"),
                       new Family("20"),
                       getRemarkSet("friends")),
            new Person(new Name("Bernice Yu"),
                       new Phone("99272758"),
                       new Income("1000"),
                       new Email("berniceyu" + "@example.com"),
                       new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                       new Family("20"),
                getRemarkSet("colleagues", "friends")),
            new Person(new Name("Charlotte Oliveiro"),
                       new Phone("93210283"),
                       new Income("1000"),
                       new Email("charlotte@example.com"),
                       new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                       new Family("20"),
                getRemarkSet("neighbours")),
            new Person(new Name("David Li"),
                       new Phone("91031282"),
                       new Income("1000"),
                       new Email("lidavid@example" + ".com"),
                       new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                       new Family("20"),
                getRemarkSet("family")),
            new Person(new Name("Irfan Ibrahim"),
                       new Phone("92492021"),
                       new Income("1000"),
                       new Email("irfan@example" + ".com"),
                       new Address("Blk 47 Tampines Street 20, #17-35"),
                       new Family("20"),
                getRemarkSet("classmates")),
            new Person(new Name("Roy Balakrishnan"),
                       new Phone("92624417"),
                       new Income("1000"),
                       new Email("royb" + "@example.com"),
                       new Address("Blk 45 Aljunied Street 85, #11-31"),
                       new Family("20"),
                getRemarkSet("colleagues"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a remark set containing the list of strings given.
     */
    public static Set<Remark> getRemarkSet(String... strings) {
        return Arrays.stream(strings)
                .map(Remark::new)
                .collect(Collectors.toSet());
    }
}
