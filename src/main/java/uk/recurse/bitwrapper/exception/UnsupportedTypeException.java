package uk.recurse.bitwrapper.exception;

public class UnsupportedTypeException extends RuntimeException {

    private final Class<?> type;

    public UnsupportedTypeException(Class<?> type) {
        this.type = type;
    }

    @Override
    public String getMessage() {
        return type + " is not a supported type";
    }
}
