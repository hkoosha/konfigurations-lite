package io.koosha.konfiguration_lite.impl.v8;

import io.koosha.konfiguration_lite.KfgIllegalArgumentException;
import io.koosha.konfiguration_lite.KfgMissingKeyException;
import io.koosha.konfiguration_lite.Konfiguration;
import io.koosha.konfiguration_lite.type.Kind;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Almost Thread-safe, <b>NOT</b> immutable.
 */
final class Kombiner implements Konfiguration {

    private final Object LOCK = new Object();

    @NotNull
    private final String name;

    @NotNull
    final KombinerSources sources;

    private final Map<Kind<?>, ? super Object> cache = new HashMap<>();

    Kombiner(@NotNull final String name,
             @NotNull final Collection<Konfiguration> sources) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(sources, "sources");

        this.name = name;

        final List<Konfiguration> newSources = sources
            .stream()
            .peek(source -> Objects.requireNonNull(source, "null in config sources"))
            .peek(source -> {
                if (source instanceof SubsetView)
                    throw new KfgIllegalArgumentException(
                        name, "can not kombine a " + source.getClass().getName() + " konfiguration.");
            })
            .flatMap(source ->
                source instanceof Kombiner
                    ? ((Kombiner) source).sources.sources().stream()
                    : Stream.of(source))
            .collect(Collectors.toList());
        if (newSources.isEmpty())
            throw new KfgIllegalArgumentException(name, "no source given");

        this.sources = new KombinerSources(this, newSources);
    }

    <T> T r(@NotNull final Supplier<T> func) {
        Objects.requireNonNull(func, "func");
        synchronized (LOCK) {
            return func.get();
        }
    }

    <T> T w(@NotNull final Supplier<T> func) {
        Objects.requireNonNull(func, "func");
        synchronized (LOCK) {
            return func.get();
        }
    }

    // =========================================================================

    <U> U k(@NotNull final String key,
            @NotNull final Kind<U> type) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(type, "type");

        final Kind<?> withKey = ((Kind<?>) type).withKey(key);

        return this.w(() -> {
            if (!this.cache.containsKey(withKey))
                this.cache.put(withKey, issueValue(withKey));
            @SuppressWarnings("unchecked")
            final U u = (U) this.cache.get(withKey);
            return u;
        });
    }

    @Nullable
    Object issueValue(@NotNull final Kind<?> key) {
        final String keyStr = key.key();
        Objects.requireNonNull(keyStr, "key passed through kombiner is null");
        final Optional<Konfiguration> find = this
            .sources
            .sources()
            .stream()
            .filter(source -> source.has(keyStr, key))
            .findFirst();
        if (!find.isPresent())
            throw new KfgMissingKeyException(this.name(), keyStr, key);
        final Object value = find.get().custom(keyStr, key);
        this.cache.put(key, value);
        return value;
    }

    boolean hasInCache(@NotNull final Kind<?> t) {
        Objects.requireNonNull(t.key());
        return this.cache.containsKey(t);
    }

    // =========================================================================

    @Override
    @Contract(pure = true)
    @NotNull
    public String name() {
        return this.name;
    }

    // =========================================================================

    @Override
    public Boolean bool(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return this.k(key, Kind.BOOL);
    }

    @Override
    public Byte byte_(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return this.k(key, Kind.BYTE);
    }

    @Override
    public Character char_(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return this.k(key, Kind.CHAR);
    }

    @Override
    public Short short_(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return this.k(key, Kind.SHORT);
    }

    @Override
    public Integer int_(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return this.k(key, Kind.INT);
    }

    @Override
    public Long long_(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return this.k(key, Kind.LONG);
    }

    @Override
    public Float float_(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return this.k(key, Kind.FLOAT);
    }

    @Override
    public Double double_(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return this.k(key, Kind.DOUBLE);
    }

    @Override
    public String string(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return this.k(key, Kind.STRING);
    }

    @Override
    public <U> List<U> list(@NotNull final String key,
                            @NotNull final Kind<U> type) {
        Objects.requireNonNull(key, "key");
        return this.k(key, type.asList());
    }

    @Override
    public <U> Set<U> set(@NotNull final String key,
                          @NotNull final Kind<U> type) {
        Objects.requireNonNull(key, "key");
        return this.k(key, type.asSet());
    }

    @Override
    public <U> U custom(@NotNull final String key,
                        @NotNull final Kind<U> type) {
        Objects.requireNonNull(key, "key");
        return this.k(key, type);
    }

    @Override
    public boolean has(@NotNull final String key,
                       @NotNull final Kind<?> type) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(key, "key");
        final Kind<?> t = type.withKey(key);
        return r(() -> this.hasInCache(t) || this.sources.has(key, type));
    }


    @Override
    @NotNull
    public final Konfiguration subset(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return new SubsetView(this.name() + "::" + key, this, key);
    }

}
