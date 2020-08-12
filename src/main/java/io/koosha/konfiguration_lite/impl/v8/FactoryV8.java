package io.koosha.konfiguration_lite.impl.v8;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.koosha.konfiguration_lite.Konfiguration;
import io.koosha.konfiguration_lite.KonfigurationFactory;
import io.koosha.konfiguration_lite.ext.v8.ExtJacksonJsonSource;
import io.koosha.konfiguration_lite.ext.v8.ExtMapSource;
import io.koosha.konfiguration_lite.ext.v8.ExtPreferencesSource;
import io.koosha.konfiguration_lite.ext.v8.ExtYamlSource;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.prefs.Preferences;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.unmodifiableMap;

@ThreadSafe
@Immutable
@ApiStatus.Internal
public final class FactoryV8 implements KonfigurationFactory {

    private static final String VERSION = "io.koosha.konfiguration_lite:8.0.0";

    @Contract(pure = true)
    @NotNull
    public static KonfigurationFactory getInstance() {
        return new FactoryV8();
    }

    private FactoryV8() {
    }

    @Override
    @Contract(pure = true)
    @NotNull
    public String getVersion() {
        return VERSION;
    }

    // ================================================================ KOMBINER

    @Override
    @Contract("_, _ -> new")
    @NotNull
    public Konfiguration kombine(@NotNull final String name,
                                 @NotNull final Konfiguration source) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(source, "source");
        return kombine(name, singleton(source));
    }

    @Override
    @NotNull
    @Contract("_, _, _ -> new")
    public Konfiguration kombine(@NotNull final String name,
                                 @NotNull final Konfiguration source,
                                 @NotNull final Konfiguration... rest) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(rest, "sources");
        final List<Konfiguration> l = new ArrayList<>();
        l.add(source);
        l.addAll(asList(rest));
        return new Kombiner(name, l);
    }

    @Override
    @NotNull
    @Contract("_, _ -> new")
    public Konfiguration kombine(@NotNull final String name,
                                 @NotNull final Collection<Konfiguration> sources) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(sources, "sources");
        return new Kombiner(name, sources);
    }

    // ==================================================================== MAP

    @Override
    @NotNull
    @Contract(pure = true,
              value = "_, _ -> new")
    public Konfiguration map(@NotNull final String name,
                             @NotNull final Map<String, ?> storage) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(storage, "storage");
        final Map<String, ?> copy = unmodifiableMap(new HashMap<>(storage));
        return new ExtMapSource(name, copy);
    }

    // ============================================================ PREFERENCES

    @Override
    @NotNull
    @Contract("_, _ -> new")
    public Konfiguration preferences(@NotNull final String name,
                                     @NotNull final Preferences storage) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(storage, "storage");
        final Konfiguration k = new ExtPreferencesSource(name, storage);
        return kombine(name, k);
    }

    // ================================================================ JACKSON

    @Override
    @NotNull
    @Contract("_, _ -> new")
    public Konfiguration jacksonJson(@NotNull final String name,
                                     @NotNull final String json) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(json, "json");
        return new ExtJacksonJsonSource(name, json);
    }

    @Override
    @NotNull
    @Contract("_, _, _ -> new")
    public Konfiguration jacksonJson(@NotNull final String name,
                                     @NotNull final String json,
                                     @NotNull final Supplier<ObjectMapper> objectMapper) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(json, "json");
        Objects.requireNonNull(objectMapper, "objectMapper");
        return new ExtJacksonJsonSource(name, json, objectMapper);
    }

    // ============================================================= SNAKE YAML

    @Override
    @NotNull
    @Contract("_, _ -> new")
    public Konfiguration snakeYaml(@NotNull final String name,
                                   @NotNull final String yaml) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(yaml, "yaml");
        return new ExtYamlSource(name, yaml);
    }

    @Override
    @NotNull
    @Contract("_, _, _ -> new")
    public Konfiguration snakeYaml(@NotNull final String name,
                                   @NotNull final String yaml,
                                   @NotNull final Supplier<Yaml> objectMapper) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(yaml, "yaml");
        Objects.requireNonNull(objectMapper, "objectMapper");
        return new ExtYamlSource(name, yaml, objectMapper);
    }

}
