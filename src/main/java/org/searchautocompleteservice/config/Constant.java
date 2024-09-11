package org.searchautocompleteservice.config;

public class Constant {
    public static final int TOP_K = 5;
    public static final int PREFIX_LENGTH = 50;
    public static final String SUCCESS_RESPONSE_MESSAGE = "Top " + TOP_K + " searched words are : ";
    public static final String FAILURE_RESPONSE_MESSAGE = "Not Available";
    public static final String MORE_THAN_THRESHOLD_EXCEPTION_MESSAGE =
            "Input word can't be stored in trie, because it's length exceeds the threshold";
    public static final String INVALID_REQUEST_EXCEPTION =
            "No valid input word for trie. Check the input for the word should contain only" +
                    "characters from a to z and should not be more than 50 characters long" +
                    "and the frequency should be above 0 for a word";
    public static final String INVALID_SEARCH_FOR_TRIE_EXCEPTION =
            "invalid character present in the prefix";
    public static final String SEARCH_AUTOCOMPLETE_SERVICE_DATASET_FILE =
            "trie_input_dataset_v0.txt";
}
