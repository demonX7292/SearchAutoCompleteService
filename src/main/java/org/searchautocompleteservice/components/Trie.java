package org.searchautocompleteservice.components;

import org.searchautocompleteservice.config.TrieConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.searchautocompleteservice.config.Constant.PREFIX_LENGTH;

@Service
public class Trie {

    private final Node root;
    private final TrieConfig trieConfig;

    public Trie(TrieConfig trieConfig) {
        root = new Node(trieConfig, "");
        this.trieConfig = trieConfig;
    }

    public synchronized void insert(String word, int frequency) {
        if (word.length() > PREFIX_LENGTH) {
            return;
        }
        List<Node> nodesToBeUpdated = new LinkedList<>();
        word = word.toLowerCase();
        Node current = root;
        nodesToBeUpdated.add(root);
        String formedWord = "";
        for (char c : word.toCharArray()) {
            int x = c - 'a';
            formedWord = formedWord + c;
            if (current.getChild()[x] == null) {
                current.getChild()[x] = new Node(trieConfig, formedWord);
            }
            current = current.getChild()[x];
            nodesToBeUpdated.add(current);
        }
        current.setFrequency(current.getFrequency() + frequency);
        updateNodes(nodesToBeUpdated, current);
    }

    private void updateNodes(List<Node> nodesToBeUpdated, Node newWord) {
        nodesToBeUpdated.forEach(node -> node.addWordIntoTopKWords(newWord));
    }

    public boolean doesWordExists(String word) {
        word = word.toLowerCase();
        Node current = root;
        for (char c : word.toCharArray()) {
            int x = c - 'a';
            if (current.getChild()[x] == null) {
                return false;
            }
            current = current.getChild()[x];
        }
        if (current.getFrequency() > 0) {
            System.out.println(current.getCurrentWord() + " exists " + current.getFrequency() + " times");
        }
        return current.getFrequency() > 0;
    }

    public boolean doesPrefixExists(String prefix) {
        prefix = prefix.toLowerCase();
        Node current = root;
        for (char c : prefix.toCharArray()) {
            int x = c - 'a';
            if (current.getChild()[x] == null) {
                return false;
            }
            current = current.getChild()[x];
        }
        return true;
    }

    public synchronized List<String> getTopKSearches(String prefix) {
        prefix = prefix.toLowerCase();
        Node current = root;
        for (char c : prefix.toCharArray()) {
            int x = c - 'a';
            if (current.getChild()[x] == null) {
                return new LinkedList<>();
            }
            current = current.getChild()[x];
        }
        List <Node> topKNodes = new java.util.ArrayList<>(current.getTopKWords().stream().toList());
        topKNodes.sort((a, b) -> b.getFrequency() - a.getFrequency());
        return topKNodes.stream().map(Node::getCurrentWord).toList();
    }

    public List<Node> getAllWords() {
        List<Node> result = new ArrayList<>();
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            Node node = q.poll();
            if (node.getFrequency() > 0) {
                result.add(node);
            }
            for (int i = 0; i < 26; i++) {
                if (node.getChild()[i] != null) {
                    q.add(node.getChild()[i]);
                }
            }
        }
        return result;
    }
}
