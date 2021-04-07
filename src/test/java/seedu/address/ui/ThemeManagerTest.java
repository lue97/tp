package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ThemeManagerTest {

    static {
        ThemeManager.init();
    }

    @Test
    public void themeManager_getTheme_success() {
        assertEquals(ThemeFactory.getDefaultTheme(), ThemeManager.getTheme());
    }

}
