package io.koosha.konfiguration_lite.impl.v8;

import io.koosha.konfiguration_lite.KfgMissingKeyException;
import io.koosha.konfiguration_lite.Konfiguration;
import io.koosha.konfiguration_lite.TestUtil;
import io.koosha.konfiguration_lite.ext.v8.ExtYamlSource;
import io.koosha.konfiguration_lite.type.Kind;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.testng.Assert.assertEquals;

@SuppressWarnings("RedundantThrows")
public class ExtYamlSourceTest {

    static String SAMPLE_0;
    static String SAMPLE_1;

    private String yaml;

    private ExtYamlSource k;

    @BeforeClass
    public void init() throws Exception {
        //noinspection ConstantConditions
        final URI uri0 = ExtYamlSource.class.getClassLoader()
                                            .getResource("sample0.yaml")
                                            .toURI();
        //noinspection ConstantConditions
        final URI uri1 = ExtYamlSource.class.getClassLoader()
                                            .getResource("sample1.yaml")
                                            .toURI();
        SAMPLE_0 = new String(Files.readAllBytes(Paths.get(uri0)));
        SAMPLE_1 = new String(Files.readAllBytes(Paths.get(uri1)));
    }

    @BeforeMethod
    public void setup() throws Exception {
        this.yaml = SAMPLE_0;
        this.k = new ExtYamlSource("yamlSourceTest", this.yaml);
    }

    private Konfiguration k() {
        return this.k;
    }

    // =========================================================================


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

    // BAD CASES


    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testBadInt0() throws Exception {
        this.k().int_("aBool");
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testBadInt1() throws Exception {
        this.k().int_("aLong");
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testBadInt2() throws Exception {
        this.k().int_("aString");
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testBadInt3() throws Exception {
        this.k().int_("aDouble");
    }


    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testBadDouble0() throws Exception {
        this.k().double_("aBool");
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testBadDouble() throws Exception {
        this.k().double_("aString");
    }


    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testBadLong0() throws Exception {
        this.k().long_("aBool");
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testBadLong1() throws Exception {
        this.k().long_("aString");
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testBadLong2() throws Exception {
        this.k().long_("aDouble");
    }


    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testBadString2() throws Exception {
        this.k().string("aIntList");
    }


    // =========================================================================

    @Test
    public void testCustomValue() {
        final String yamlString = "bang:\n  str : hello\n  i: 99";

        final ExtYamlSource y = new ExtYamlSource("testYamlSource", yamlString);

        final TestUtil.DummyCustom bang = y.custom(
            "bang", Kind.of(TestUtil.DummyCustom.class));

        assertEquals(bang.i, 99);
        assertEquals(bang.str, "hello");
    }

    @Test
    public void testCustomValue2() {
        final String yamlString;
        try {
            File file = new File(getClass().getResource("/sample2.yaml").getPath());
            yamlString = new Scanner(file, StandardCharsets.UTF_8.name())
                .useDelimiter("\\Z").next();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        final ExtYamlSource y = new ExtYamlSource(
            "testYamlSource", yamlString);

        final TestUtil.DummyCustom2 bang = y.custom(
            "bang", Kind.of(TestUtil.DummyCustom2.class));

        assertEquals(bang.i, 99);
        assertEquals(bang.str, "hello");
        assertEquals(bang.olf, TestUtil.mapOf(
            "manga", "panga", "foo", "bar", "baz", "quo"));
        assertEquals(bang.again, "no");
        assertEquals(bang.i, 99);
    }

}
