package setting.common;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class SettingTest {

    String propertiesPath = "test\\resources\\setting\\setting.properties";
    String propertiesPath_dummy = "test\\resources\\setting\\setting_dummy.properties";
    String propertiesPath_reload = "test\\resources\\setting\\setting_reload.properties";
    String propertiesPath_forGetAll = "test\\resources\\setting\\setting_forGetAll.properties";

    @BeforeEach
    void init() {
        // clear Setting (edit null as member setting hold)
        Method setting_discard;
        try {
            setting_discard = Setting.class.getDeclaredMethod("discard");
            setting_discard.setAccessible(true);
            setting_discard.invoke(null);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see Setting#init(String)
     * test with inner private method.
     */
    @Test
    @DisplayName("Loaded properties successfully")
    void init_LoadedSuccessfully() {
        Setting.init(propertiesPath);

        assertThat(Setting.getFilePath())
                .isEqualTo(propertiesPath);
        assertThat(Setting.getAsString("loaded"))
                .isEqualTo("success");
    }

    /**
     * @see Setting#init(String)
     * test with inner private method.
     * @see <a href="https://todd.ginsberg.com/post/testing-system-exit/">description : {@link ExpectSystemExitWithStatus}</a>
     */
    @Test
    @DisplayName("init called tow time. Reload another file and confirm　Guaranteed singleton")
    @ExpectSystemExitWithStatus(-1)
    void init_calledTwoTime() {

        // first time load and access properties
        Setting.init(propertiesPath);
        assertThat(Setting.getFilePath())
                .isEqualTo(propertiesPath);
        assertThat(Setting.getAsString("loaded"))
                .isEqualTo("success");

        // second time load and access properties(use key defined "propertiesPath")
        Setting.init(propertiesPath_dummy);

        assertThat(Setting.getFilePath())
                .isEqualTo(propertiesPath);
        assertThat(Setting.getAsString("loaded"))
                .isEqualTo("success");

        // Can't setting get System exit with status: -1.
        Setting.getAsString("dummy");
    }

    /**
     * @see Setting#init(String)
     * test with inner private method.
     */
    @Test
    @DisplayName("param is null")
    @ExpectSystemExitWithStatus(-1)
    void init_paramIsNull() {
        Setting.init(null);
        Setting.getAsString("loaded");
    }

    /**
     * @see Setting#init(String)
     */
    @Test
    @DisplayName("properties no exist.(case path mistake")
    @ExpectSystemExitWithStatus(-1)
    void init_noExistPath() {
        Setting.init("path\\ttt\\t\\gh\\s\\a\\dummy.prop");
        Setting.getAsString("loaded");
    }

    /**
     * @see Setting#getAsString(String) As(String)
     * test with inner private method.
     */
    @Test
    @DisplayName("get value success")
    void getAsString_getValueSuccess() {
        Setting.init(propertiesPath);
        assertThat(Setting.getAsString("loaded"))
                .isEqualTo("success");
    }

    /**
     * @see Setting#getAsString(String) As(String)
     * test with inner private method.
     */
    @Test
    @DisplayName("value contains any space and tab")
    void getAsString_containSpace() {
        Setting.init(propertiesPath);
        // space
        assertThat(Setting.getAsString("pre-half-space"))
                .isEqualTo("pre-half-space");
        assertThat(Setting.getAsString("pre-full-space"))
                .isEqualTo("pre-full-space");
        assertThat(Setting.getAsString("middle-half-space"))
                .isEqualTo("middle- half -space");
        assertThat(Setting.getAsString("middle-full-space"))
                .isEqualTo("middle-　full　-space");
        assertThat(Setting.getAsString("end-half-space"))
                .isEqualTo("end-half-space");
        assertThat(Setting.getAsString("end-full-space"))
                .isEqualTo("end-full-space");
        assertThat(Setting.getAsString("contain_space_all"))
                .isEqualTo("s p　　a  ce");
        // tab
        assertThat(Setting.getAsString("contain_tab"))
                .isEqualTo("tab\ttab");
    }

    /**
     * @see Setting#getAsString(String) As(String)
     * test with inner private method.
     */
    @Test
    @DisplayName("key is null")
    @ExpectSystemExitWithStatus(-1)
    void getAsString_keyIsNull() {
        Setting.init(propertiesPath);
        Setting.getAsString(null);


    }

    /**
     * @see Setting#getAsString(String) As(String)
     * test with inner private method.
     */
    @Test
    @DisplayName("key is blank")
    @ExpectSystemExitWithStatus(-1)
    void getAsString_keyIsBlank() {
        Setting.init(propertiesPath);
        Setting.getAsString("");
    }

    /**
     * @see Setting#getAsString(String) As(String)
     * test with inner private method.
     */
    @Test
    @DisplayName("cat't get property.(key is right. property has no value")
    void getAsString_hasNoValue() {
        Setting.init(propertiesPath);
        assertThat(Setting.getAsString("non_value"))
                .isEqualTo("");
    }

    /**
     * @see Setting#getAsString(String) As(String)
     * test with inner private method.
     */
    @Test
    @DisplayName("can't get property.(key is undefined")
    @ExpectSystemExitWithStatus(-1)
    void getAsString_keyIsUndefined() {
        Setting.init(propertiesPath);
        Setting.getAsString("undefined_key_dummy_dummy");
    }

}
