package org.searchautocompleteservice.components;


import org.searchautocompleteservice.config.Configuration;

import java.util.LinkedList;
import java.util.List;
import static org.searchautocompleteservice.config.Constant.PREFIX_LENGTH;

public class Trie {

    private final Node root;
    private final Configuration configuration;

    public Trie(Configuration configuration) {
        root = new Node(configuration, "");
        this.configuration = configuration;
    }

    public void insert(String word, int frequency) {
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
                current.getChild()[x] = new Node(configuration, formedWord);
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

    public List<String> getTopKSearches(String prefix) {
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
}
