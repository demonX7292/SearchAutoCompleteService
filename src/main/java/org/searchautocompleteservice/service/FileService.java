package org.searchautocompleteservice.service;


import java.io.File;
import java.util.List;

public interface FileService {
    String saveFile(File file);
    byte[] downloadFile(String filename);
    String deleteFile(String filename);
    List<String> listAllFiles();
}
