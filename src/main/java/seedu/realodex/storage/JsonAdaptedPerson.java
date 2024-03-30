package seedu.realodex.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.realodex.commons.exceptions.IllegalValueException;
import seedu.realodex.model.person.Address;
import seedu.realodex.model.person.Birthday;
import seedu.realodex.model.person.Email;
import seedu.realodex.model.person.Family;
import seedu.realodex.model.person.HousingType;
import seedu.realodex.model.person.Income;
import seedu.realodex.model.person.Name;
import seedu.realodex.model.person.Person;
import seedu.realodex.model.person.Phone;
import seedu.realodex.model.person.Remark;
import seedu.realodex.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    private final String name;
    private final String phone;
    private final String income;
    private final String email;
    private final String address;
    private final String family;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final String housingType;
    private final String remark;
    private final String birthday;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name,
                             @JsonProperty("phone") String phone,
                             @JsonProperty("income") String income,
                             @JsonProperty("email") String email,
                             @JsonProperty("address") String address,
                             @JsonProperty("family") String family,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags,
                             @JsonProperty("housingType") String housingType,
                             @JsonProperty("remark") String remark,
                             @JsonProperty("birthday") String birthday) {
        this.name = name;
        this.phone = phone;
        this.income = income;
        this.email = email;
        this.address = address;
        this.family = family;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.housingType = housingType;
        this.remark = Objects.requireNonNullElse(remark, "");
        this.birthday = birthday;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        income = source.getIncome().toString();
        email = source.getEmail().value;
        address = source.getAddress().value;
        family = source.getFamily().toString();
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        housingType = source.getHousingType().toString();
        remark = source.getRemark().toString();
        birthday = source.getBirthday().toString();
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (income == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Income.class.getSimpleName()));
        }
        if (!Income.isValidIncome(income)) {
            throw new IllegalValueException(Income.MESSAGE_CONSTRAINTS);
        }
        final Income modelIncome = new Income(income);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (family == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Family.class.getSimpleName()));
        }
        if (!Family.isValidFamily(family)) {
            throw new IllegalValueException(Family.MESSAGE_CONSTRAINTS);
        }
        final Family modelFamily = new Family(family);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        if (housingType == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                                                          HousingType.class.getSimpleName()));
        }
        if (!HousingType.isValidHousingType(housingType)) {
            throw new IllegalValueException(HousingType.MESSAGE_CONSTRAINTS);
        }
        final HousingType modelHousingType = new HousingType(housingType);

        final Remark modelRemark = new Remark(remark);
        if (birthday == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                                                          Birthday.class.getSimpleName()));
        }
        if (!Birthday.isValidBirthday(birthday)) {
            throw new IllegalValueException(Birthday.MESSAGE_CONSTRAINTS);
        }
        final Birthday modelBirthday = new Birthday(birthday);
        return new Person(modelName, modelPhone, modelIncome, modelEmail, modelAddress, modelFamily,
                modelTags, modelHousingType, modelRemark, modelBirthday);
    }
}
