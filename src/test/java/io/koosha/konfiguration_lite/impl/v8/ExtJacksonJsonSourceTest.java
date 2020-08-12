package io.koosha.konfiguration_lite.impl.v8;

import io.koosha.konfiguration_lite.KfgMissingKeyException;
import io.koosha.konfiguration_lite.ext.v8.ExtJacksonJsonSource;
import io.koosha.konfiguration_lite.type.Kind;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;

@SuppressWarnings("RedundantThrows")
public class ExtJacksonJsonSourceTest {

    private static final Logger LOG = LoggerFactory.getLogger(ExtJacksonJsonSourceTest.class);

    static String SAMPLE_0;
    static String SAMPLE_1;

    private String json;

    private ExtJacksonJsonSource k;

    @BeforeClass
    public void init() throws Exception {
        //noinspection ConstantConditions
        final URI uri0 = ExtJacksonJsonSource.class.getClassLoader()
                                                   .getResource("sample0.json")
                                                   .toURI();
        //noinspection ConstantConditions
        final URI uri1 = ExtJacksonJsonSource.class.getClassLoader()
                                                   .getResource("sample1.json")
                                                   .toURI();
        SAMPLE_0 = new String(Files.readAllBytes(Paths.get(uri0)));
        SAMPLE_1 = new String(Files.readAllBytes(Paths.get(uri1)));
    }

    @BeforeMethod
    public void setup() throws Exception {
        this.json = SAMPLE_0;
        this.k = new ExtJacksonJsonSource("testJacksonSource", json);
    }

    private ExtJacksonJsonSource k() {
        return this.k;
    }

    @Test
    public void testBool() throws Exception {
        assertEquals(this.k().bool("aBool"), Boolean.TRUE);
    }

    @Test
    public void testInt() throws Exception {
        assertEquals(this.k().int_("aInt"), Integer.valueOf(12));
    }

    @Test
    public void testLong() throws Exception {
        assertEquals(this.k().long_("aLong"), (Object) Long.MAX_VALUE);
    }

    @Test
    public void testDouble() throws Exception {
        assertEquals(this.k().double_("aDouble"), (Double) 3.14);
    }

    @Test
    public void testString() throws Exception {
        assertEquals(this.k().string("aString"), "hello world");
    }

    @Test
    public void testList() throws Exception {
        final List<Integer> before = this.k().list("aIntList", Kind.INT);
        assertEquals(before, asList(1, 0, 2));
    }

    @Test
    public void testSet() throws Exception {
        assertEquals(this.k().set("aSet", Kind.INT), new HashSet<>(asList(1, 2)));
    }


    // BAD CASES

    @Test(expectedExceptions = KfgMissingKeyException.class,
          dataProvider = "testBadDoubleDataProvider")
    public void testBadDouble(@NotNull final String konfigKey) throws Exception {
        this.k().double_(konfigKey);
        LOG.error("testBadDouble: {} did not fail", konfigKey);
    }

    @DataProvider
    public static Object[][] testBadDoubleDataProvider() {
        return new Object[][]{
            {"aString"},
            // {"aInt"},
            {"aBool"},
            {"aIntList"},
            // {"aLong"},
            // {"aDouble"},
            {"aMap"},
            {"aSet"},
            {"some"},
        };
    }

}
