package org.searchautocompleteservice.exceptions;


import lombok.Getter;

import java.io.IOException;

@Getter
public class SearchAutoCompleteServiceException extends IOException {
    private final String exceptionMessage;
    public SearchAutoCompleteServiceException(String message) {
        super(message);
        exceptionMessage = message;
    }
}
