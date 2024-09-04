package org.searchautocompleteservice.components;

import lombok.Getter;
import lombok.Setter;
import org.searchautocompleteservice.config.Configuration;

import java.util.Comparator;
import java.util.PriorityQueue;

@Getter
@Setter
public class Node {
    private Node[] child; // 4 bytes * 26 (array size) = 104 bytes
    private int frequency; // 4 bytes
    private final Configuration configuration; // 4 bytes
    private final String currentWord; // max 50 length -> 50 * 2 = 100 bytes
    private PriorityQueue<Node> topKWords; // max k elements -> k(5) * 4 = 20 bytes
    // total = 104 + 4 + 4 + 100 + 20 = 234 ~ 300 bytes

    public Node(Configuration configuration, String currentWord) {
        child = new Node[26];
        frequency = 0;
        topKWords = new PriorityQueue<>(configuration.topKSearches(), Comparator.comparingInt(a -> a.frequency));
        this.configuration = configuration;
        this.currentWord = currentWord;
    }

    public void addWordIntoTopKWords(Node node) {
        if (topKWords.size() < configuration.topKSearches()) {
            topKWords.add(node);
            return;
        }
        if (topKWords.peek().getFrequency() < node.getFrequency()) {
            topKWords.poll();
            topKWords.add(node);
        }
    }
}
