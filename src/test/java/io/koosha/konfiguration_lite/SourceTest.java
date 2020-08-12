package io.koosha.konfiguration_lite;

import io.koosha.konfiguration_lite.type.Kind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Test for {@link Source}
 */
public class SourceTest {

    private static final String INVALID = "invalidAlwaysKey";
    private static final String MISSING = "missingAlwaysKey";
    private static final String NULL = "nullAlwaysKey";

    private static final String BOOL_T = "boolTrueKey";
    private static final String BOOL_F = "boolFalseKey";

    private static final String CHAR_A = "charA";
    private static final String CHAR_B = "charB";

    private static final String STRING_ABC = "stringAbc";

    private List<?> listValue = null;
    private Set<?> setValue = null;
    private Object customValue = null;

    class ExtSampleSource extends Source {

        @Override
        @NotNull
        public String name() {
            return getClass().getSimpleName();
        }

        @Override
        public boolean has(@NotNull String key, @Nullable Kind<?> type) {
            return !MISSING.equals(key);
        }

        @Override
        protected boolean isNull(@NotNull String key) {
            return NULL.equals(key);
        }

        @Override
        @NotNull
        protected Object bool0(@NotNull String key) {
            switch (key) {
                case BOOL_T:
                    return true;
                case BOOL_F:
                    return false;
                case INVALID:
                    return new Object();
                default:
                    throw new RuntimeException("unknown key=" + key);
            }
        }

        @Override
        @NotNull
        protected Object char0(@NotNull String key) {
            switch (key) {
                case CHAR_A:
                    return 'a';
                case CHAR_B:
                    return "b";
                case INVALID:
                    return "bb";
                default:
                    throw new RuntimeException("unknown key=" + key);
            }
        }

        @Override
        @NotNull
        protected Object string0(@NotNull String key) {
            switch (key) {
                case STRING_ABC:
                    return "abc";
                case INVALID:
                    return new Object();
                default:
                    throw new RuntimeException("unknown key=" + key);
            }
        }

        @Override
        @NotNull
        protected Number number0(@NotNull String key) {
            final String value = key.split(":")[1];
            if (key.startsWith("byte"))
                return Byte.valueOf(value);
            if (key.startsWith("short"))
                return Short.valueOf(value);
            if (key.startsWith("int"))
                return Integer.valueOf(value);
            if (key.startsWith("long"))
                return Long.valueOf(value);
            if (key.startsWith("float"))
                return Float.valueOf(value);
            if (key.startsWith("double"))
                return Double.valueOf(value);
            throw new RuntimeException("unknown key=" + key);
        }

        @Override
        @NotNull
        protected Number numberDouble0(@NotNull String key) {
            return number0(key);
        }

        @NotNull
        @Override
        protected List<?> list0(@NotNull final String key,
                                @NotNull final Kind<?> type) {
            return listValue;
        }

        @Override
        @NotNull
        protected Set<?> set0(@NotNull String key, @NotNull Kind<?> type) {
            return setValue;
        }

        @Override
        @NotNull
        protected Object custom0(@NotNull String key, @NotNull Kind<?> type) {
            return customValue;
        }

    }

    ExtSampleSource source;

    @BeforeMethod
    public void setup() {
        listValue = null;
        setValue = null;
        customValue = null;
        this.source = new ExtSampleSource();
    }

    // =========================================================================

    @Test(expectedExceptions = KfgAssertionException.class)
    public void testSubsetIsNotSupported() {
        this.source.subset("");
    }

    // --------------------------------- BOOL

    @Test
    public void testBoolValueTrue() {
        assertEquals(this.source.bool(BOOL_T), Boolean.TRUE);
    }

