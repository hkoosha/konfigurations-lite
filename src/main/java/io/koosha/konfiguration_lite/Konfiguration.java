package io.koosha.konfiguration_lite;

import io.koosha.konfiguration_lite.type.Kind;
import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * All methods are thread-safe (and should be implemented as such).
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

    Boolean bool(@NotNull String key,
                 Boolean def);

    /**
     * Get a byte konfiguration value.
     *
     * @param key unique key of the konfiguration being requested.
     * @return konfiguration value wrapper for the requested key.
     */
    Byte byte_(@NotNull String key);

    Byte byte_(@NotNull String key,
               Byte def);

    /**
     * Get a char konfiguration value.
     *
     * @param key unique key of the konfiguration being requested.
     * @return konfiguration value wrapper for the requested key.
     */
    Character char_(@NotNull String key);

    Character char_(@NotNull String key,
                    Character def);

    /**
     * Get a short konfiguration value.
     *
     * @param key unique key of the konfiguration being requested.
     * @return konfiguration value wrapper for the requested key.
     */
    Short short_(@NotNull String key);

    Short short_(@NotNull String key,
                 Short def);

    /**
     * Get an int konfiguration value.
     *
     * @param key unique key of the konfiguration being requested.
     * @return konfiguration value wrapper for the requested key.
     */
    Integer int_(@NotNull String key);

    Integer int_(@NotNull String key,
                 Integer def);

    /**
     * Get a long konfiguration value.
     *
     * @param key unique key of the konfiguration being requested.
     * @return konfiguration value wrapper for the requested key.
     */
    Long long_(@NotNull String key);

    Long long_(@NotNull String key,
               Long def);

    /**
     * Get a float konfiguration value.
     *
     * @param key unique key of the konfiguration being requested.
     * @return konfiguration value wrapper for the requested key.
     */
    Float float_(@NotNull String key);

    Float float_(@NotNull String key,
                 Float def);

    /**
     * Get a double konfiguration value.
     *
     * @param key unique key of the konfiguration being requested.
     * @return konfiguration value wrapper for the requested key.
     */
    Double double_(@NotNull String key);

    Double double_(@NotNull String key,
                   Double def);

    /**
     * Get a string konfiguration value.
     *
     * @param key unique key of the konfiguration being requested.
     * @return konfiguration value wrapper for the requested key.
     */
    String string(@NotNull String key);

    String string(@NotNull final String key,
                  final String def);

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

    <U> List<U> list(@NotNull String key,
                     @NotNull Kind<U> type,
                     List<U> def);

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

    <U> Set<U> set(@NotNull String key,
                   @NotNull Kind<U> type,
                   Set<U> def);

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

    <U> U custom(@NotNull String key,
                 @NotNull Kind<U> type,
                 U def);

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
