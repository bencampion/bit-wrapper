package uk.recurse.bitwrapper.exception;

import uk.recurse.bitwrapper.annotation.Bytes;

/**
 * Thrown when an expression could not parsed or evaluated.
 *
 * @see Bytes#offsetExp()
 * @see Bytes#lengthExp()
 */
public class BadExpressionException extends RuntimeException {

    /**
     * @param cause an exception thrown by the expression parsing and evaluation library
     */
    public BadExpressionException(Throwable cause) {
        super(cause);
    }
}
