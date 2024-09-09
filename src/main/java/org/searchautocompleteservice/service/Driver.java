package org.searchautocompleteservice.service;

import org.searchautocompleteservice.components.Trie;
import org.searchautocompleteservice.config.Configuration;
import org.searchautocompleteservice.exceptions.InvalidRequestException;
import org.searchautocompleteservice.exceptions.SearchAutoCompleteServiceException;
import org.searchautocompleteservice.model.NewWordRequest;
import org.searchautocompleteservice.model.SearchAutocompleteResponse;
import org.searchautocompleteservice.util.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static org.searchautocompleteservice.config.Constant.*;

@RestController
public class Driver {

    private final Trie trie;
    private final Configuration configuration;

    @Autowired
    public Driver() {
        configuration = new Configuration(TOP_K);
        trie = new Trie(configuration);
        initializeTrie(trie);
    }

    @RequestMapping("/search")
    public ResponseEntity<SearchAutocompleteResponse> getTopKSearchesForEmptyString() {
        SearchAutocompleteResponse response = buildSearchAutocompleteResponse("");
        return new ResponseEntity<>(response, response.getStatus());
    }

    @RequestMapping("/search/{prefix}")
    public ResponseEntity<SearchAutocompleteResponse> getTopKSearches(@PathVariable String prefix) {
        SearchAutocompleteResponse response = buildSearchAutocompleteResponse(prefix);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/search/newWord/{word}")
    public ResponseEntity<SearchAutocompleteResponse> addBook(@PathVariable String word) {
        NewWordRequest request = new NewWordRequest(word, 1);
        try {
            RequestValidator.filterRequests(Collections.singletonList(request));
            trie.insert(request.getWord(), request.getFrequency());
        } catch (InvalidRequestException e) {
            return new ResponseEntity<>(SearchAutocompleteResponse.builder()
                    .message(e.getMessage())
                    .error(true)
                    .topKSearchedWords(null)
                    .build(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(SearchAutocompleteResponse.builder()
                .message("words successfully inserted into trie")
                .topKSearchedWords(null)
                .build(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/search/newWords")
    public ResponseEntity<SearchAutocompleteResponse> addBooks(@RequestBody List<NewWordRequest> requests) {
        try {
            List<NewWordRequest> filteredRequests = RequestValidator.filterRequests(requests);
            filteredRequests.forEach(request -> trie.insert(request.getWord(), request.getFrequency()));
        } catch (InvalidRequestException e) {
            return new ResponseEntity<>(SearchAutocompleteResponse.builder()
                    .message(e.getMessage())
                    .error(true)
                    .topKSearchedWords(null)
                    .build(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(SearchAutocompleteResponse.builder()
                .message("words successfully inserted into trie")
                .topKSearchedWords(null)
                .build(), HttpStatus.OK);
    }

    private void initializeTrie(Trie trie) {
        try {
            File myObj = new File("C:\\Users\\Karan\\Desktop\\searchAutoCompleteInput.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split(" ");
                String word = data[0];
                int frequency = Integer.parseInt(data[1]);
                trie.insert(word, frequency);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private SearchAutocompleteResponse buildSearchAutocompleteResponse(String prefix) {
        try {
            RequestValidator.validateSearchedPrefix(prefix);
            List<String> topKSearches = trie.getTopKSearches(prefix);
            if (topKSearches.isEmpty()) {
                return SearchAutocompleteResponse.builder()
                        .status(HttpStatus.OK)
                        .message(FAILURE_RESPONSE_MESSAGE)
                        .error(true)
                        .topKSearchedWords(topKSearches)
                        .build();
            }
            return SearchAutocompleteResponse.builder()
                    .status(HttpStatus.OK)
                    .message(SUCCESS_RESPONSE_MESSAGE)
                    .topKSearchedWords(topKSearches)
                    .build();
        } catch (SearchAutoCompleteServiceException e) {
            return SearchAutocompleteResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message(e.getMessage())
                    .error(true)
                    .topKSearchedWords(null)
                    .build();
        }
    }
}
