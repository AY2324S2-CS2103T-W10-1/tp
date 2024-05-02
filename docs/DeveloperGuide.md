---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# Realodex Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**
Some of the NFRs are inspired by [Pawfection](https://ay2324s1-cs2103t-f08-3.github.io/tp/DeveloperGuide.html#appendix-instructions-for-manual-testing)
Some of the Sequence Diagrams inspired by [PoochPlanner](https://github.com/AY2324S2-CS2103T-W10-2/tp/blob/master/docs/DeveloperGuide.md)


--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](#setting-up-getting-started).

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

<div style="page-break-after: always;"></div>

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `LogicManager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

Realodex has implemented a dynamic delete function that either deletes user by index or by their name. Here we illustrate
deletion by index for brevity.

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `RealodexParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `RealodexParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `RealodexParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)


<puml src="diagrams/PersonClass.puml" width="800" />

<br>
<br>

In the context of our developer guide, the provided class diagram illustrates the structure of the `Person` class,
encompassing essential attributes that real-estate agents would require from their clients for official documents and for better understanding of their requirements. 
This detailed depiction allows developers
to grasp the internal composition of the `Person` entity
without needing to replicate `Person` in higher-level model interactions without cluttering too much low-level info.

<puml src="diagrams/ModelClassUpdated.puml" width="500"></puml>

The `Model` component,

* stores the Realodex contact data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div style="page-break-after: always;"></div>

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the Realodex, which `Person` references. This allows Realodex to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="800"></puml>

</box>

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both Realodex data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `RealodexStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.realodex.commons` package.

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### <u>Overall Add feature</u>

#### Description

The `add` feature, that was morphed from the original AddressBook3, allows users to add clients along with their critical personal information, as well as optional remarks and birthday information.

#### Key Components
- `AddCommand`: Executes the addition operation based on the user's input.
- `AddCommandParser`: Parses user input to create an `AddCommand` object.
- `Person`: Represents a person in Realodex, encapsulating their personal information.
- `ModelManager`: Implements the `Model` interface and contains the internal list of persons.
- `LogicManager`: Invokes the `AddCommand` to execute the addition operation.
- `RealodexParser`: Creates an `AddCommand` object based on the user input.

#### `AddCommand` Implementation Sequence Diagram
The sequence diagram below illustrates the process of adding a person into Realodex.
<puml src="diagrams/add/AddCommandSequenceDiagram.puml" width="1000" />

#### Component Interaction Details
1. The user executes the command `add n/John Doe p/98765432 i/20000 e/johnd@example.com a/311, Clementi Ave 2, #02-25 f/4 t/buyer t/seller h/HDB r/Has 3 cats b/01May2009`, intending to add a person with the specified details.
2. The `AddCommandParser` interprets the input.
3. An `AddCommand` object is created.
4. The `LogicManager` invokes the execute method of AddCommand.
5. The execute method of `AddCommand` invokes the `addPerson` method in `Model` property to create new contact with the new `Person` object.
6. The execute method of `AddCommand` returns a `CommandResult` object which stores the data regarding the completion of the `AddCommand`.
7. The UI reflects this new list with added `Person`.

<div style="page-break-after: always;"></div>

#### Example Usage Scenario
1. The user launches the application.
2. The user inputs `add n/John Doe p/98765432 i/20000 e/johnd@example.com a/311, Clementi Ave 2, #02-25 f/4 t/buyer t/seller h/HDB r/Has 3 cats b/01May2009`, intending to add a person with the specified details.
3. The UI reflects this new list with added person John Doe.

#### Design Considerations

**Compulsory fields** include: Name, Phone Number, Income, Email, Address, Family Size, Buyer / Seller Tag, Housing Type.<br>
* We chose these fields to be compulsory as they are critical for the real estate agent to make informed decisions and recommendations for their clients.
* While they may not be **absolutely necessary** for all clients, we believe that the cost of missing out on these fields outweighs the hassle of making them compulsory.
* For example, missing out on the family size may not be critical for a buyer who is single and is searching for a bachelor pad, but is critical for a family of 7 who needs a large enough house for all 7 of them.
* These fields are also made necessary for both buyers and sellers, even though they may be intuitively be more suitable for either seller or buyer. 
* For example, while the income may not be absolutely necessary for a seller, it is important information if the agent would like to make recommendations for future properties for own-stay or investment purposes.

**Optional fields** include: Remark, Birthday
A real estate agent may not have any remark for a client yet, and wishes to leave Remark empty.
A real estate agent may also only want to track birthdays of their esteemed clients, and wishes to not include Birthday for the rest.

**Field Constraints**
Specific field constraints are described below. They are designed with the users in mind.
* `NAME`:
  * Must be an alphanumeric string
* `PHONE`:
  * Should only contain numbers, and should be at least 3 digits long. This is to account for phone numbers of all types and need not necessarily be from Singapore. We avoid the '+' sign as it is not necessary and can be replaced with the international prefix
* `INCOME`:
  * Income should be an integer and should be at least 0. Decimals provide an unnecessary level of precision.
* `EMAIL`:
  * Emails should be of the format local-part@domain and adhere to the following constraints:
  1. The local-part should only contain alphanumeric characters and these special characters, excluding the parentheses, (+_.-).
  2. The local-part may not start or end with any special characters.
  3. This is followed by a '@' and then a domain name. The domain name is made up of domain labels separated by periods.\
     The domain name must:
    * end with a domain label at least 2 characters long
    * have each domain label start and end with alphanumeric characters
    * have each domain label consist of alphanumeric characters, separated only by hyphens, if any.
* `ADDRESS`:
  * Represented as String
* `FAMILY`:
  * Should be an integer greater than 1.
* `TAG`:
  * Should be restricted to case-insensitive "buyer" or "seller" using enums.
* `HOUSINGTYPE`: housing type a buyer wants or housing type a seller is selling
  * Should be restricted to "HDB", "CONDOMINIUM", "LANDED PROPERTY", "GOOD CLASS BUNGALOW" (case-insensitive) using enums.
  * Only one housing type is allowed.
* `REMARK`:
  * Represented as a String
  * If remark is not specified, an empty string is used for representation 
* `BIRTHDAY`:
  * Implemented as an Optional Date, making use of Java SimpleDateFormat and Calendar classes for input and output validation.

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

### <u>Overall Sort feature</u>

#### Description
The `sort` feature, introduced in version 1.4, allows users to arrange clients based on their upcoming birthday proximity, which is determined by the number of days until their next birthday relative to the current date.

#### Key Components
- `SortCommand`: Executes the sorting operation based on the upcoming birthdays of persons.
- `SortCommandParser`: Parses user input to create a `SortCommand` object.
- `BirthdayComparator`: Compares two `Person` objects based on their birthdays to facilitate sorting.
- `LogicManager`: Invokes the `SortCommand` to execute the sorting operation.
- `RealodexParser`: Creates a `SortCommand` object based on the user input.
- `UniquePersonsList`: Stores the list of unique persons in Realodex.
- `ModelManager`: Implements the `Model` interface and contains the internal list of persons.

#### `SortCommand` Architecture

`SortCommand` extensively interacts with the `Model` component to facilitate list sorting during execution. Consequently, `SortCommand` depends on `ModelManager`, which is an implementation of the `Model` interface. This dependency arises because `ModelManager` instances are passed as arguments in the `public CommandResult execute(Model model) throws CommandException` method of `SortCommand`. For brevity, interactions beyond the `Model` layer are not detailed.
<puml src="diagrams/sort/SortCommandClassDiagram-Model.puml" width="300" />

<div style="page-break-after: always;"></div>

#### `SortCommand` Initialization Sequence Diagram

To implement the sorting functionality, the `LogicManager` component parses the user's input command. Subsequently, it forwards the parsed command text to the `RealodexParser`. The RealodexParser is responsible for creating an instance of the `SortCommand`, encapsulating the logic for sorting clients based on their upcoming birthdays.

The sequence diagram below illustrates the process of creating a sort operation through the `Logic` component:
<puml src="diagrams/sort/SortSequenceDiagram-Logic.puml" width="800" />

<div style="page-break-after: always;"></div>

#### `SortCommand` Implementation Sequence Diagram

1. **Model Retrieval**: The method begins by retrieving the `Realodex` component from the provided `Model` object using the `getRealodex` method.

2. **List Copying**: Next, the method obtains a duplicate of the internal list of unique persons stored within the `Realodex` component. This is achieved by calling the `getCopyOfInternalListOfUniquePersonsList` method.

3. **Sorting**: The method proceeds to sort the copied list of persons using a `BirthdayComparator` object. This comparator compares the birthdays of two persons, ensuring that the list is arranged in ascending order based on upcoming birthdays.

4. **List Update**: After sorting the copied list, the method updates the internal list of persons within the `Realodex` component with the sorted list. This is accomplished by calling the `setPersons` method of the `Realodex` component.

5. **Command Result Creation**: Finally, the method returns a `CommandResult` object with a success message indicating the completion of the sorting operation. The success message is defined by the constant `MESSAGE_SUCCESS`.

6. **Exception Handling**: The method declares a `throws CommandException`, indicating that it may throw a `CommandException` if an error occurs during execution. However, the method implementation does not contain explicit error handling logic.
<puml src="diagrams/sort/SortSequenceDiagram-Model.puml" width="1000" />

<div style="page-break-after: always;"></div>

#### Implementation of `BirthdayComparator`
The provided comparator compares two `Person` objects based on their birthdays.

1. If `o1` has an unspecified birthday (i.e., its birthday is blank), it is considered to come after `o2`.
2. If `o2` has an unspecified birthday (i.e., its birthday is blank), it is considered to come before `o1`.
3. If both `o1` and `o2` have specified birthdays, the comparator compares them based on the number of days until their next birthday.

    - If `o1`'s birthday is closer (fewer days until the next birthday) than `o2`'s birthday, `o1` is considered to come before `o2`.
    - If `o2`'s birthday is closer (fewer days until the next birthday) than `o1`'s birthday, `o2` is considered to come before `o1`.
    - If both `o1` and `o2` have the same number of days until their next birthday, their order remains unchanged.



```
    public int compare(Person o1, Person o2) {
        if (o1.getBirthday().toString().isBlank()) {
            return 1; // o1 has an unspecified birthday, so it comes after o2
        }
        if (o2.getBirthday().toString().isBlank()) {
            return -1; // o2 has an unspecified birthday, so it comes before o1
        }
        return o1.getBirthday().getDaysUntilBirthday().compareTo(o2.getBirthday().getDaysUntilBirthday());
    }
```

#### Component Interaction Details
1. The user executes the command `sort`, intending to sort the list of persons based on their upcoming birthdays.
2. The `SortCommandParser` interprets the input.
3. A `SortCommand` object is created.
4. The `LogicManager` invokes the execute method of `SortCommand`.
5. The execute method of `SortCommand` sorts the list of persons based on their upcoming birthdays.
6. The UI reflects the updated list with persons sorted based on their upcoming birthdays.

#### Example Usage Scenario
1. The user inputs `sort`, intending to sort the list of persons based on their upcoming birthdays.
2. The UI reflects the updated list with persons sorted based on their upcoming birthdays.
3. The user can now view the list of persons arranged in ascending order based on their upcoming birthdays.

<div style="page-break-after: always;"></div>

#### Design considerations:


Introducing the ability to sort clients based on criteria other than "Today" opens up a world of possibilities for users seeking more tailored and flexible search options.

**Pros:**
- Enhanced Flexibility: By accommodating sorting based on various criteria such as birthdays relative to holidays like Christmas, users gain the ability to prioritize their interactions based on unique contexts or preferences.

- Improved Relevance:
  Sorting by alternate criteria allows users to surface clients who are most relevant to specific events or occasions,
  ensuring that they can engage with individuals in a timely and contextually appropriate manner.

- Personalization: This feature empowers users to personalize their client management experience, aligning it more closely with their individual needs and preferences. This can lead to greater user satisfaction and efficiency.

**Cons:**
- Learning Curve: Introducing new command formats may require users to adapt to changes in the interface or workflow. While this can initially pose a challenge, clear documentation and intuitive design can help mitigate the learning curve.

- Complexity: Adding additional sorting options may increase the complexity of the system, potentially leading to a more cluttered user interface or backend implementation. Careful design and prioritization are necessary to maintain usability.

- Maintenance Overhead: Supporting multiple sorting criteria introduces additional maintenance overhead, requiring ongoing updates and adjustments to ensure continued functionality and relevance.

In summary, while introducing sorting by criteria other than "Today" may come with some initial challenges, the benefits of enhanced flexibility, relevance, and personalization can outweigh these concerns, ultimately leading to a more powerful and user-friendly client management system.

<div style="page-break-after: always;"></div>

#### [Proposed] `Sort` Features Beyond v1.4

The `sort` functionality is poised for exciting developments in the future. Although initially focused on sorting
clients based on their birthdays to bolster client relationships in a **breadth-first development** approach,
we have ambitious plans to extend this feature to other fields. With clients having diverse attributes
such as income and housing preferences, implementing `sort` for these fields is definitely on our roadmap.

#### Proposed `Sort` Command Initialisation Sequence Diagram

To enhance the sorting functionality, we're introducing the capability to sort based on various fields specified by the user.
The proposed command format is `sort field`, where `field` represents the attribute by which the sorting will be performed.
For instance, users can execute commands like `sort birthday`, `sort income`, or `sort housepref`.

The following sequence diagram illustrates the process
of introducing this new `sort` operation through the `Logic` component,
with user-specified fields.

The ref frame sequence diagram is omitted here,
as it's similar to the [sorting](#sortcommand-implementation-sequence-diagram) sequence illustrated earlier.
Instead of using the `BirthdayComparator`,
we'll utilize different comparators based on the user's specified field, such as `IncomeComparator`.

<puml src="diagrams/sort/NewSortSequenceDiagram-Logic.puml" width="1000" />


--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

### <u>Overall Filter feature</u>

#### Description

This feature enables users to filter and view persons in Realodex based on specified criteria such as name, remark, tag, birthday, and housing type.<br>
There are five ways to filter:
1. Filter by Name
2. Filter by Remark
3. Filter by Tag
4. Filter by Birthday
5. Filter by Housing Type

#### Key Components
- `FilterCommand`: Executes the filtering operation based on a provided predicate that encapsulates the filtering criteria.
- `PrefixChecker`: Ensures the syntactic correctness of user inputs by verifying command prefixes.
Detects command format violations, and facilitates clear error messaging.
- `FilterCommandParser`: Parses user input into a FilterCommand by identifying the filtering field and keyphrase.
- `PredicateProducer`: Generates specific predicates based on the identified field and keyphrase.
- `Predicates`: `NameContainsKeyphrasePredicate`, `RemarkContainsKeyphrasePredicate`, `TagsMatchPredicate`, `BirthdayIsInMonthPredicate`, and `HousingTypeMatchPredicate`
that determine if a person's attributes match the user-defined criteria.

#### Filter Command Architecture
<puml src="diagrams/filter/FilterFeatureArchitecture.puml" width="1000" />

<div style="page-break-after: always;"></div>

#### Filter Command Sequence Diagram
The sequence diagram below illustrates the process of creating a filter operation through the `Logic` component:

<puml src="diagrams/filter/FilterSequenceDiagram.puml" width="1000" />

### 1. Filter by Name feature

#### Description

The Filter by Name feature allows users to filter the list of persons in Realodex based on their names.
This is implemented using the `NameContainsKeyphrasePredicate` that checks if a person's name contains the keyphrase provided by the user.

#### Component Interaction Details
1. The user executes the command `filter n/John`, intending to filter out persons whose names contain "John".
2. The FilterCommandParser interprets the input, creating a FilterCommand with the `NameContainsKeyphrasePredicate`.
3. The `FilterCommand` applies the `NameContainsKeyphrasePredicate` predicate, updating the filtered person list to only include those whose names contain "John".
4. The UI reflects this filtered list.

#### Example Usage Scenario
1. The user inputs `filter n/John`, intending to filter the list of persons to only include those whose names contain "John".
2. The UI reflects the filtered list with persons whose names contain "John".

<div style="page-break-after: always;"></div>

#### Design considerations

Aspect: Handling of partial names

**Alternative 1 (current choice): Allow partial matches of names.**

> For example, `filter n/Jo` returns persons with names "John", "Bonjovi", etc.

Pros: More flexible search.

Cons: May return too many results for very short keyphrases.

**Alternative 2: Require exact matches.**

> For example, `filter n/Jo` only returns persons with names "Jo"

Pros: Precise filtering.

Cons: Less flexible; users must remember exact names.

### 2. Filter by Remark Feature

#### Description

The Filter by Remark feature allows users to filter the list of persons in Realodex based on the remarks associated with them.
This is implemented using the `RemarkContainsKeyphrasePredicate` that checks if a person's remarks includes the keyphrase provided by the user.

#### Component Interaction Details
1. The user executes the command `filter r/Loves coding`, intending to filter out to filter out persons whose remarks include "Loves coding".
2. The `FilterCommandParser` interprets the input, creating a FilterCommand with a `RemarkContainsKeyphrasePredicate`.
3. The `FilterCommand` applies the `RemarkContainsKeyphrasePredicate` predicate, updating the filtered person list to only include those whose remarks contain "Loves coding".
4. The UI reflects this filtered list.

#### Example Usage Scenario
1. The user inputs `filter r/Loves coding`, intending to filter the list of persons to only include those whose remarks include "Loves coding".
2. The UI reflects the filtered list with persons whose remarks include "Loves coding".

#### Design considerations

Aspect: Handling of partial names

**Alternative 1 (current choice): Allow partial matches for remarks.**

> For example, `filter r/love` returns persons with remarks like "Loves coding" and "Has a lovely dog".

Pros: More flexible search.

Cons: May return too many results for common keyphrases.

**Alternative 2: Require exact matches.**

> For example, `filter r/Loves Coding` only returns persons with the exact remark "Loves coding".

Pros: Ensures that only persons with specific remarks are listed, reducing clutter.

Cons: Extremely limiting. Users must remember exact remarks.

<div style="page-break-after: always;"></div>

### 3. Filter by Tag Feature

#### Description

The Filter by Tag feature allows users to filter the list of persons in Realodex based on their tags.
This is implemented using the `TagsMatchPredicate` that checks whether a person's tags match the tag(s) specified by the user.

#### Component Interaction Details
1. The user executes the command `filter t/Buyer`, intending to filter out to filter out persons who are tagged as "Buyer".
2. The `FilterCommandParser` interprets the input, creating a FilterCommand with a `TagsMatchPredicate`.
3. The `FilterCommand` applies the `TagsMatchPredicate` predicate, updating the filtered person list to only include those who are tagged as "Buyer".
4. The UI reflects this filtered list.

#### Example Usage Scenario
1. The user inputs `filter t/Buyer`, intending to filter the list of persons to only include those who are tagged as "Buyer".
2. The UI reflects the filtered list with persons who are tagged as "Buyer", including those tagged as "Buyer" and "Seller".

#### Design considerations
**Alternative 1 (current choice): Allow inclusion of persons with matching tags, irrespective of other tags.**

> For example, `filter t/Buyer` returns persons tagged as "Buyer", including those tagged as "Buyer" and "Seller".

Pros: Inclusive Search that returns anyone with the "Buyer" tag, increasing breadth of search outcomes.

Cons: Reduced precision in cases where users want to find persons exclusively tagged with this specific tag.

**Alternative 2: All tag inputs must strictly match without.**

> For example, `filter t/Buyer` only returns persons tagged solely as "Buyer" and excludes persons tagged as both "Buyer" and "Seller".

Pros: Increase precision of search results for targeted searches.

Cons: Excludes potentially relevant persons who carry the specified tag alongside others.

### 4. Filter by Birthday Feature

#### Description

The Filter by Birthday feature allows users to filter the list of persons in Realodex based on their birthday month.
This is implemented using the `BirthdayIsInMonthPredicate` that checks whether a person's birthday matches the month specified by the user.

#### Component Interaction Details
1. The user executes the command `filter b/Jan`, intending to filter out to filter out persons with birthdays in January.
2. The `FilterCommandParser` interprets the input, creating a FilterCommand with a `BirthdayIsInMonthPredicate`.
3. The `FilterCommand` applies the `BirthdayIsInMonthPredicate` predicate, updating the filtered person list to only include those with birthday in January.
4. The UI reflects this filtered list.

#### Example Usage Scenario
1. The user inputs `filter b/Jan`, intending to filter the list of persons to only include those with birthdays in January.
2. The UI reflects the filtered list with persons who have birthdays in January.

<div style="page-break-after: always;"></div>

#### Design considerations
**Alternative 1 (current choice): Filter by birthday month.**

> For example, `filter b/Jan` returns all persons born in January, regardless of the day.

Pros: Simplifies the birthday search process, making it easier to remember and use.

Cons: Less precise, might include unwanted results from the entire month.

**Alternative 2: Require exact birthday matches.**

> For example, `filter b/1Jan` only returns persons born on January 1st.

Pros: Increase precision of search results, finding persons with specific birth dates.

Cons: Requires exact date knowledge, which may not always be available or remembered by users.

### 5. Filter by Housing Type Feature

#### Description

The Filter by Housing Type feature allows users to filter the list of persons in Realodex based on their preferred housing type.
This is implemented using the `HousingTypeMatchPredicate` that checks whether a person's preferred housing type matches the housing type specified by the user.

#### Component Interaction Details
1. The user executes the command `filter h/Condominium`, intending to filter out persons with a "Condominium" housing type preference.
2. The `FilterCommandParser` interprets the input, creating a FilterCommand with a `HousingTypeMatchPredicate`.
3. The `FilterCommand` applies the `HousingTypeMatchPredicate` predicate, updating the filtered person list to only include those with a "Condominium" housing type preference.
4. The UI reflects this filtered list.

#### Example Usage Scenario
1. The user inputs `filter h/Condominium`, intending to filter the list of persons to only include those with a "Condominium" housing type preference.
2. The UI reflects the filtered list with persons who have a "Condominium" housing type preference.

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

### <u>Overall Help feature</u>

#### Description
The Help feature provides help to the user (depending on user input) by either giving details on how all commands
are used in a new window, or a short description in the main window on how an individual, specified command is used.

There are two types of help features
1. General `Help` Feature
2. Command `Help`

#### Key Components
- `HelpCommand`: A command that, when executed, either shows a new window summarising help for all commands, or
prints the help message in the Main Window for the requested command, depending on user input.
- `HelpCommandParser`: Processes the user input to instantiate the HelpCommand object appropriately to perform the
correct action (the type of help to give, in this case help for all commands).

#### Overall `Help` Command Architecture
<puml src="diagrams/help/HelpCommand.puml" width="1000" />

#### `Help` Command Sequence Diagram
<puml src="diagrams/help/HelpSequenceDiagram.puml" width="1000" />

<div style="page-break-after: always;"></div>

### 1. General `Help` Feature

#### Description
The `help` feature provides help to the user by showing a new window with a summary of how to use all commands, with
the correct format and relevant examples. A link to the User Guide is also provided.

#### Component Interaction Details
1. User launches the application.
2. User executes `help`, wanting to get the help for all commands.
3. `LogicManager` instantiates a `RealodexParser`, which parses the command into a `HelpCommand`.
4. The `HelpCommand` is executed, showing a new window with help for all the features in Realodex.
5. The GUI reflects that the help window is currently open.

#### Example Usage Scenario
1. User executes `help`, wanting to get the help for all commands.
2. A new window opens with a summary of how to use all commands, with the correct format and relevant examples.

<div style="page-break-after: always;"></div>

#### Design Considerations

Aspect: Information to include in the Help Window

__Alternative 1 (current choice): Includes summary of ways to use all commands.__

Pros: User does not need to leave the app to get the appropriate help, and can visit the UG if he/she needs more information.

Cons: May be lengthy and hard to find when the set of commands added becomes larger in the future.

__Alternative 2: Only include link to User Guide in the help window.__

Pros: Help window does not have too much information.

Cons: User will need to leave the application and look at a website everytime they require help which can be inconvenient.

### 2. Help by command

#### Description
The Help by command feature provides help to the user for an individual command specified by the user,
printed on the main window. This has been implemented for the `add`,`clearRealodex`,`delete`,`edit`,`filter`,`list`
and `sort` commands only.

* Note that although the command format is `COMMAND help`, `clear help` is the command to get the help for the
clearRealodex command instead of `clearRealodex help`. 
* We changed the `clear` command to `clearRealodex` to avoid confusion with the `delete` command, as both involve the removal of entries, and `clearRealodex`encapsulates the
functionality of clearing the entire app more clearly. 
* However, we kept `clear help` as this syntax is more user-friendly when seeking help.

#### Component Interaction Details
1. User launches the application.
2. User executes `COMMAND help`, wanting to get the help for only specified `COMMAND`.
3. LogicManager instantiates a RealodexParser, which parses the command into a HelpCommand with appropriate parameters.
4. The HelpCommand is executed, printing the help message for the specified `COMMAND` in the GUI.

#### Example Usage Scenario
1. User executes `add help`, wanting to get the help for the `add` command.
2. The GUI prints the help message for the `add` command.

#### Design Considerations

Aspect: Method to request for help

__Alternative 1 (current choice): Format is `COMMAND help`.__

Pros: Intuitive syntax for the user, and is consistent with other CLI-based applications.

Cons: Harder to implement and maintain as a Developer as awareness of how other commands are currently being parsed is needed to preserve functionality.

__Alternative 2: Format is `help COMMAND`.__

Pros: Easy to implement as all functionality can be contained within help-related classes only.

Cons: Syntax may not be as intuitive.

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

### <u>Overall Delete Feature</u>

#### Description
The `delete` feature provides the user the ability to delete a client's profile based on their index in the list or their name.

There are two ways to delete:
1. `Delete` by index
2. `Delete` by name

#### Key components
* `DeleteCommand`: Executes the deletion operation based on the specified index or name.
* `DeleteCommandParser`: Parses the user input to instantiate the DeleteCommand object appropriately to perform the correct deletion operation.
* `DeleteCommandExecutor`: Creates DeleteCommand object based on user input and returns it.

#### `Delete` Command Architecture
<puml src="diagrams/delete/DeleteArchitecture.puml" width="1000" />

#### `Delete` Command Sequence Diagram
<puml src="diagrams/delete/DeleteSequenceDiagram.puml" width="1000" />

### 1. `Delete` by index feature

#### Description

The `delete` by index feature provides the user the ability to delete a client's profile based on their index in the list.

#### Component Interaction Details
1. User launches the application.
2. User executes `delete INDEX`, wanting to delete the profile on the client at index `INDEX`.
3. LogicManager instantiates a RealodexParser, which parses the command into a DeleteCommand with appropriate parameters.
4. The DeleteCommand is executed, deleting the client's profile at index `INDEX`.
5. The UI reflects the updated list of clients.

#### Example Usage Scenario
1. Assuming the Realodex list has 2 or more clients.
2. User executes `delete 2`, wanting to delete the profile on the client at index 2.
3. The UI reflects the updated list of clients where the original client at index 2 is deleted.

#### Design Considerations

Aspect: Method to delete client at index `INDEX`

__Alternative 1 (current choice): Format is `delete INDEX`.__

Pros: Easy to implement as name and index can be differentiated using the prefix.

Cons: Syntax may not be as intuitive.

__Alternative 2: Format is `delete i/INDEX`.__

Pros: More intuitive as the user knows that the index is being deleted.

Cons: Prefix `i/` is already used for the income field, and is more inconvenient to have to type in prefix.

### 2. Delete by name feature

#### Description

The `delete` by name feature provides the user the ability to delete a client's profile based on their name.

#### Component Interaction Details
1. User launches the application.
2. User executes `delete n/NAME`, wanting to delete the profile on the client with name `NAME`.
3. LogicManager instantiates a RealodexParser, which parses the command into a DeleteCommand with appropriate parameters.
4. The DeleteCommand is executed, deleting the client's profile with the name `NAME`.
5. The UI reflects the updated list of clients.

#### Example Usage Scenario
1. Assuming a client with the name `John Doe` exists in the Realodex list.
2. User executes `delete n/John Doe`, wanting to delete the profile on the client with name `John Doe`.
2. The UI reflects the updated list of clients, where the client with the name `John Doe` is deleted.

#### Design Considerations

Aspect: Method to delete client with name `NAME`

__Alternative 1 (current choice): Format is `delete n/NAME`.__

Pros: Easy to implement as name and index can be differentiated using the prefix.

Cons: Syntax may not be as intuitive.

__Alternative 2: Format is `delete NAME`.__

Pros: More convenient for user to not have to put in the prefix `n/`.

Cons: Harder to implement due to the other delete by index feature, there is no way to differentiate if the user is try to input an index or name.

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

### <u>Overall Edit Feature</u>

#### Description
The edit by field feature provides the user the ability to edit a client's profile based on a specified field. This has been implemented for all
user fields `name`, `phone`, `email`, `address`, `income`, `birthday`, `housingType`, `tags`, and `remark`.

#### Key Components
* EditCommand: Executes the editing operation based on the specified index or name and field.
* EditCommandParser: Parses the user input to instantiate the EditCommand object appropriately to perform the correct editing operation.

#### Edit Command Architecture
<puml src="diagrams/edit/EditArchitecture.puml" width="1000" />

#### Edit Command Sequence Diagram
<puml src="diagrams/edit/EditSequenceDiagram.puml" width="1000" />

#### Component Interaction Details
1. The user executes the command `edit INDEX n/John Doe`, intending to edit the name of the client at index `INDEX` to "John Doe".
2. The `EditCommandParser` interprets the input.
3. An `EditCommand` object is created.
4. The `LogicManager` invokes the execute method of EditCommand.
5. The execute method of `EditCommand` invokes the `editPerson` method in `Model` property to edit the client's profile with the new `Person` object.
6. The execute method of `EditCommand` returns a `CommandResult` object which stores the data regarding the completion of the `EditCommand`.
7. The UI reflects this updated list with the edited client.

#### Example Usage Scenario
1. The user executes the command `edit 1 n/John Doe`, intending to edit the name of the client at index 1 to "John Doe".
2. The UI reflects the updated list with the client at index 1 having the name "John Doe".

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* A real estate agent
* has a need to manage a significant number of contacts of their clients
* has to note down many details about each client
* has to frequently add, delete, and search for clients
* prefer desktop apps over other types
* can type fast
* is reasonably comfortable using CLI apps
* is a real estate agent that wants to store relevant information about clients
* able to store additional notes about contacts

**Value proposition**:
* manage contacts faster than a typical mouse/GUI driven applications.
* storing of information tailored to real-estate agents


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                    | I want to …​                                             | So that I can…​                                                                                   |
|----------|----------------------------|----------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| `* * *`  | first-time user            | receive a simple tutorial on app usage                    | easily navigate Realodex                                                                          |
| `* * *`  | tech-savvy user            | use a command-line interface                              | navigate the app more efficiently due to my fast typing speed                                    |
| `* * *`  | fast typer                 | quickly input various commands in the text box           | perform actions like adding new clients, editing profiles, finding clients, without using GUI    |
| `* * *`  | real-estate agent user     | easily log personal notes after client interactions       | reference these in future conversations for more personalized communication                     |
| `* * *`  | user with inactive clients | delete inactive clients permanently                      | remove them from my database and free up space                                                   |
| `* *`    | real estate agent user     | search for clients interested in specific property listings | quickly match selling and buying clients                                                         |
| `* *`    | real estate agent user     | record and access clients' preferred house types          | filter and match clients with relevant property listings                                         |
| `* *`    | real estate agent user     | analyze trends in housing preferences                     | understand market demands and tailor my services                                                 |
| `* *`    | efficient user             | filter clients by tag                                    | organize and access client information more efficiently                                           |
| `* *`    | efficient user             | filter clients by categories                             | better categorize and manage client information based on personal attributes                     |
| `* *`    | first-time user            | be guided through setting up my user profile              | save my details for future use                                                                   |
| `* *`    | first-time user            | learn how to create and edit client profiles              | manage client information efficiently                                                            |
| `* *`    | first-time user            | understand how to navigate the app and use CLI commands   | effectively use Realodex's features                                                              |
| `* *`    | forgetful user             | get instructions on how to set up profiles and navigate  | refresh my memory on how to use Realodex when needed                                             |
| `* *`    | user with inactive clients | archive inactive clients                                 | hide them from my active list while keeping their information for future reference               |
| `* `     | real estate agent user     | be notified of upcoming client birthdays                  | send personalized greetings and strengthen my relationships                                      |
| `*`      | real estate agent user     | be notified of upcoming holidays                          | prepare gifts for my clients and enhance our relationship                                        |
| `*`      | real estate agent user     | be reminded of significant client milestones              | acknowledge these events and further personalize our relationship                                |
| `*`      | tech-savvy user            | use tab to autofill parts of my command                  | speed up my use of the command line                                                              |

<div style="page-break-after: always;"></div>

### Use cases

(For all use cases below, the **System** is Realodex and the **Actor** is the user, unless specified otherwise)

**Use case: UC01 — Adding a user profile**

**Actor: User**

**MSS**

1. User enters `add ...` Command.
2. System adds user profile to `Realodex`.
3. System replies to user with a success message.
    
   Use case ends.

**Extensions:**

* 1a. `Name` does not contain fully alphanumeric characters.

    * 1a1. Realodex throws an error and highlights the format to user.
  
    * 1a2. User enters new data.

    * Steps 1a1 to 1a2 repeats until the name input is valid.
  
  * Use case resumes from step 2.

* 1b. `Name` contains erroneous whitespace at front or back.

    * 1b1. Realodex fixes this for user without errors.
  
    * Use case resumes from step 2.

* 1c. `Name` is not capitalized.

    * 1c1. Realodex fixes this for user without errors.
  
    * Use case resumes from step 2.

* 1d. `Name` is blank.

    * 1d1. Realodex throws an error and highlights the format to user.
  
    * 1d2. User enters new data.

    * Steps 1d1 to 1d2 repeats until the `Name` input is valid.

  * Use case resumes from step 2.

* 1e. `Phone` contains non-integer characters.

    * 1e1. Realodex throws an error and highlights the format to user.
  
    * 1e2. User enters new data.

    * Steps 1e1 to 1e2 repeats until the `Phone` input is valid.

  * Use case resumes from step 2.

* 1f. `Phone` is less than three characters.

    * 1f1. Realodex throws an error and highlights the format to user.
  
    * 1f2. User enters new data.

    * Steps 1f1 to 1f2 repeats until the `Phone` input is valid.

  * Use case resumes from step 2.

* 1g. `Phone` is blank.

    * 1g1. Realodex throws an error and highlights the format to user.
  
    * 1g2. User enters new data.

  * Steps 1g1 to 1g2 repeats until the `Phone` input is valid.

  * Use case resumes from step 2.

* 1h. `Income` is negative

    * 1h1. Realodex throws an error and highlights the format to user.
  
    * 1h2. User enters new data.

    * Steps 1h1 to 1h2 repeats until the `Income` input is valid.

  * Use case resumes from step 2.

* 1i. `Income` contains non-integer characters.

    * 1i1. Realodex throws an error and highlights the format to user.
  
    * 1i2. User enters new data.

    * Steps 1i1 to 1i2 repeats until the `Income` input is valid.

  * Use case resumes from step 2.

* 1j. `Income` is blank.

    * 1j1. Realodex throws an error and highlights the format to user.
  
    * 1j2. User enters new data.

    * Steps 1j1 to 1j2 repeats until the `Income` input is valid.

  * Use case resumes from step 2.

* 1k. `Email` is not in the valid format.

    * 1k1. Realodex throws an error and highlights the format to user.
  
    * 1k2. User enters new data.

    * Steps 1k1 to 1k2 repeats until the `Email` input is valid.

  * Use case resumes from step 2.

* 1l. `Email` is blank.

    * 1l1. Realodex throws an error and highlights the format to user.
  
    * 1l2. User enters new data.

    * Steps 1l1 to 1l2 repeats until the `Email` input is valid.

  * Use case resumes from step 2.

* 1m. `Address` is blank.

    * 1m1. Realodex throws an error and highlights the format to user.
  
    * 1m2. User enters new data.

    * Steps 1m1 to 1m2 repeats until the `Address` input is valid.

  * Use case resumes from step 2.

* 1n. `Family` contains non-integer characters.

    * 1n1. Realodex throws an error and highlights the format to user.
  
    * 1n2. User enters new data.

    * Steps 1n1 to 1n2 repeats until the `Family` input is valid.

  * Use case resumes from step 2.

* 1o. `Family` is negative or zero.

    * 1o1. Realodex throws an error and highlights the format to user.
  
    * 1o2. User enters new data.

    * Steps 1o1 to 1o2 repeats until the `Family` input is valid.

  * Use case resumes from step 2.

* 1p. `Family` is blank.

    * 1p1. Realodex throws an error and highlights the format to user.
  
    * 1p2. User enters new data.

    * Steps 1p1 to 1p2 repeats until the `Family` input is valid.

  * Use case resumes from step 2.

* 1q. `Tag` is not `buyer` or `seller`.

    * 1q1. Realodex throws an error and highlights the format to user.
  
    * 1q2. User enters new data.

    * Steps 1q1 to 1q2 repeats until the `Tag` input is valid.

  * Use case resumes from step 2.

* 1r. `Tag` is blank.

    * 1r1. Realodex throws an error and highlights the format to user.
  
    * 1r2. User enters new data.

    * Steps 1r1 to 1r2 repeats until the `Tag` input is valid.

  * Use case resumes from step 2.

* 1s. `Housing Type` is not in any of 'HDB', 'CONDOMINIUM', 'LANDED PROPERTY' or 'GOOD CLASS BUNGALOW'.

    * 1s1. Realodex throws an error and highlights the format to user.
  
    * 1s2. User enters new data.

    * Steps 1s1 to 1s2 repeats until the `Housing Type` input is valid.

  * Use case resumes from step 2.

* 1t. `Housing Type` is blank.

    * 1t1. Realodex throws an error and highlights the format to user.
  
    * 1t2. User enters new data.

    * Steps 1t1 to 1t2 repeats until the `Housing Type` input is valid.

  * Use case resumes from step 2.

* 1u. `Birthday` is not in the valid format.

    * 1u1. Realodex throws an error and highlights the format to user.
  
    * 1u2. User enters new data.

    * Steps 1u1 to 1u2 repeats until the `Birthday` input is valid.

  * Use case resumes from step 2.

* 1v. `Birthday` is blank.

    * 1v1. Realodex throws an error and highlights the format to user.
  
    * 1v2. User enters new data.

    * Steps 1v1 to 1v2 repeats until the `Birthday` input is valid.

  * Use case resumes from step 2.

* 1w. Some compulsory fields are missing.

    * 1w1. Realodex throws an error and highlights the format to user.
  
    * 1w2. User enters new data.

    * Steps 1w1 to 1w2 repeats until the user inputs all compulsory fields.

  * Use case resumes from step 2.
<div style="page-break-after: always;"></div>

**Use case: UC02 — Editing a user profile**

**Actor: User**

**MSS**

1. User Executes `edit ...` Command:
2. System edits user profile of `Realodex` and replies to user with a success message.
   
   Use case ends.

**Extensions:**

* 1a. `Name` does not contain fully alphanumeric characters.

    * 1a1. Realodex throws an error and highlights the format to user.

    * 1a2. User enters new data.

    * Steps 1a1 to 1a2 repeats until the name input is valid.

  * Use case resumes from step 2.

* 1b. `Name` contains erroneous whitespace at front or back.

    * 1b1. Realodex fixes this for user without errors.

  * Use case resumes from step 2.

* 1c. `Name` is not capitalized.

    * 1c1. Realodex fixes this for user without errors.

  * Use case resumes from step 2.

* 1d. `Name` is blank.

    * 1d1. Realodex throws an error and highlights the format to user.

    * 1d2. User enters new data.

    * Steps 1d1 to 1d2 repeats until the `Name` input is valid.

  * Use case resumes from step 2.

* 1e. `Phone` contains non-integer characters.

    * 1e1. Realodex throws an error and highlights the format to user.

    * 1e2. User enters new data. 

    * Steps 1e1 to 1e2 repeats until the `Phone` input is valid.

  * Use case resumes from step 2.

* 1f. `Phone` is less than three characters.

    * 1f1. Realodex throws an error and highlights the format to user.

    * 1f2. User enters new data.

    * Steps 1f1 to 1f2 repeats until the `Phone` input is valid.

  * Use case resumes from step 2.

* 1g. `Phone` is blank.

    * 1g1. Realodex throws an error and highlights the format to user.

    * 1g2. User enters new data.

    * Steps 1g1 to 1g2 repeats until the `Phone` input is valid.

  * Use case resumes from step 2.

* 1h. `Income` is negative

    * 1h1. Realodex throws an error and highlights the format to user.

    * 1h2. User enters new data.

    * Steps 1h1 to 1h2 repeats until the `Income` input is valid.

  * Use case resumes from step 2.

* 1i. `Income` contains non-integer characters.

    * 1i1. Realodex throws an error and highlights the format to user.

    * 1i2. User enters new data.

    * Steps 1i1 to 1i2 repeats until the `Income` input is valid.

  * Use case resumes from step 2.

* 1j. `Income` is blank.

    * 1j1. Realodex throws an error and highlights the format to user.

    * 1j2. User enters new data.

    * Steps 1j1 to 1j2 repeats until the `Income` input is valid.

  * Use case resumes from step 2.

* 1k. `Email` is not in the valid format.

    * 1k1. Realodex throws an error and highlights the format to user.

    * 1k2. User enters new data.

    * Steps 1k1 to 1k2 repeats until the `Email` input is valid.

  * Use case resumes from step 2.

* 1l. `Email` is blank.

    * 1l1. Realodex throws an error and highlights the format to user.

    * 1l2. User enters new data.

    * Steps 1l1 to 1l2 repeats until the `Email` input is valid.

  * Use case resumes from step 2.

* 1m. `Address` is blank.

    * 1m1. Realodex throws an error and highlights the format to user.

    * 1m2. User enters new data.

    * Steps 1m1 to 1m2 repeats until the `Address` input is valid.

  * Use case resumes from step 2.

* 1n. `Family` contains non-integer characters.

    * 1n1. Realodex throws an error and highlights the format to user.

    * 1n2. User enters new data.

    * Steps 1n1 to 1n2 repeats until the `Family` input is valid.

  * Use case resumes from step 2.

* 1o. `Family` is negative or zero.

    * 1o1. Realodex throws an error and highlights the format to user.

    * 1o2. User enters new data.

    * Steps 1o1 to 1o2 repeats until the `Family` input is valid.

  * Use case resumes from step 2.

* 1p. `Family` is blank.

    * 1p1. Realodex throws an error and highlights the format to user.

    * 1p2. User enters new data.

    * Steps 1p1 to 1p2 repeats until the `Family` input is valid.

  * Use case resumes from step 2.

* 1q. `Tag` is not `buyer` or `seller`.

    * 1q1. Realodex throws an error and highlights the format to user.

    * 1q2. User enters new data.

    * Steps 1q1 to 1q2 repeats until the `Tag` input is valid.

  * Use case resumes from step 2.

* 1r. `Tag` is blank.

    * 1r1. Realodex throws an error and highlights the format to user.

    * 1r2. User enters new data.

    * Steps 1r1 to 1r2 repeats until the `Tag` input is valid.

  * Use case resumes from step 2.

* 1s. `Housing Type` is not in any of 'HDB', 'CONDOMINIUM', 'LANDED PROPERTY' or 'GOOD CLASS BUNGALOW'.

    * 1s1. Realodex throws an error and highlights the format to user.

    * 1s2. User enters new data.

    * Steps 1s1 to 1s2 repeats until the `Housing Type` input is valid.

  * Use case resumes from step 2.

* 1t. `Housing Type` is blank.

    * 1t1. Realodex throws an error and highlights the format to user.

    * 1t2. User enters new data.

    * Steps 1t1 to 1t2 repeats until the `Housing Type` input is valid.

  * Use case resumes from step 2.

* 1u. `Birthday` is not in the valid format.

    * 1u1. Realodex throws an error and highlights the format to user.

    * 1u2. User enters new data.

    * Steps 1u1 to 1u2 repeats until the `Birthday` input is valid.

  * Use case resumes from step 2.

* 1v. `Birthday` is blank.

    * 1v1. Realodex throws an error and highlights the format to user.

    * 1v2. User enters new data.

    * Steps 1v1 to 1v2 repeats until the `Birthday` input is valid.

  * Use case resumes from step 2.

* 1w. No fields input.

    * 1w1. Realodex throws an error and highlights the format to user.
  
    * 1w2. User enters new data.
  
  * Use case resumes from step 1.

<div style="page-break-after: always;"></div>

**Use case: UC03 — `Delete` a person by name**

**MSS**

1.  User requests to `delete` user by name

2.  Realodex deletes the person with a success message

    Use case ends.

**Extensions:**

* 1a. The input `Name` is not of valid format

    * 1a1. Realodex shows an error message highlighting the correct format for `Name`.

    * 1a2. User inputs a new `Name`.

    * 1a3. Steps 1a1 to 1a2 repeats until the `Name` input is valid.
  
  * Use case resumes from step 2.

* 1b. The input `Name` is not found

    * 1b1. Realodex shows an error message that `Name` is invalid.

    * 1b2. User inputs a new `Name`.

    * 1b3. Steps 1b1 to 1b2 repeats until the `Name` input is valid.
  
  * Use case resumes from step 2.

<div style="page-break-after: always;"></div>

**Use case: UC04 — `Delete` a person by `index`**

**MSS**

1.  User requests to `delete` user by `index`

2.  Realodex deletes the person with a success message

    Use case ends.

**Extensions:**

* 1a. The `index` is more than client list size

    * 1a1. Realodex shows an error message indicating an invalid `index` error.

    * 1a2. User inputs a new `index`.

    * 1a3. Steps 1a1 to 1a2 repeats until the `index` input is valid.

  * Use case resumes from step 2.

* 1b. The `index` is negative

    * 1b1. Realodex shows an error message indicating a negative `index` error.

    * 1b2. User inputs a new `index`.

    * 1b3. Steps 1b1 to 1b2 repeats until the `index` input is valid.

  * Use case resumes from step 2.

<div style="page-break-after: always;"></div>

**Use case: UC05 — `Sort` list by birthday**

**MSS**

1.  User requests to `sort` the list by the nearest upcoming birthday.

2.  Realodex sorts the `list` and returns the sorted list to screen.

    Use case ends.

<div style="page-break-after: always;"></div>

**Use case: UC06 — `List`**

**MSS**

1.  User requests to `list`

2.  Realodex shows the list of all clients

    Use case ends.

**Extensions:**

* 2a. The list is empty

    * 2a1. Realodex shows an empty list.
  
  * Use case ends.

<div style="page-break-after: always;"></div>

**Use case: UC07 — `Filter` by Name**

**MSS**

1. User requests to `filter` clients by providing a `Name` substring.

2. Realodex filters and displays a list of all clients whose names include the input substring.

    Use case ends.

**Extensions:**

* 1a. The input substring is empty.

    * 1a1. Realodex shows an error message indicating that the filter criteria cannot be empty.

    * 1a2. User inputs a new substring.

    * 1a3. Steps 1a1 to 1a2 repeats until the input substring is valid.

  * Use case resumes from step 2.

* 1b. The name input is not of valid format.

    * 1b1. Realodex shows an error message highlighting the correct format.

    * 1b2. User inputs a new substring.

    * 1b3. Steps 1b1 to 1b2 repeats until the input substring is valid.

  * Use case resumes from step 2.

<div style="page-break-after: always;"></div>

**Use case: UC08 — Filter by Remarks**

**MSS**

1. User requests to filter clients by providing a remark reference.

2. Realodex filters and displays a list of all clients whose remarks match the reference input.

   Use case ends.

**Extensions:**

* 1a. The remark input is empty.

    * 1a1. Realodex shows an error message indicating that the filter criteria cannot be empty.

    * 1a2. User inputs a remark.

    * 1a3. Steps 1a1 to 1a2 repeats until the input remark is valid.

  * Use case resumes from step 2.

<div style="page-break-after: always;"></div>

**Use case: UC09 — Filter by Tag**

**MSS**

1. User requests to filter clients by providing a tag.

2. Realodex filters and displays a list of all clients whose tag matches the input.

   Use case ends.

**Extensions:**

* 1a. The tag input is empty.

    * 1a1. Realodex shows an error message indicating that the filter criteria cannot be empty.

    * 1a2. User inputs a new tag.

    * 1a3. Steps 1a1 to 1a2 repeats until the input tag is non-empty and valid.

  * Use case resumes from step 2.

* 1b. The tag input is not of valid format.

    * 1b1. Realodex shows an error message highlighting the correct format.

    * 1b2. User inputs a new tag.

    * 1b3. Steps 1b1 to 1b2 repeats until the tag input is valid.

  * Use case resumes from step 2.

<div style="page-break-after: always;"></div>

**Use case: UC10 — Filter by Housing Type**

**MSS**

1. User requests to filter clients by providing a housing type.

2. Realodex filters and displays a list of all clients whose housing type matches the input.

   Use case ends.

**Extensions:**

* 1a. The housing type input is empty.

    * 1a1. Realodex shows an error message indicating that the filter criteria cannot be empty.

    * 1a2. User inputs a housing type.
  
    * 1a3. Steps 1a1 to 1a2 repeats until the housing type is valid

  * Use case resumes from step 2.

* 1b. The housing type input is not of valid format.

    * 1b1. Realodex shows an error message highlighting the correct format.

    * 1b2. User inputs a new housing type.

    * 1b3. Steps 1b1 to 1b2 repeats until the housing type input is valid.

  * Use case resumes from step 2.

<div style="page-break-after: always;"></div>

**Use case: UC11 — Filter by Birthday**

**MSS**

1. User requests to filter clients by providing a birthday month.

2. Realodex filters and displays a list of all clients whose birthday month matches the input.

   Use case ends.

**Extensions:**

* 1a. The birthday month input is empty.

    * 1a1. Realodex shows an error message indicating that the filter criteria cannot be empty.

    * 1a2. User inputs a birthday month.
  
    * 1a3. Steps 1a1 to 1a2 repeats until the birthday month is non-empty and valid.

  * Use case resumes from step 2.

* 1b. The birthday month is not of valid format.

    * 1b1. Realodex shows an error message highlighting the correct format.

    * 1b2. User inputs a new birthday month.

    * 1b3. Steps 1b1 to 1b2 repeats until the birthday month input is valid.

  * Use case resumes from step 2.

<div style="page-break-after: always;"></div>

**Use case: UC12 — Getting help**

**MSS**

1. User requests for help.

2. Realodex displays a new window showing a summary of how all features are used with examples.

   Use case ends.

**Use case: UC13 — Getting help for specific command**

**MSS**

1. User requests for help for a specific command.

2. A string summarizing how that individual command is used with examples is displayed.

**Extensions:**

* 1a. The requested command does not exist.

    * 1a1. Realodex shows an error message command does not exist.

    * 1a2. User inputs a new command.

    * 1a3. Steps 1a1 to 1a2 repeats until the command input is valid.

  * Use case resumes from step 2.


<div style="page-break-after: always;"></div>

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. Application should respond to user interactions within 2 seconds. 
3. Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage. 
4. Should be able to have up to 1000 client profiles.
5. The response to any command should become visible within 5 seconds.
6. Application should load the GUI components and data within 5 seconds of start-up.
7. The user interface should be intuitive and not have a steep learning curve to get used to.
8. Data should be stored locally and should not be accessible from other devices due to privacy issues.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Client Profile**: Details of customer of the Real Esate Agent looking to buy / sell / rent a property
* **Command Line Interface (CLI)**: A text-based interface used to interact with the software by entering commands into a terminal or console window, typically preferred by users who prefer efficiency and automation.
* **Command**: An input from you that tells Realodex to execute an action.
* **Case-Sensitive**: The casing of the alphabetic characters matters
* **Case-Insensitive**: The casing of the alphabetic characters does not matter

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   2. Open a command terminal, cd into the folder you put the jar file in, and use the `java -jar realodex.jar` command to run the application.
      The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. Exiting the app

   1. Users can type in exit to exit the app. All data is auto-saved.

### Adding a person

1. Test case: `add n/John Doe p/98765432 i/20000 e/johnd@example.com a/311, Clementi Ave 2, #02-25 f/4 t/buyer t/seller h/HDB r/Has 3 cats b/01May2009`
   - Expected: A client `John Doe` will be added and shown on the GUI.
   If list was already populated, ensure you scroll down to find `John Doe`.
2. Test case: `add n/John Wick p/98765432 i/20000 e/johnd@example.com a/311, Clementi Ave 2, #02-25 f/4 t/buyer t/seller h/HDB`
   - Expected:
   A client `John Wick` will be added
   and shown on the GUI with the optional parameters stating that it is blank.
   If list was already populated, ensure you scroll down to find `John Wick`.
3. `add n/Tom Hanks e/johnd@example.com a/311, Clementi Ave 2, #02-25 f/4 t/buyer t/seller h/HDB`
   - Expected:
   A error message `Missing compulsory prefixes in the command! Prefixes That Are Missed Are: i/INCOME, p/PHONE` will be displayed.
4. `add n/John Doe p/9876@@5432 i/200@00 e/johnd@example.com a/311, Clementi Ave 2, #02-25 f/4 t/buyer t/seller h/HDB r/Has 3 cats b/01May2009`
   - Expected: A error message <br> `Error parsing phone: Phone numbers should only contain numbers, and it should be at least 3 digits long`
     <br> `Error parsing income: Income should be an integer and should be at least 0` will be displayed

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: Assuming, there exists a person in the list, first contact is deleted from the list.
      Details of the deleted contact shown in the status message. 

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message.

   1. Other incorrect delete commands to try: `delete`, `delete x` (where x is larger than the list size)<br>
      Expected: No person is deleted.
      Error details shown in the status message.

<div style="page-break-after: always;"></div>

### Editing a person

1. Editing a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `edit 1 n/John Doe`<br>
      Expected: First contact is updated with the new name. Details of the updated contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `edit 0 n/John Doe`<br>
      Expected: No person is updated. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect edit commands to try: `edit`, `edit x` (where x is larger than the list size)<br>
      Expected: Similar to previous.

### Sorting
1. Sorting a list while persons are being shown
   1. Prerequisites: Ensure at least a few people with specified birthdays are displayed on screen, 
       You may do so by using the add command to populate a list clients.
   2. Test case: `sort`
      - Expected: A list sorted based on their proximity to their upcoming birthday will be displayed.

### List
1. `list` will display a list of original clients, this is useful after using `filter` commands which will display a subset of the original list.

### Clear
1. `clearRealodex` will clear the list of clients from Realodex. Be careful as you are unable to undo.

### Filter By Name
1. Test case: `filter n/John`
   Expected: A list of clients whose names contain the string `John` will be returned.

### Filter By Tag
1. Test case: `filter t/buyer`
   Expected: A list of clients with `BUYER` tag is returned.

### Filter By Housing Type
1. Test case: `filter h/HDB`
   Expected: A list of clients with the HDB housing type is returned.

### Filter By Remark
1. Test case: `filter r/FOOD`
   Expected: A list of clients whose remarks include the specified keyphrase of FOOD is returned.

### Filter By Birthday
1. Test case: `filter b/SEP`
   Expected: A list of clients whose birthdays are in the specified month of September are returned.

### Help
1. `help` should display a help window with summary of each feature provided by Realodex

### Command Help

1. Test case: `add help`
   Expected: A help message for `AddCommand` will be displayed in the box

2. Test case: `edit help` 
   Expected: A help message for `EditCommand` will be displayed in the box

<div style="page-break-after: always;"></div>

### Corrupted Data

1. Dealing with missing/corrupted data files

   1. Should you want to re-enter your contacts in a fresh JSON file in the event of file corruption or a bad edit
      causing the format to be incorrect,
      simply delete `realodex.json` in the `data` directory and restart the app.
      A new JSON file with sample contacts will be generated and you may proceed from there.


### Save Data

1. Data is auto-saved in the `json` file in real time.

   1. Open up the `realodex.json` in the `data` directory in a text editor.

   2. If there is an existing user, try `delete index` where index is of that user.
      <br>Expected: This user will no longer appear in the `json` file after command is executed.
   3. If there is no existing user, you may want to refer to above "Corrupted Data" section
      to easily get a fresh `json` file with sample data and repeat from step 1.

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

## **Appendix: Planned Enhancements**

**Team size is five.**

1. **Make index error messages more specific for out-of-bounds indexes**
   -  Currently, if a user types in an index, that is out of bounds, assuming its within the integer limit of 2147483647,
      as addressed in the user guide, Realodex simply returns a `The client index provided is invalid`
       - E.g., Given list with 10 people, `edit 123 n/Denzel` and `delete 123` returns  `The client index provided is invalid`
       - We plan to make this more specific by returning a `Client index provided is more than the 
       actual number of clients in Realodex!`
   
2. **Make index error messages more specific for `edit` negative indexes**
   - If user types in a negative index for `edit`, Realodex returns `Invalid command format!...`
     - E.g. `edit -1 n/Denzel` returns `Invalid command format!...`
     - We plan to make this more specific by returning `Index is not a non-zero unsigned integer.`

3. **Reduce compulsory fields for the "add"
command and streamline the process of adding new user profiles**
   - Currently, `add` requires eight compulsory fields.
     This might be excessive for users to always put in eight fields
     every time they add a new user profile.
     While this was a design choice to prevent users from missing out on important
     fields, we plan to make this more user-friendly in the future.
     - We plan to introduce dynamic checks such that if the profile is of certain conditions, they may not need to input certain fields.
     - E.g., Users who add a profile that is a `SELLER`, they may choose to omit the input for `INCOME` which would otherwise be
       compulsory!
       The Rationale is that sellers may not see a need to disclose their income for real estate deals.
   - Based on feedback, we may make some fields not compulsory all together.
     - If users feedback that `EMAIL` is not as important for their day-to-day client management, we may remove the 
       compulsory need for it in future iterations.

4. **Make date error more specific for leap dates on non-leap years**
   - Currently, if user types in an invalid date for commands such as `edit`, Realodex will return a invalid
     `...Birthday should be in ddMMMyyyy format... Date should also not be in future years and no earlier than year 1000!`
     - When user types in a leap date on a year with no leap date such `29Feb2023`,
       this will expectedly return the error as its invalid date. 
       However, the error message is not entirely helpful as the format is technically correct as its `DDMMMYYYY`.
     - We plan to return a more specific message such as `Date input is a leap date on a non-leap year`

5. **Improve duplicate user profile check**
   - Currently, Realodex prevents duplicates based on the condition of two clients having the same full name regardless of other parameters.
     - Since many clients may have the same name or since they may only disclose a part of 
       their name leading to greater chances of name collisions, we plan to improve our duplicate detection.
   - We plan
     to add more conditions to our duplicate check such
     adding more fields to ensure they are truly not adding duplicates.
     Even if we detected a possible duplicate,
     we may give the user an option to continue adding the client if they wish to do so, instead of downright rejecting the 
     addition of the profile.

6. **Make `PHONE` input conditions less restrictive**
   - Currently, `PHONE` only allows numbers, and it should be at least three digits long
   - This poses problems to users who wish to input 
     - Symbols such `+6590215365` or `1-800` numbers
       which may commonly have `-`.
     - Symbols such as white-space as they may wish to put gaps such as `9021 5365`
   - We plan to reduce the restrictions of no symbols for future iterations 

7. **Allow symbols in `NAME` input**
   - Currently, `NAME` does not accept symbols, hence common substrings such as `s/o`,
     `d/o` or punctuation such as in "Tan Xin En, Betty" 
     are not allowed in the name input.
   - We plan to allow the usage of symbols for future iterations by accepting symbols in names.

8. **Allow multiple field inputs and relax duplicate `PREFIX` restrictions**
   - Currently, we do not allow duplicate prefixes for fields except `TAG` to prevent duplicate fields being added.
   - However, some clients may wish to store multiple fields, e.g., a seller may wish to sell multiple properties, and 
     hence Realodex will be required to list multiple addresses.
   - We plan to relax the duplicate prefixes restrictions for some fields and allow users to input multiple fields.

<div style="page-break-after: always;"></div>

9. **Allow symbols in `INCOME` input**
   - Currently, `INCOME` only allows numbers
   - This poses problems to users who wish to input commas in their input such as `100,000` which is common too
   - We plan to allow users to input symbols in their income while keeping the restriction of value >= 0.

10. **Persistent Filtered Client List**
    - Currently, our application resets the filtered client list to show all clients after any command (e.g. `add`, `edit`, or `delete`) is executed.
    - Users may find it an inconvenience that the list reverts to unfiltered after every command.
    - We plan to implement a persistent filter mechanism within the client list by modifying the application to maintain the state of the filtered client list across various commands until the user decides to reset it. 
    - This will allow the filter to remain active and allow for multiple filtering by stacking filter commands until the user decides to reset it using list.

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

## **Appendix: Appendix: Effort**

### Difficulty, challenges, efforts and achievements

1. **Modifying UI Components** One of the significant challenges we encountered was modifying the UI to incorporate our desired background. The UI comprises numerous components, 
     making it challenging to identify and modify the relevant code sections.
 
   **Achievements** Despite the challenge, we successfully navigated the UI structure and implemented the necessary modifications to integrate our custom background, enhancing the visual appeal of the application.

2. **Test Case Regressions** Another challenge we encountered was that after significant feature changes or additions, many of our test cases failed. 
     We spent considerable time tracing the test code or components to determine the source of the errors.

   **Achievements** Despite the challenge, this turned out to be a positive experience as it indicated robust code. After addressing the failures, we gained confidence in the robustness of our bug-free features.