    @Test
    public void testBoolValueFalse() {
        assertEquals(this.source.bool(BOOL_F), Boolean.FALSE);
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testBoolInvalidValueThrowsException() {
        this.source.bool(INVALID);
    }

    @Test
    public void testBoolNullValue() {
        assertNull(this.source.bool(NULL));
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testBoolMissingValueThrowsException() {
        this.source.bool(MISSING);
    }

    // --------------------------------- CHAR

    @Test
    public void testCharValueChar() {
        assertEquals(this.source.char_(CHAR_A), (Character) 'a');
    }

    @Test
    public void testCharValueString() {
        assertEquals(this.source.char_(CHAR_B), (Character) 'b');
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testCharInvalidValueThrowsException() {
        this.source.char_(INVALID);
    }

    @Test
    public void testCharNullValue() {
        assertNull(this.source.char_(NULL));
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testCharMissingValueThrowsException() {
        this.source.char_(MISSING);
    }

    // --------------------------------- STRING

    @Test
    public void testStringValue() {
        assertEquals(this.source.string(STRING_ABC), "abc");
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testStringValueBadStringThrowsException() {
        this.source.string(INVALID);
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testStringInvalidValueThrowsException() {
        this.source.string(INVALID);
    }

    @Test
    public void testStringNullValue() {
        assertNull(this.source.string(NULL));
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testStringMissingValueThrowsException() {
        this.source.string(MISSING);
    }

    // --------------------------------- BYTE

    @Test
    public void testByteValue() {
        final Byte value = Integer.valueOf(9).byteValue();
        assertEquals(this.source.byte_("byte:" + value), value);
        assertEquals(this.source.byte_("short:" + value), value);
        assertEquals(this.source.byte_("int:" + value), value);
        assertEquals(this.source.byte_("long:" + value), value);

        final Byte min = Byte.MIN_VALUE;
        assertEquals(this.source.byte_("byte:" + min), min);
        assertEquals(this.source.byte_("short:" + min), min);
        assertEquals(this.source.byte_("int:" + min), min);
        assertEquals(this.source.byte_("long:" + min), min);

        final Byte max = Byte.MAX_VALUE;
        assertEquals(this.source.byte_("byte:" + max), max);
        assertEquals(this.source.byte_("short:" + max), max);
        assertEquals(this.source.byte_("int:" + max), max);
        assertEquals(this.source.byte_("long:" + max), max);
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testByteBadValue0ThrowsException() {
        this.source.byte_("short:300");
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testByteBadValue1ThrowsException() {
        this.source.byte_("int:300");
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testByteBadValue2ThrowsException() {
        this.source.byte_("long:300");
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testByteBadValue3ThrowsException() {
        this.source.byte_("short:-300");
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testByteBadValue4ThrowsException() {
        this.source.byte_("int:-300");
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testByteBadValue5ThrowsException() {
        this.source.byte_("long:-300");
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testByteBadValue6ThrowsException() {
        this.source.int_("double:9.9");
    }

    @Test
    public void testByteNullValue() {
        assertNull(this.source.byte_(NULL));
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testByteMissingValueThrowsException() {
        this.source.byte_(MISSING);
    }

    // --------------------------------- SHORT

    @Test
    public void testShortValue() {
        Short value = Integer.valueOf(9).shortValue();
        assertEquals(this.source.short_("byte:" + value), value);
        assertEquals(this.source.short_("short:" + value), value);
        assertEquals(this.source.short_("int:" + value), value);
        assertEquals(this.source.short_("long:" + value), value);

        value = Integer.valueOf(300).shortValue();
        assertEquals(this.source.short_("short:" + value), value);
        assertEquals(this.source.short_("int:" + value), value);
        assertEquals(this.source.short_("long:" + value), value);

        final Short min = Short.MIN_VALUE;
        assertEquals(this.source.short_("short:" + min), min);
        assertEquals(this.source.short_("int:" + min), min);
        assertEquals(this.source.short_("long:" + min), min);

        final Short max = Short.MAX_VALUE;
        assertEquals(this.source.short_("short:" + max), max);
        assertEquals(this.source.short_("int:" + max), max);
        assertEquals(this.source.short_("long:" + max), max);
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testShortBadValue0ThrowsException() {
        this.source.short_("int:33000");
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testShortBadValue1ThrowsException() {
        this.source.short_("long:33000");
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testShortBadValue2ThrowsException() {
        this.source.short_("int:-33000");
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testShortBadValue3ThrowsException() {
        this.source.short_("long:-33000");
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testShortBadValue4ThrowsException() {
        this.source.int_("double:9.9");
    }

    @Test
    public void testShortNullValue() {
        assertNull(this.source.short_(NULL));
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testShortMissingValueThrowsException() {
        this.source.short_(MISSING);
    }

    // --------------------------------- INT

    @Test
    public void testIntValue() {
        Integer value = 9;
        assertEquals(this.source.int_("byte:" + value), value);
        assertEquals(this.source.int_("short:" + value), value);
        assertEquals(this.source.int_("int:" + value), value);
        assertEquals(this.source.int_("long:" + value), value);

        value = 64000;
        assertEquals(this.source.int_("int:" + value), value);
        assertEquals(this.source.int_("long:" + value), value);

        final Integer min = Integer.MIN_VALUE;
        assertEquals(this.source.int_("int:" + min), min);
        assertEquals(this.source.int_("long:" + min), min);

        final Integer max = Integer.MAX_VALUE;
        assertEquals(this.source.int_("int:" + max), max);
        assertEquals(this.source.int_("long:" + max), max);
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testIntBadValue0ThrowsException() {
        this.source.int_("long:" + Long.MAX_VALUE);
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testIntBadValue1ThrowsException() {
        this.source.int_("long:" + Long.MIN_VALUE);
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testIntBadValue2ThrowsException() {
        this.source.int_("double:9.9");
    }

    @Test
    public void testIntNullValue() {
        assertNull(this.source.int_(NULL));
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testIntMissingValueThrowsException() {
        this.source.int_(MISSING);
    }

    // --------------------------------- LONG

    @Test
    public void testLongValue() {
        Long value = 9L;
        assertEquals(this.source.long_("byte:" + value), value);
        assertEquals(this.source.long_("short:" + value), value);
        assertEquals(this.source.long_("int:" + value), value);
        assertEquals(this.source.long_("long:" + value), value);

        value = 999999999999L;
        assertEquals(this.source.long_("long:" + value), value);

        final Long min = Long.MIN_VALUE;
        assertEquals(this.source.long_("long:" + min), min);

        final Long max = Long.MAX_VALUE;
        assertEquals(this.source.long_("long:" + max), max);
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testLongBadValue0ThrowsException() {
        this.source.long_("double:" + 9.9);
    }

    @Test
    public void testLongNullValue() {
        assertNull(this.source.long_(NULL));
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testLongMissingValueThrowsException() {
        this.source.long_(MISSING);
    }

    // --------------------------------- FLOAT

    @Test
    public void testFloatValue() {
        final long value0 = 9L;
        final Float value = 9F;
        assertEquals(this.source.float_("byte:" + value0), value);
        assertEquals(this.source.float_("short:" + value0), value);
        assertEquals(this.source.float_("int:" + value0), value);
        assertEquals(this.source.float_("long:" + value0), value);
        assertEquals(this.source.float_("float:" + value0), value);
        assertEquals(this.source.float_("double:" + value0), value);

        final Float min = Float.MIN_VALUE;
        assertEquals(this.source.float_("float:" + min), min);

        final Float max = Float.MAX_VALUE;
        assertEquals(this.source.float_("float:" + max), max);
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testFloatBadValue0ThrowsException() {
        this.source.int_("double:" + Double.MAX_VALUE);
    }

    @Test(expectedExceptions = KfgTypeException.class)
    public void testFloatBadValue1ThrowsException() {
        this.source.int_("double:" + Double.MIN_VALUE);
    }

    @Test
    public void testFloatNullValue() {
        assertNull(this.source.int_(NULL));
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testFloatMissingValueThrowsException() {
        this.source.int_(MISSING);
    }

    // --------------------------------- LIST

    @Test
    public void testList() {
        final List<String> value = Arrays.asList("a", null, "b", "c");
        this.listValue = value;
        assertEquals(this.source.list("any", Kind.STRING), value);
    }

    @Test
    public void testListNullValue() {
        assertNull(this.source.list(NULL, new Kind<Object>() {
        }));
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testListMissingValueThrowsException() {
        this.source.list(MISSING, new Kind<Object>() {
        });
    }

}
