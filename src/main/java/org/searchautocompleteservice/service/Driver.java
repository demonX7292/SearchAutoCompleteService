package org.searchautocompleteservice.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.searchautocompleteservice.components.Node;
import org.searchautocompleteservice.components.Trie;
import org.searchautocompleteservice.config.TrieConfig;
import org.searchautocompleteservice.exceptions.InvalidRequestException;
import org.searchautocompleteservice.exceptions.SearchAutoCompleteServiceException;
import org.searchautocompleteservice.model.NewWordRequest;
import org.searchautocompleteservice.model.SearchAutocompleteResponse;
import org.searchautocompleteservice.util.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;
import java.util.Scanner;

import static org.searchautocompleteservice.config.Constant.*;

@RestController
public class Driver {

    @Autowired
    private Trie trie;
    @Autowired
    private TrieConfig trieConfig;
    @Autowired
    private S3FileService s3FileService;

    @PreDestroy
    public void destroy() {
        List<Node> allWords = trie.getAllWords();
        try {
            s3FileService.deleteFile(SEARCH_AUTOCOMPLETE_SERVICE_DATASET_FILE);
            File file = new File(SEARCH_AUTOCOMPLETE_SERVICE_DATASET_FILE);
            FileWriter myWriter = new FileWriter(SEARCH_AUTOCOMPLETE_SERVICE_DATASET_FILE);
            myWriter.write("");
            for (Node node : allWords) {
                myWriter.write(node.getCurrentWord() + " " + node.getFrequency() + "\n");
            }
            myWriter.close();
            System.out.println(s3FileService.saveFile(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    private void initializeTrie() {
        byte[] inputStream = s3FileService.downloadFile(SEARCH_AUTOCOMPLETE_SERVICE_DATASET_FILE);

        try {
            File file = new File(SEARCH_AUTOCOMPLETE_SERVICE_DATASET_FILE);
            FileOutputStream fos = new FileOutputStream(SEARCH_AUTOCOMPLETE_SERVICE_DATASET_FILE);
            fos.write(inputStream);
            fos.close();
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split(" ");
                String word = data[0];
                int frequency = Integer.parseInt(data[1]);
                trie.insert(word, frequency);
            }
            myReader.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
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

    @RequestMapping(method = RequestMethod.POST, value = "/search/newWords")
    public ResponseEntity<SearchAutocompleteResponse> addWords(@RequestBody List<NewWordRequest> requests) {
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
