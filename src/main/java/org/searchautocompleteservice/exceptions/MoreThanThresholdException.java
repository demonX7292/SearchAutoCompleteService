package org.searchautocompleteservice.exceptions;

import static org.searchautocompleteservice.config.Constant.MORE_THAN_THRESHOLD_EXCEPTION_MESSAGE;

public class MoreThanThresholdException extends SearchAutoCompleteServiceException {
    public MoreThanThresholdException() {
        super(MORE_THAN_THRESHOLD_EXCEPTION_MESSAGE);
    }
}
