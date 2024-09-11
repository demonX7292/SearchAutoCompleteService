package org.searchautocompleteservice.components;

import lombok.Getter;
import lombok.Setter;
import org.searchautocompleteservice.config.TrieConfig;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

@Getter
@Setter
public class Node {
    private Node[] child; // 4 bytes * 26 (array size) = 104 bytes
    private int frequency; // 4 bytes
    private final TrieConfig trieConfig; // 4 bytes
    private final String currentWord; // max 50 length -> 50 * 2 = 100 bytes
    private PriorityQueue<Node> topKWords;
    private Set<String> wordSet;// max k elements -> k(5) * 4 = 20 bytes
    // total = 104 + 4 + 4 + 100 + 20 = 234 ~ 300 bytes

    public Node(TrieConfig trieConfig, String currentWord) {
        child = new Node[26];
        frequency = 0;
        wordSet = new HashSet<>();
        topKWords = new PriorityQueue<>(trieConfig.topKSearches(), Comparator.comparingInt(a -> a.frequency));
        this.trieConfig = trieConfig;
        this.currentWord = currentWord;
    }

    public void addWordIntoTopKWords(Node node) {
        if (wordSet.contains(node.getCurrentWord())) {
            return;
        }
        if (topKWords.size() < trieConfig.topKSearches()) {
            wordSet.add(node.getCurrentWord());
            topKWords.add(node);
            return;
        }
        if (topKWords.peek().getFrequency() < node.getFrequency()) {
            topKWords.poll();
            topKWords.add(node);
            wordSet.add(node.getCurrentWord());
        }
    }
}
