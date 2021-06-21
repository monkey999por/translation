package app;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testonly.RefMethod;

import static org.assertj.core.api.Assertions.assertThat;

class SettingTest {

    final String propertiesPath = "test\\resources\\setting\\setting.properties";
    final String propertiesPath_dummy = "test\\resources\\setting\\setting_dummy.properties";
    final String propertiesPath_forGetAll = "test\\resources\\setting\\setting_forGetAll.properties";

    // for private method test.
    // for use only refactoring. variable_name same as value.
    private final RefMethod load = new RefMethod(Setting.class, "load", String.class);
    private final RefMethod likeNumber = new RefMethod(Setting.class, "likeNumber", String.class, String.class);
    private final RefMethod likeBoolean = new RefMethod(Setting.class, "likeBoolean", String.class, String.class);
    private final RefMethod isBlankKey = new RefMethod(Setting.class, "isBlankKey", String.class);
    //    private final RefMethod ApplicationFailure = new RefMethod(Setting.class, "ApplicationFailure", String.class);
    private final RefMethod discard = new RefMethod(Setting.class, "discard");
    private final RefMethod get = new RefMethod(Setting.class, "get", String.class);

    @BeforeEach
    void init() throws Exception {
        // clear Setting (edit null as member setting hold)
        this.discard.invoke();
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
     * test with inner private method {@link #load}
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
     * test with inner private method {@link #load}
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
     * test with inner private method {@link #load}
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
     * test with inner private method {@link #load}
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
     * test with inner private method {@link #get}
     */
    @Test
    @DisplayName("can't get property.(key is right. property has no value")
    void getAsString_hasNoValue() {
        Setting.init(propertiesPath);
        assertThat(Setting.getAsString("non_value"))
                .isEqualTo("");
    }

    /**
     * @see Setting#getAsString(String) As(String)
     * test with inner private method {@link #load}
     */
    @Test
    @DisplayName("can't get property.(key is undefined")
    @ExpectSystemExitWithStatus(-1)
    void getAsString_keyIsUndefined() {
        Setting.init(propertiesPath);
        Setting.getAsString("undefined_key_dummy_dummy");
    }

    /**
     * @see #isBlankKey
     * private method.
     */
    @Test
    @DisplayName("no error")
    void isBlankKey_noError() throws Exception {
        this.isBlankKey.invoke("no error");
    }

    /**
     * @see #isBlankKey
     * private method.
     */
    @Test
    @DisplayName("key is null")
    @ExpectSystemExitWithStatus(-1)
    void isBlankKey_keyIsNull() throws Exception {
        this.isBlankKey.invoke(new Object[]{null});
    }

    /**
     * @see #isBlankKey
     * private method.
     */
    @Test
    @DisplayName("key is blank")
    @ExpectSystemExitWithStatus(-1)
    void isBlankKey_keyIsBlank() throws Exception {
        this.isBlankKey.invoke("");
    }

    /**
     * @see #likeNumber
     * private method.
     */
    @Test
    @DisplayName("key is blank")
    @ExpectSystemExitWithStatus(-1)
    void likeNumber_keyIsBlank() throws Exception {
        this.likeNumber.invoke("", "value");
    }

    /**
     * @see #likeNumber
     * private method.
     */
    @Test
    @DisplayName("not number")
    @ExpectSystemExitWithStatus(-1)
    void likeNumber_notNumber() throws Exception {
        this.likeNumber.invoke("not number", "value");
    }

    /**
     * @see #likeNumber
     * private method.
     */
    @Test
    @DisplayName("param is number like. expect no error")
    void likeNumber_numberLike() throws Exception {
        // comment auto is error pattern
        this.likeNumber.invoke("int", "10");
        this.likeNumber.invoke("double", "10.1235");
        this.likeNumber.invoke("decimal", "0.00000002365478799");
        this.likeNumber.invoke("pre0", "0123");
        this.likeNumber.invoke("start with dot", ".32");
        this.likeNumber.invoke("start with -", "-8888");
//        this.likeNumber.invoke(new Object[]{"start with +", "+5689"});
//        this.likeNumber.invoke(new Object[]{"contain half-space start end", " 45 "});
//        this.likeNumber.invoke(new Object[]{"contain half-space middle", "4 6"});
//        this.likeNumber.invoke(new Object[]{"start with tab code", "\t99"});
//        this.likeNumber.invoke(new Object[]{"contains comma", "1,234,568"});
        this.likeNumber.invoke("contains E", "1.234E+02");
//        this.likeNumber.invoke(new Object[]{"contains Napier", "1.234-e"});
//        this.likeNumber.invoke(new Object[]{"Napier", "E"});
//        this.likeNumber.invoke(new Object[]{"contains full-space start end", "　12　"});
//        this.likeNumber.invoke(new Object[]{"Full-width", "１２３"});
//        this.likeNumber.invoke(new Object[]{"end with -", "10-"});
//        this.likeNumber.invoke(new Object[]{"fraction", "5/8"});
    }

    /**
     * @see #likeBoolean
     * private method.
     */
    @Test
    @DisplayName("key is empty")
    @ExpectSystemExitWithStatus(-1)
    void likeBoolean_keyIsEmpty() throws Exception {
        this.likeBoolean.invoke("", "value");
    }

    /**
     * @see #likeBoolean
     * private method.
     */
    @Test
    @DisplayName("not boolean like")
    @ExpectSystemExitWithStatus(-1)
    void likeBoolean_notBoolean() throws Exception {
        this.likeBoolean.invoke("key", "tree");
    }

    /**
     * @see #likeBoolean
     * private method.
     */
    @Test
    @DisplayName("boolean like")
    void likeBoolean_booleanLike() throws Exception {
        // arrowed pattern
        this.likeBoolean.invoke("lower case true", "true");
        this.likeBoolean.invoke("lower case false", "false");
//        this.likeBoolean.invoke(new Object[]{"upper case true", "TRUE"});
//        this.likeBoolean.invoke(new Object[]{"upper case false", "FALSE"});
//        this.likeBoolean.invoke(new Object[]{"start with Upper true", "True"});
//        this.likeBoolean.invoke(new Object[]{"start with Upper false", "False"});
//        this.likeBoolean.invoke(new Object[]{"start with Upper false", "False"});
//        this.likeBoolean.invoke(new Object[]{"random upper or lower true", "tRue"});
//        this.likeBoolean.invoke(new Object[]{"random upper or lower false", "FAlsE"});
    }

    /**
     * @see Setting#getAsInteger(String)
     */
    @Test
    @DisplayName("key is null")
    @ExpectSystemExitWithStatus(-1)
    void getAsInteger_keyIsNull() {
        Setting.init(propertiesPath);
        Setting.getAsInteger("undefined key");
    }

    /**
     * @see Setting#getAsInteger(String)
     */
    @Test
    @DisplayName("not number")
    @ExpectSystemExitWithStatus(-1)
    void getAsInteger_notNumber() {
        Setting.init(propertiesPath);
        Setting.getAsInteger("debug_mode");
    }

    /**
     * @see Setting#getAsInteger(String)
     */
    @Test
    @DisplayName("type missing")
    @ExpectSystemExitWithStatus(-1)
    void getAsInteger_typeMissing() {
        Setting.init(propertiesPath);
        Setting.getAsInteger("double_like");
    }

    /**
     * @see Setting#getAsInteger(String)
     */
    @Test
    @DisplayName("type missing")
    void getAsInteger_success() {
        Setting.init(propertiesPath);
        assertThat(Setting.getAsInteger("integer_like"))
                .isEqualTo(10);
    }

    /**
     * @see Setting#getAsDouble(String)
     */
    @Test
    @DisplayName("key is null")
    @ExpectSystemExitWithStatus(-1)
    void getAsDouble_keyIsNull() {
        Setting.init(propertiesPath);
        Setting.getAsDouble("undefined key");
    }

    /**
     * @see Setting#getAsDouble(String)
     */
    @Test
    @DisplayName("not number")
    @ExpectSystemExitWithStatus(-1)
    void getAsDouble_notNumber() {
        Setting.init(propertiesPath);
        Setting.getAsDouble("debug_mode");
    }

    /**
     * @see Setting#getAsDouble(String)
     */
    @Test
    @DisplayName("type missing")
    @ExpectSystemExitWithStatus(-1)
    void getAsDouble_typeMissing() {
        Setting.init(propertiesPath);
        Setting.getAsDouble("double_type_missing");
    }

    /**
     * @see Setting#getAsDouble(String)
     */
    @Test
    @DisplayName("type missing")
    void getAsDouble_success() {
        Setting.init(propertiesPath);
        assertThat(Setting.getAsDouble("double_like"))
                .isEqualTo(10.0);
    }

    /**
     * @see Setting#getAsLong(String)
     */
    @Test
    @DisplayName("key is null")
    @ExpectSystemExitWithStatus(-1)
    void getAsLong_keyIsNull() {
        Setting.init(propertiesPath);
        Setting.getAsLong("undefined key");
    }

    /**
     * @see Setting#getAsLong(String)
     */
    @Test
    @DisplayName("not number")
    @ExpectSystemExitWithStatus(-1)
    void getAsLong_notNumber() {
        Setting.init(propertiesPath);
        Setting.getAsLong("debug_mode");
    }

    /**
     * @see Setting#getAsLong(String)
     */
    @Test
    @DisplayName("type missing")
    @ExpectSystemExitWithStatus(-1)
    void getAsLong_typeMissing() {
        Setting.init(propertiesPath);
        Setting.getAsLong("decimal_like");
    }

    /**
     * @see Setting#getAsLong(String)
     */
    @Test
    @DisplayName("type missing")
    void getAsLong_success() {
        Setting.init(propertiesPath);
        assertThat(Setting.getAsLong("long_like"))
                .isEqualTo(1000000000000000000L);
    }

    /**
     * @see Setting#getAsBoolean(String)
     */
    @Test
    @DisplayName("key is null")
    @ExpectSystemExitWithStatus(-1)
    void getAsBoolean_keyIsNull() {
        Setting.init(propertiesPath);
        Setting.getAsBoolean("undefined key");
    }

    /**
     * @see Setting#getAsBoolean(String)
     */
    @Test
    @DisplayName("not boolean like")
    @ExpectSystemExitWithStatus(-1)
    void getAsBoolean_notBoolean() {
        Setting.init(propertiesPath);
        Setting.getAsBoolean("double_like");
    }

    /**
     * @see Setting#getAsBoolean(String)
     */
    @Test
    @DisplayName("type missing")
    @ExpectSystemExitWithStatus(-1)
    void getAsBoolean_typeMissing() {
        Setting.init(propertiesPath);
        Setting.getAsBoolean("decimal_like");
    }

    /**
     * @see Setting#getAsBoolean(String)
     */
    @Test
    @DisplayName("type missing")
    void getAsBoolean_success() {
        Setting.init(propertiesPath);
        assertThat(Setting.getAsBoolean("true"))
                .isEqualTo(true);
        assertThat(Setting.getAsBoolean("false"))
                .isEqualTo(false);
        assertThat(Setting.getAsBoolean("true_value"))
                .isEqualTo(true);
    }

    /**
     * @see Setting#getAllToString()
     */
    @Test
    @DisplayName("get all to string success")
    void getAllToString_success() {
        Setting.init(propertiesPath_forGetAll);
        assertThat(Setting.getAllToString())
                .isEqualTo(
                        "■ [false_value] :   false" + "\r\n" +
                                "■ [debug_mode] :   true" + "\r\n" +
                                "■ [long_key_name_aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa] :   long_key_name" + "\r\n" +
                                "■ [windows_path_like] :   C:\\develop\\language-detection\\profiles" + "\r\n" +
                                "■ [regexp_like] :   ^[A-X]\\s*(B|C)" + "\r\n" +
                                "■ [false] :   false" + "\r\n" +
                                "■ [true_value] :   true" + "\r\n" +
                                "■ [decimal_like] :   .000312425" + "\r\n" +
                                "■ [double_like] :   10.0" + "\r\n" +
                                "■ [double_like_detail] :   10.019834u582" + "\r\n" +
                                "■ [unix_path_like] :   /develop/language-detection/profiles" + "\r\n" +
                                "■ [symbols] :   )('&%$#\"!{}_?*`P+><./\\:][\\^-" + "\r\n" +
                                "■ [url_like] :   https://dummy.com/url_like/hoge?param=p" + "\r\n" +
                                "■ [integer_like] :   10" + "\r\n" +
                                "■ [true] :   true" + "\r\n" +
                                "■ [version_like] :   1.20.1.1" + "\r\n" +
                                "■ [non_value] :   " + "\r\n"
                );
    }


}
