package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.util.DateUtil.ZERO_DAY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.model.util.SampleDataUtil;
import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void person_getGoalDeadline() {
        Person aliceCopy = new PersonBuilder(ALICE).build();
        LocalDate date = LocalDate.of(2021, 3, 30);

        Person editedAlice = new PersonBuilder(aliceCopy).build()
                .withGoal(new Goal(Goal.Frequency.WEEKLY));
        assertEquals(ZERO_DAY, editedAlice.getGoalDeadline(date));

        editedAlice = new PersonBuilder(aliceCopy).build()
                .withGoal(new Goal(Goal.Frequency.WEEKLY))
                .withMeetings(Arrays.asList(SampleDataUtil.getSampleEvents().clone()));
        assertEquals(LocalDate.of(2021, 2, 7), editedAlice.getGoalDeadline(date));

        editedAlice = new PersonBuilder(aliceCopy).build()
                .withGoal(new Goal(Goal.Frequency.MONTHLY))
                .withMeetings(Arrays.asList(SampleDataUtil.getSampleEvents().clone()));
        assertEquals(LocalDate.of(2021, 2, 28), editedAlice.getGoalDeadline(date));

        editedAlice = new PersonBuilder(aliceCopy).build()
                .withGoal(new Goal(Goal.Frequency.YEARLY))
                .withMeetings(Arrays.asList(SampleDataUtil.getSampleEvents().clone()));
        assertEquals(LocalDate.of(2022, 12, 31), editedAlice.getGoalDeadline(date));

        // leap day
        editedAlice = new PersonBuilder(aliceCopy).build()
                .withGoal(new Goal(Goal.Frequency.MONTHLY))
                .withMeetings(Collections.singletonList(new Event(
                        LocalDate.of(2024, 1, 15),
                        LocalTime.of(10, 0),
                        "Some meeting"
                )));
        assertEquals(LocalDate.of(
                2024, 2, 29),
                editedAlice.getGoalDeadline(LocalDate.of(2024, 1, 20)));

    }
}
