package org.searchautocompleteservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3FileService implements FileService {

    @Value("${bucketName}")
    private String bucketName;
    private final AmazonS3 s3;

    public S3FileService(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public String saveFile(File file) {
        String originalFilename = file.getName();
        PutObjectResult putObjectResult = s3.putObject(bucketName, originalFilename, file);
        return putObjectResult.getContentMd5();
    }

    @Override
    public byte[] downloadFile(String filename) {
        S3Object object = s3.getObject(bucketName, filename);
        S3ObjectInputStream objectContent = object.getObjectContent();
        try {
            return IOUtils.toByteArray(objectContent);
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    public String deleteFile(String filename) {
        s3.deleteObject(bucketName,filename);
        return "File deleted";
    }

    @Override
    public List<String> listAllFiles() {
        ListObjectsV2Result listObjectsV2Result = s3.listObjectsV2(bucketName);
        return listObjectsV2Result
                .getObjectSummaries()
                .stream().map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }
}
