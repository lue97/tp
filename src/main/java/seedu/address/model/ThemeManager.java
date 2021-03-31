package seedu.address.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Observer;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.FileUtil;
import seedu.address.ui.Theme;
import seedu.address.ui.ThemeFactory;
import seedu.address.ui.exceptions.InvalidThemeException;

/**
 * Class for managing the theme of the application. Stores data on what theme is currently being applied.
 */
public class ThemeManager implements ThemeObservable {

    /**
     * Template of the css used by the application.
     */
    private static final String CSS_TEMPLATE = FileUtil.getResourceAsString("/view/Template.css");

    private static final Logger logger = LogsCenter.getLogger(ThemeManager.class);

    private List<ThemeObserver> observers;

    /**
     * Current theme used by the application
     */
    private Theme theme;

    /**
     * Path of the current theme
     */
    private String themePath;

    /**
     * Path of the css file currently in use
     */
    private String cssCacheUri;

    /**
     * Constructs a new themeManager.
     */
    public ThemeManager(String themePath) {
        this.observers = new ArrayList<>();
        this.theme = ThemeFactory.getDefaultTheme();
        this.cssCacheUri = getNewCssCacheUri(this.theme);
        if (themePath == null) {
            this.setTheme(ThemeFactory.getDefaultTheme(), null);
        } else {
            try {
                Theme theme = ThemeFactory.load(themePath);
                setTheme(theme, themePath);
            } catch (DataConversionException | InvalidThemeException exception) {
                logger.warning("Invalid " + themePath + " theme supplied");
                this.setTheme(ThemeFactory.getDefaultTheme(), themePath);
            } catch (IOException fileNotFoundException) {
                logger.warning("Theme " + themePath + " not found");
                this.setTheme(ThemeFactory.getDefaultTheme(), themePath);
            }
        }
    }

    /**
     * Gets the theme currently in use by the application.
     *
     * @return The theme currently in use by the application.
     */
    public Theme getTheme() {
        return this.theme;
    }

    /**
     * Gets the path of the JSON theme file currently being applied.
     *
     * @return Path of the JSON theme file currently being applied.
     */
    public String getThemePath() {
        return this.themePath;
    }

    /**
     * Sets the current theme of the application.
     *
     * @param newTheme  The new theme to be used.
     * @param themePath The path of the new theme to be used.
     */
    public void setTheme(Theme newTheme, String themePath) {
        this.theme = newTheme;
        this.themePath = themePath;
        String newCssCache = getNewCssCacheUri(newTheme);
        if (newCssCache != null) {
            this.cssCacheUri = getNewCssCacheUri(newTheme);
        }
        updateAll();
    }

    public String getCssCacheUri() {
        return this.cssCacheUri;
    }

    /**
     * Returns the URI of the updated CSS cache.
     *
     * @return URI of the updated CSS cache.
     */
    private static String getNewCssCacheUri(Theme theme) {
        String cssString = generateCssFromTheme(theme);
        try {
            return createCssCacheFile(cssString);
        } catch (IOException ioException) {
            return null;
        }
    }

    /**
     * Generates CSS based on theme given.
     *
     * @param theme The theme to be used.
     * @return cssTemplate with colors assigned.
     */
    private static String generateCssFromTheme(Theme theme) {
        String cssString = ThemeManager.CSS_TEMPLATE;
        cssString = cssString
                .replaceAll("\\$foreground", theme.foreground)
                .replaceAll("\\$background", theme.background);
        for (int i = 0; i < 16; i++) {
            cssString = cssString
                    .replaceAll("\\$c" + Integer.toHexString(i), theme.color[i]);
        }
        return cssString;
    }

    /**
     * Creates a temporary file containing the given CSS.
     *
     * @param cssString CSS to be written to the file.
     * @return The temp file location.
     * @throws IOException The file cannot be created/written to.
     */
    private static String createCssCacheFile(String cssString) throws IOException {
        File temp = File.createTempFile("current", ".tmp");
        temp.deleteOnExit();
        BufferedWriter out = new BufferedWriter(new FileWriter(temp));
        out.write(cssString);
        out.close();
        return temp.getAbsolutePath().replace(File.separator, "/");
    }

    @Override
    public void addObserver(Observer<String[]> observer) {
        this.observers.add((ThemeObserver) observer);
    }

    /**
     * Updates all Observers subscribed to this object.
     */
    public void updateAll() {
        for (ThemeObserver observer : this.observers) {
            String[] args = new String[] {this.getThemePath(), this.getCssCacheUri()};
            observer.update(args);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ThemeManager t = (ThemeManager) o;
        if (!Objects.equals(theme, t.theme)) {
            return false;
        }
        if (!Objects.equals(themePath, t.themePath)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = observers != null ? observers.hashCode() : 0;
        result = 31 * result + (theme != null ? theme.hashCode() : 0);
        result = 31 * result + (themePath != null ? themePath.hashCode() : 0);
        result = 31 * result + (cssCacheUri != null ? cssCacheUri.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ThemeManager{"
                + "observers=" + observers
                + ", theme=" + theme
                + ", themePath='" + themePath + '\''
                + ", cssCacheUri='" + cssCacheUri + '\''
                + '}';
    }
}