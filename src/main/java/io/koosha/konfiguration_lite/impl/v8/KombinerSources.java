package io.koosha.konfiguration_lite.impl.v8;

import io.koosha.konfiguration_lite.Konfiguration;
import io.koosha.konfiguration_lite.type.Kind;
import net.jcip.annotations.NotThreadSafe;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@NotThreadSafe
@ApiStatus.Internal
final class KombinerSources {

    @NotNull
    private final Kombiner origin;

    @NotNull
    private final List<Konfiguration> sources;

    boolean has(@NotNull final String key,
                @NotNull final Kind<?> type) {
        Objects.requireNonNull(key, "key");
        return this.sources
            .stream()
            .filter(x -> x != this.origin)
            .anyMatch(k -> k.has(key, type));
    }

    KombinerSources(@NotNull final Kombiner origin,
                    @NotNull final List<Konfiguration> sources) {
        Objects.requireNonNull(origin, "origin");
        this.origin = origin;
        this.sources = sources;
    }

    @Contract(pure = true)
    @NotNull
    Collection<Konfiguration> sources() {
        return Collections.unmodifiableList(sources);
    }

}
