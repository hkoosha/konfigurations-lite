package io.koosha.konfiguration_lite;

import io.koosha.konfiguration_lite.type.Kind;
import org.jetbrains.annotations.Nullable;

/**
 * Exceptions regarding the source (backing storage), or as a wrapper around
 * exceptions thrown by the backing storage.
 */
public class KfgSourceException extends KfgException {

    public KfgSourceException(@Nullable final String source,
                              @Nullable final String message) {
        super(source, null, null, null, message);
    }

    public KfgSourceException(@Nullable final String source,
                              @Nullable final String message,
                              @Nullable final Throwable cause) {
        super(source, null, null, null, message, cause);
    }


    public KfgSourceException(@Nullable final String source,
                              @Nullable final String key,
                              @Nullable final Kind<?> neededType,
                              @Nullable final Object actualValue,
                              @Nullable final String message,
                              @Nullable final Throwable cause) {
        super(source, key, neededType, actualValue, message, cause);
    }

    public KfgSourceException(@Nullable final String source,
                              @Nullable final String key,
                              @Nullable final Kind<?> neededType,
                              @Nullable final Object actualValue,
                              @Nullable final String message) {
        super(source, key, neededType, actualValue, message);
    }

    public KfgSourceException(@Nullable final String source,
                              @Nullable final String key,
                              @Nullable final Kind<?> neededType,
                              @Nullable final Object actualValue,
                              @Nullable final Throwable cause) {
        super(source, key, neededType, actualValue, cause);
    }

    public KfgSourceException(@Nullable final String source,
                              @Nullable final String key,
                              @Nullable final Kind<?> neededType,
                              @Nullable final Object actualValue) {
        super(source, key, neededType, actualValue);
    }

}
