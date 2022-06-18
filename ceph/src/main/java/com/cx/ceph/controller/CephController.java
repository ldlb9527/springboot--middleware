package com.cx.ceph.controller;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
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

    /**
     * 文件下载 (公有读可通过url直接下载)
     * @param bucketName
     * @param key
     * @return
     */
    @RequestMapping(value = "/fileDownLoad",method = {RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> fileDownLoad(@RequestParam("bucketName") String bucketName,@RequestParam(value = "key")String key){
        return s3Service.fileDownLoad(bucketName,key);
    }

    /**
     * 文件下载 (公有读可通过url直接下载)
     * @param bucketName
     * @param key
     * @return
     */
    @RequestMapping(value = "/deleteFile",method = {RequestMethod.GET})
    @ResponseBody
    public void deleteFile(@RequestParam("bucketName") String bucketName,@RequestParam(value = "key")String key){
        s3Service.deleteFile(bucketName,key);
    }

    /**
     * 获取 bucketName下的所有文件
     * @param bucketName
     * @return
     */
    @RequestMapping(value = "/bucketFileList",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ObjectListing bucketFileList(@RequestParam(value = "bucketName")String bucketName){
        return s3Service.bucketFileList(bucketName);
    }


}
