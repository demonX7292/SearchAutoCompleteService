package org.searchautocompleteservice.exceptions;

import static org.searchautocompleteservice.config.Constant.INVALID_REQUEST_EXCEPTION;

public class InvalidRequestException extends SearchAutoCompleteServiceException {
    public InvalidRequestException() {
        super(INVALID_REQUEST_EXCEPTION);
    }
}
