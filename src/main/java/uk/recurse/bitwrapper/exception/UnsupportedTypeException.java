package uk.recurse.bitwrapper.exception;

import java.util.function.Function;

/**
 * Thrown when no decoder exists for the type being decoded.
 *
 * @see uk.recurse.bitwrapper.Wrapper.Builder#addDecoder(Class, Function)
 */
public class UnsupportedTypeException extends RuntimeException {

    private final Class<?> type;

    /**
     * @param type the type that could not be decoded
     */
    public UnsupportedTypeException(Class<?> type) {
        this.type = type;
    }

    @Override
    public String getMessage() {
        return type + " is not a supported type";
    }
}
