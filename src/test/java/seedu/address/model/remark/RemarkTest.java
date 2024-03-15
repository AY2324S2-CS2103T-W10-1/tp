package seedu.address.model.remark;

import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RemarkTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Remark(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Remark(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null remark name
        assertThrows(NullPointerException.class, () -> Remark.isValidTagName(null));
    }

}
