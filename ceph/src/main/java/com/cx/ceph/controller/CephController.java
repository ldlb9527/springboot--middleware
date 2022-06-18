package com.cx.ceph.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.cx.ceph.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
public class CephController {

    @Autowired
    private S3Service s3Service;

    /**
     * 获取桶列表
     * @return
     */
    @RequestMapping( "/listBuckets")
    @ResponseBody
    public List<Bucket> buckets(){
        return s3Service.listBuckets();
    }

    /**
     * 创建桶，如果存在则返回null
     * @param bucketName
     * @return
     */
    @RequestMapping( "/createBucket")
    @ResponseBody
    public Bucket createBucket(@RequestParam String bucketName){
        return s3Service.createBucket(bucketName);
    }

    /**
     * 删除桶
     * @param bucketName
     * @return
     */
    @RequestMapping( "/deleteBucket")
    @ResponseBody
    public void deleteBucket(@RequestParam String bucketName){
        s3Service.deleteBucket(bucketName);
    }

    /**
     * 文件上传
     * @param multipartFile
     * @param bucketName
     * @return
     */
    @RequestMapping(value = "/fileUpload",method = {RequestMethod.POST})
    @ResponseBody
    public Map<String,Object> fileUpload(@RequestParam("file") MultipartFile multipartFile, @RequestParam(value = "bucketName")String bucketName){
        return s3Service.fileUpload(bucketName,multipartFile);
    }

}
