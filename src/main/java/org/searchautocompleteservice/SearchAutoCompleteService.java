package org.searchautocompleteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Sample input data
 * [
 *     {
 *         "word" : "bee",
 *         "frequency" : 20
 *     },
 *     {
 *         "word" : "beer",
 *         "frequency" : 10
 *     },
 *     {
 *         "word" : "best",
 *         "frequency" : 35
 *     },
 *     {
 *         "word" : "bet",
 *         "frequency" : 29
 *     },
 *     {
 *         "word" : "wish",
 *         "frequency" : 25
 *     },
 *     {
 *         "word" : "buy",
 *         "frequency" : 14
 *     },
 *     {
 *         "word" : "win",
 *         "frequency" : 50
 *     }
 * ]
 */
@SpringBootApplication
public class SearchAutoCompleteService {
    public static void main(String[] args) {
        SpringApplication.run(SearchAutoCompleteService.class, "Search Auto Complete Service");
    }
}