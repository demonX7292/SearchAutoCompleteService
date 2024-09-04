package org.searchautocompleteservice.exceptions;

import static org.searchautocompleteservice.config.Constant.INVALID_SEARCH_FOR_TRIE_EXCEPTION;

public class InvalidPrefixException extends SearchAutoCompleteServiceException {
    public InvalidPrefixException() {
        super(INVALID_SEARCH_FOR_TRIE_EXCEPTION);
    }
}
