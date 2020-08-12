package io.koosha.konfiguration_lite.impl.v8;

import io.koosha.konfiguration_lite.KfgMissingKeyException;
import io.koosha.konfiguration_lite.Konfiguration;
import io.koosha.konfiguration_lite.KonfigurationFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static java.util.Collections.singletonMap;

public class KonfigurationMissingKeyTest {

    private final Map<String, ?> sup = singletonMap("xxx", (Object) 12);

    private Konfiguration k;

    @BeforeMethod
    public void setup() {
        this.k = KonfigurationFactory.getInstanceV8().map("map", sup);
    }

    @Test(expectedExceptions = KfgMissingKeyException.class)
    public void testMissingKeyThrowsException() {
        k.string("i.do.not.exist");
    }

}
