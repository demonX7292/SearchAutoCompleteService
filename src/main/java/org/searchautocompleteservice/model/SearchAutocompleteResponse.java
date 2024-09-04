package org.searchautocompleteservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class SearchAutocompleteResponse {
    private HttpStatus status;
    private boolean error;
    private String message;
    private List<String> topKSearchedWords;
}
