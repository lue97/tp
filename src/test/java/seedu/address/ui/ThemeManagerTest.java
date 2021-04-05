package seedu.address.ui;

import org.junit.jupiter.api.Test;

public class ThemeManagerTest {

    static {
        ThemeManager.init();
    }

    @Test
    public void themeManager_reInit_success() {
        ThemeManager.init();
    }

}
