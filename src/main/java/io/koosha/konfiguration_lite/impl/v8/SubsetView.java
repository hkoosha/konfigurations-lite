package io.koosha.konfiguration_lite.impl.v8;

import io.koosha.konfiguration_lite.KfgIllegalArgumentException;
import io.koosha.konfiguration_lite.Konfiguration;
import io.koosha.konfiguration_lite.type.Kind;
import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Read only subset view of a konfiguration. Prepends a pre-defined key
 * to all konfig values
 * <p>
 * Ignore the J prefix.
 *
 * <p>Immutable and thread safe by itself, although the underlying wrapped
 * konfiguration's thread safety is not guarantied.
 */
@ThreadSafe
@ApiStatus.Internal
final class SubsetView implements Konfiguration {

    private final String name;
    private final Konfiguration wrapped;
    private final String baseKey;

    SubsetView(@NotNull final String name,
               @NotNull final Konfiguration wrappedKonfiguration,
               @NotNull final String baseKey) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(wrappedKonfiguration, "wrappedKonfiguration");
        Objects.requireNonNull(baseKey, "baseKey");
        this.name = name;
        this.wrapped = wrappedKonfiguration;

        if (baseKey.startsWith(".")) // covers baseKey == "." too.
            throw new KfgIllegalArgumentException(this.name(), "key must not start with a dot: " + baseKey);
        if (baseKey.contains(".."))
            throw new KfgIllegalArgumentException(this.name(), "key can not contain subsequent dots: " + baseKey);

        if (baseKey.isEmpty())
            this.baseKey = "";
        else if (baseKey.endsWith("."))
            this.baseKey = baseKey;
        else
            this.baseKey = baseKey + ".";
    }


    @Override
    @Contract(pure = true)
    @NotNull
    public String name() {
        return this.name;
    }


    @Contract(pure = true)
    @Override
    public Boolean bool(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return wrapped.bool(key(key));
    }

    @Contract(pure = true)
    @Override
    public Byte byte_(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return wrapped.byte_(key(key));
    }

    @Contract(pure = true)
    @Override
    public Character char_(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return wrapped.char_(key(key));
    }

    @Contract(pure = true)
    @Override
    public Short short_(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return wrapped.short_(key(key));
    }

    @Contract(pure = true)
    @Override
    public Integer int_(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return wrapped.int_(key(key));
    }

    @Contract(pure = true)
    @NotNull
    @Override
    public Long long_(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return wrapped.long_(key(key));
    }

    @Contract(pure = true)
    @NotNull
    @Override
    public Float float_(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return wrapped.float_(key(key));
    }

    @Contract(pure = true)
    @NotNull
    @Override
    public Double double_(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return wrapped.double_(key(key));
    }

    @Contract(pure = true)
    @NotNull
    @Override
    public String string(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return wrapped.string(key(key));
    }

    @Contract(pure = true)
    @NotNull
    @Override
    public <U> List<U> list(@NotNull final String key,
                            @NotNull final Kind<U> type) {
        Objects.requireNonNull(key, "key");
        return wrapped.list(key(key), type);
    }

    @Contract(pure = true)
    @NotNull
    @Override
    public <U> Set<U> set(@NotNull final String key,
                          @NotNull final Kind<U> type) {
        Objects.requireNonNull(key, "key");
        return wrapped.set(key(key), type);
    }

    @Contract(pure = true)
    @NotNull
    @Override
    public <U> U custom(@NotNull final String key,
                        @NotNull final Kind<U> type) {
        Objects.requireNonNull(key, "key");
        return wrapped.custom(key(key), type);
    }

    @Contract(pure = true)
    @Override
    public boolean has(@NotNull final String key,
                       @NotNull final Kind<?> type) {
        Objects.requireNonNull(key, "key");
        return wrapped.has(key(key), type);
    }

    @Contract(pure = true,
              value = "_ -> _")
    @NotNull
    @Override
    public Konfiguration subset(@NotNull final String key) {
        Objects.requireNonNull(key, "key");
        return key.isEmpty()
            ? this
            : new SubsetView(
            this.name.split("::")[0] + "::" + key,
            this.wrapped,
            this.baseKey + this.key(key)
        );
    }

    @Contract(pure = true,
              value = "_ -> _")
    @NotNull
    private String key(@NotNull final String key) {
        Objects.requireNonNull(key, "key");

        if (key.startsWith("."))
            throw new KfgIllegalArgumentException(this.name(), "key must not start with a dot: " + key);

        return this.baseKey + key;
    }

}
