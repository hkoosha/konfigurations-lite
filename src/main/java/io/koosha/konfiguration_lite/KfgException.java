package io.koosha.konfiguration_lite;

import io.koosha.konfiguration_lite.type.Kind;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static java.lang.String.format;

@SuppressWarnings("unused")
public class KfgException extends RuntimeException {

    @Nullable
    private final String source;

    @Nullable
    private final String key;

    @Nullable
    private final Kind<?> neededType;

    @Nullable
    private final String actualValue;

    public KfgException(@Nullable final String source,
                        @Nullable final Throwable cause) {
        super(cause);
        this.source = source;
        this.key = null;
        this.neededType = null;
        this.actualValue = null;
    }

    public KfgException(@Nullable final String source,
                        @Nullable final String message) {
        super(message);
        this.source = source;
        this.key = null;
        this.neededType = null;
        this.actualValue = null;
    }

    public KfgException(@Nullable final String source,
                        @Nullable final String message,
                        @Nullable final Throwable cause) {
        super(message, cause);
        this.source = source;
        this.key = null;
        this.neededType = null;
        this.actualValue = null;
    }

    public KfgException(@Nullable final String source,
                        @Nullable final String key,
                        @Nullable final Kind<?> neededType,
                        @Nullable final Object actualValue,
                        @Nullable final String message,
                        @Nullable final Throwable cause) {
        super(message, cause);
        this.source = source;
        this.key = key;
        this.neededType = neededType;
        this.actualValue = toStringOf(actualValue);
    }

    public KfgException(@Nullable final String source,
                        @Nullable final String key,
                        @Nullable final Kind<?> neededType,
                        @Nullable final Object actualValue,
                        @Nullable String message) {
        super(message);
        this.source = source;
        this.key = key;
        this.neededType = neededType;
        this.actualValue = toStringOf(actualValue);
    }

    public KfgException(@Nullable final String source,
                        @Nullable final String key,
                        @Nullable final Kind<?> neededType,
                        @Nullable final Object actualValue,
                        @Nullable final Throwable cause) {
        super(cause);
        this.source = source;
        this.key = key;
        this.neededType = neededType;
        this.actualValue = toStringOf(actualValue);
    }

    public KfgException(@Nullable final String source,
                        @Nullable final String key,
                        @Nullable final Kind<?> neededType,
                        @Nullable final Object actualValue) {
        this.source = source;
        this.key = key;
        this.neededType = neededType;
        this.actualValue = toStringOf(actualValue);
    }


    @Override
    public String toString() {
        return format("%s(key=%s, neededType=%s, actualValue=%s)",
            this.getClass().getName(),
            this.key(),
            this.neededType(),
            this.actualValue());
    }


    public boolean hasSource() {
        return this.source() != null;
    }

    public boolean hasKey() {
        return this.key() != null;
    }

    public boolean hasNeededType() {
        return this.neededType() != null;
    }

    public boolean hasActualValue() {
        return this.actualValue() != null;
    }


    @Nullable
    public String source() {
        return this.source;
    }

    @Nullable
    public String key() {
        return this.key;
    }

    @Nullable
    public Kind<?> neededType() {
        return this.neededType;
    }

    @Nullable
    public String actualValue() {
        return this.actualValue;
    }


    static String msgOf(final Throwable t) {
        return t == null
            ? "[null exception]->[null exception]"
            : format("[throwable::%s]->[%s]", t.getClass().getName(), t.getMessage());
    }

    static String toStringOf(final Object value) {
        String representationC;
        try {
            representationC = value == null ? "null" : value.getClass().getName();
        }
        catch (Throwable t) {
            representationC = "[" + "value.getClass().getName()" + "]->" + msgOf(t);
        }

        String representationV;
        try {
            representationV = Objects.toString(value);
        }
        catch (Throwable t) {
            representationV = "[" + "Objects.toString(value)" + "]->" + msgOf(t);
        }

        return format("[%s]:[%s]", representationC, representationV);
    }

}
