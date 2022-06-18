package com.cx.ceph.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

    @Autowired
    private CephConfig cephConfig;

    @Bean(name = "amazonS3")
    public AmazonS3 getAmazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials(cephConfig.getAccessKey(), cephConfig.getSecretKey());

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setProtocol(Protocol.HTTP);//设置协议
        clientConfiguration.setMaxConnections(100);//设置AmazonS3使用的最大连接数
        clientConfiguration.setRequestTimeout(10000);
        clientConfiguration.setConnectionTimeout(10000);
        System.out.println(cephConfig.getHosts());
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(cephConfig.getHosts(), Regions.US_EAST_1.name());//Regions.US_EAST_1.name()
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(endpointConfiguration)
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfiguration)
                .build();
        return amazonS3;
    }

}
