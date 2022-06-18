package com.cx.ceph.service;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface S3Service {

    List<Bucket> listBuckets();
    Bucket createBucket(String bucketName);

    void deleteBucket(String bucketName);

    Map<String, Object> fileUpload(String bucketName, MultipartFile multipartFile);

    Map<String, Object> fileDownLoad(String bucketName, String key);

    ObjectListing bucketFileList(String bucketName);
}
