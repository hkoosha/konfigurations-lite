package io.koosha.konfiguration_lite;

import io.koosha.konfiguration_lite.type.Kind;
import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * All methods are thread-safe (and should be implemented as such).
 * <p>
 * Entry point to this library is at {@link KonfigurationFactory#getInstanceV8()}.
 */
@SuppressWarnings("unused")
@ThreadSafe
public interface Konfiguration {

    /**
     * Get a boolean konfiguration value.
     *
     * @param key unique key of the konfiguration being requested.
     * @return konfiguration value wrapper for the requested key.
     */
    Boolean bool(@NotNull String key);

    default Boolean bool(@NotNull final String key,
                         final Boolean def) {
        return this.has(key, Kind.of(boolean.class).withKey(key)) ? bool(key) : def;
    }


    /**
     * Get a byte konfiguration value.
     *
     * @param key unique key of the konfiguration being requested.
     * @return konfiguration value wrapper for the requested key.
     */
    Byte byte_(@NotNull String key);

    default Byte byte_(@NotNull final String key,
                       final Byte def) {
        return this.has(key, Kind.of(byte.class).withKey(key)) ? byte_(key) : def;
    }

    /**
     * Get a char konfiguration value.
     *
     * @param key unique key of the konfiguration being requested.
     * @return konfiguration value wrapper for the requested key.
     */
    Character char_(@NotNull String key);

    default Character char_(@NotNull final String key,
                            final Character def) {
        return this.has(key, Kind.of(char.class).withKey(key)) ? char_(key) : def;
    }

    /**
     * Get a short konfiguration value.
     *
     * @param key unique key of the konfiguration being requested.
     * @return konfiguration value wrapper for the requested key.
     */
    Short short_(@NotNull String key);

    default Short short_(@NotNull final String key,
                         final Short def) {
        return this.has(key, Kind.of(short.class).withKey(key)) ? short_(key) : def;
    }

    /**
     * Get an int konfiguration value.
     *
     * @param key unique key of the konfiguration being requested.
     * @return konfiguration value wrapper for the requested key.
     */
    Integer int_(@NotNull String key);

    default Integer int_(@NotNull final String key,
                         final Integer def) {
        return this.has(key, Kind.of(int.class).withKey(key)) ? int_(key) : def;
    }

    /**
     * Get a long konfiguration value.
     *
     * @param key unique key of the konfiguration being requested.
     * @return konfiguration value wrapper for the requested key.
     */
    Long long_(@NotNull String key);

    default Long long_(@NotNull final String key,
                       final Long def) {
        return this.has(key, Kind.of(long.class).withKey(key)) ? int_(key) : def;
    }

    /**
     * Get a float konfiguration value.
     *
     * @param key unique key of the konfiguration being requested.
     * @return konfiguration value wrapper for the requested key.
     */
    Float float_(@NotNull String key);

    default Float float_(@NotNull final String key,
                         final Float def) {
        return this.has(key, Kind.of(float.class).withKey(key)) ? int_(key) : def;
    }

    /**
     * Get a double konfiguration value.
     *
     * @param key unique key of the konfiguration being requested.
     * @return konfiguration value wrapper for the requested key.
     */
    Double double_(@NotNull String key);

    default Double double_(@NotNull final String key,
                           final Double def) {
        return this.has(key, Kind.of(double.class).withKey(key)) ? int_(key) : def;
    }

    /**
     * Get a string konfiguration value.
     *
     * @param key unique key of the konfiguration being requested.
     * @return konfiguration value wrapper for the requested key.
     */
    String string(@NotNull String key);

    default String string(@NotNull final String key,
                          final String def) {
        return this.has(key, Kind.of(String.class).withKey(key)) ? string(key) : def;
    }

    /**
     * Get a list of U konfiguration value.
     *
     * @param key  unique key of the konfiguration being requested.
     * @param type type object of values in the list.
     * @param <U>  generic type of elements in the list.
     * @return konfiguration value wrapper for the requested key.
     */
    <U> List<U> list(@NotNull String key,
                     @NotNull Kind<U> type);

    default <U> List<U> list(@NotNull final String key,
                             @NotNull final Kind<U> type,
                             final List<U> def) {
        return this.has(key, type.withKey(key)) ? list(key, type) : def;
    }

    /**
     * Get a set of konfiguration value.
     *
     * @param key  unique key of the konfiguration being requested.
     * @param type type object of values in the set.
     * @param <U>  generic type of elements in the set.
     * @return konfiguration value wrapper for the requested key.
     */
    <U> Set<U> set(@NotNull String key,
                   @NotNull Kind<U> type);

    default <U> Set<U> set(@NotNull final String key,
                           @NotNull final Kind<U> type,
                           final Set<U> def) {
        return this.has(key, type.withKey(key)) ? set(key, type) : def;
    }

    /**
     * Get a custom object of type Q konfiguration value.
     *
     * <p><b>Important:</b> the underlying konfiguration source must support
     * this!
     *
     * <p><b>Important:</b> this method must <em>NOT</em> be used to obtain
     * maps, lists or sets. Use the corresponding methods
     * {@link #list(String, Kind)} and * {@link #set(String, Kind)}.
     *
     * @param key  unique key of the konfiguration being requested.
     * @param type type object of the requested value.
     * @param <U>  generic type of requested value.
     * @return konfiguration value wrapper for the requested key.
     */
    <U> U custom(@NotNull String key,
                 @NotNull Kind<U> type);

    default <U> U custom(@NotNull String key,
                         @NotNull Kind<U> type,
                         final U def) {
        return has(key, type.withKey(key)) ? custom(key, type) : def;
    }

    // =========================================================================

    /**
     * Check if {@code key} exists in the configuration.
     *
     * @param key  the config key to check it's existence
     * @param type type of konfiguration value.
     * @return true if the key exists, false otherwise.
     */
    @Contract(pure = true)
    boolean has(@NotNull String key,
                @NotNull Kind<?> type);

    /**
     * Get a subset view of this konfiguration representing all the values under
     * the namespace of supplied key.
     *
     * @param key the key to which the scope of returned konfiguration is
     *            limited.
     * @return a konfiguration whose scope is limited to the supplied key.
     */
    @SuppressWarnings("UnusedReturnValue")
    @NotNull
    @Contract(pure = true)
    Konfiguration subset(@NotNull String key);


    // =========================================================================

    /**
     * Name of this konfiguration. Helps with debugging and readability.
     *
     * @return Name of this configuration.
     */
    @NotNull
    @Contract(pure = true)
    String name();

}
