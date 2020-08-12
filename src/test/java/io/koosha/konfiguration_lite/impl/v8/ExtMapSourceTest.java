package io.koosha.konfiguration_lite.impl.v8;

import io.koosha.konfiguration_lite.KfgMissingKeyException;
import io.koosha.konfiguration_lite.Konfiguration;
import io.koosha.konfiguration_lite.KonfigurationFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@SuppressWarnings({"RedundantThrows", "WeakerAccess"})
public class ExtMapSourceTest {

    protected Map<String, Object> map0;

    private Konfiguration k;

    @BeforeClass
    public void classSetup() throws Exception {
        this.map0 = new HashMap<>();

        this.map0.put("aInt", 12);
        this.map0.put("aBool", true);
        this.map0.put("aIntList", asList(1, 0, 2));
        this.map0.put("aStringList", asList("a", "B", "c"));
        this.map0.put("aLong", Long.MAX_VALUE);
        this.map0.put("aDouble", 3.14D);
        this.map0.put("aString", "hello world");

        HashMap<Object, Object> m0 = new HashMap<>();
        m0.put("a", 99);
        m0.put("c", 22);
        this.map0.put("aMap", m0);

        HashSet<Integer> s0 = new HashSet<>(asList(1, 2));
        this.map0.put("aSet", s0);
        this.map0 = Collections.unmodifiableMap(this.map0);
    }

    @BeforeMethod
    public void setup() throws Exception {
        this.k = KonfigurationFactory.getInstanceV8().map("map", map0);
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
    public void testBadDouble1() throws Exception {
        this.k().double_("aLong");
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
    public void testBadString0() throws Exception {
        this.k().string("aInt");
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testBadString1() throws Exception {
        this.k().string("aBool");
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testBadString2() throws Exception {
        this.k().string("aIntList");
    }


}
