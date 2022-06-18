package com.cx.ceph.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class S3ServiceImpl implements S3Service {

    @Autowired
    private AmazonS3 s3;

    @Override
    public List<Bucket> listBuckets() {
        return s3.listBuckets();
    }

    @Override
    public Bucket createBucket(String bucketName) {
        List<Bucket> buckets = listBuckets();
        for (Bucket bucket : buckets) {
            if (bucket.getName().equals(bucketName)) return null;
        }
        return s3.createBucket(bucketName);
    }

    @Override
    public void deleteBucket(String bucketName) {
        s3.deleteBucket(bucketName);
    }

    @Override
    public Map<String, Object> fileUpload(String bucketName, MultipartFile multipartFile) {
        String name = multipartFile.getOriginalFilename();
        long size = multipartFile.getSize();

        String fileName = name.substring(0,name.lastIndexOf("."));
        // 获取文件后缀
        String suffix = name.substring(name.lastIndexOf(".") + 1);
        //key 使用uuid随机生成
        String key = fileName +""+new Date().getTime()+"."+suffix;
        ObjectMetadata metadata = new ObjectMetadata();
        // 必须设置ContentLength
        metadata.setContentLength(size);
        try {
            PutObjectRequest request = new PutObjectRequest(bucketName, key, multipartFile.getInputStream(), metadata);
            //公有读
            s3.putObject(request.withCannedAcl(CannedAccessControlList.PublicRead));
            s3.shutdown();
            GeneratePresignedUrlRequest guRequest = new GeneratePresignedUrlRequest(bucketName, key);
            URL url = s3.generatePresignedUrl(guRequest);
            String imgUrl = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + url.getPath();
            String path = url.getPath();
            Map<String,Object> result = new HashMap<>();
            result.put("fileName", key);
            result.put("bucketName", bucketName);
            result.put("size", size);
            result.put("url", imgUrl);
            result.put("path", path);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, Object> fileDownLoad(String bucketName, String key) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key);
        URL url = s3.generatePresignedUrl(request);
        Map<String,Object> result = new HashMap<>();
        result.put("url",url);
        return result;
    }

    @Override
    public ObjectListing bucketFileList(String bucketName) {
        return s3.listObjects(bucketName);
    }
}
