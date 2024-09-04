package org.searchautocompleteservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class TrieWordInsertResponse {
    private HttpStatus status;
    private boolean error;
    private String message;
}
