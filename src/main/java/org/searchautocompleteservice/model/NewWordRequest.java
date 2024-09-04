package org.searchautocompleteservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NewWordRequest {
    private String word;
    private int frequency;
}
