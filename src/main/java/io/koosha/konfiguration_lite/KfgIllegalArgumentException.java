package io.koosha.konfiguration_lite;

import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.Nullable;

public class KfgIllegalArgumentException extends IllegalArgumentException {

    @Nullable
    private final String source;

    public KfgIllegalArgumentException(@Nullable final String source,
                                       final String message) {
        super(message);
        this.source = source;
    }

    @Nullable
    public String getSource() {
        return this.source;
    }

}
