package com.cx.ceph.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.cx.ceph.util.AmazonS3Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CephController {

    /**
     * 获取桶列表
     * @return
     */
    @RequestMapping("/buckets")
    @ResponseBody
    public List<Bucket> buckets(){
        AmazonS3 s3 = AmazonS3Utils.getClient();
        List<Bucket> bucketList =  s3.listBuckets();
        bucketList.forEach(System.out::println);
        return bucketList;
    }

}
