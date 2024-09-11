package org.searchautocompleteservice.util;


import org.searchautocompleteservice.exceptions.InvalidPrefixException;
import org.searchautocompleteservice.exceptions.InvalidRequestException;
import org.searchautocompleteservice.exceptions.MoreThanThresholdException;
import org.searchautocompleteservice.exceptions.SearchAutoCompleteServiceException;
import org.searchautocompleteservice.model.NewWordRequest;

import java.util.List;

import static org.searchautocompleteservice.config.Constant.PREFIX_LENGTH;

public class RequestValidator {

    private RequestValidator(){}

    public static List<NewWordRequest> filterRequests(List<NewWordRequest> requests) throws InvalidRequestException {
        List <NewWordRequest> filteredRequests = requests.stream()
                .filter(request -> (request.getWord().length() < PREFIX_LENGTH
                        && checkWordForInvalidCharacters(request.getWord())
                        && request.getFrequency() > 0)).toList();
        if (filteredRequests.isEmpty()) {
            throw new InvalidRequestException();
        }
        return filteredRequests;
    }

    private static boolean checkWordForInvalidCharacters(String word) {
        for (char c : word.toCharArray()) {
            int x = c - 'a';
            if (x < 0 || x > 25) {
                return false;
            }
        }
        return true;
    }

    public static void validateSearchedPrefix(String prefix) throws SearchAutoCompleteServiceException {
        if (!checkWordForInvalidCharacters(prefix)) {
            throw new InvalidPrefixException();
        }
        if (prefix.length() > 50) {
            throw new MoreThanThresholdException();
        }
    }
}
