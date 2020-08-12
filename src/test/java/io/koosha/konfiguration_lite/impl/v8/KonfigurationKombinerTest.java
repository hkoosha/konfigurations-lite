package io.koosha.konfiguration_lite.impl.v8;

import io.koosha.konfiguration_lite.KfgMissingKeyException;
import io.koosha.konfiguration_lite.Konfiguration;
import io.koosha.konfiguration_lite.KonfigurationFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Collections.singletonMap;
import static org.testng.Assert.assertEquals;

@SuppressWarnings("RedundantThrows")
public final class KonfigurationKombinerTest {

    private final AtomicBoolean flag = new AtomicBoolean(true);

    private final Map<String, ?> sup = singletonMap("xxx", 12);

    private Konfiguration k;

    @BeforeMethod
    public void setup() {
        this.flag.set(true);
        this.k = KonfigurationFactory.getInstanceV8().kombine(
            "def", KonfigurationFactory.getInstanceV8().map("map", sup));
    }

    @Test
    public void testV1() throws Exception {
        assertEquals((Object) k.int_("xxx"), 12);
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testV3() throws Exception {
        k.string("xxx");
    }


    @Test
    public void testDoublyUpdate() throws Exception {
        assertEquals(k.int_("xxx"), (Integer) 12);
    }


    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testNoDefaultValue() {
        k.long_("some bla bla bla");
    }

    @Test
    public void testDefaultValue() {
        assertEquals(k.long_("some bla bla bla", 9876L), (Long) 9876L);
    }

}
