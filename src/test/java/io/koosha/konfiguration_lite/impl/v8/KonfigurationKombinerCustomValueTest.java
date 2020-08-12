package io.koosha.konfiguration_lite.impl.v8;

import io.koosha.konfiguration_lite.Konfiguration;
import io.koosha.konfiguration_lite.KonfigurationFactory;
import io.koosha.konfiguration_lite.TestUtil;
import io.koosha.konfiguration_lite.type.Kind;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;

@SuppressWarnings("WeakerAccess")
public class KonfigurationKombinerCustomValueTest {

    final TestUtil.DummyCustom value = new TestUtil.DummyCustom();

    final String key = "theKey";

    private final Konfiguration k = KonfigurationFactory.getInstanceV8().kombine("def", KonfigurationFactory.getInstanceV8().map(
        "meName",
        Collections.singletonMap(key, value)
    ));

    @Test
    public void testCustomValue() {
        TestUtil.DummyCustom custom = k.custom(key, Kind.of(TestUtil.DummyCustom.class));
        Assert.assertSame(custom, value);
    }

}
