package com.cx.ceph.util;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.cx.ceph.config.CephConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class AmazonS3Utils {

    @Resource
    private CephConfig cephConfig;
    private static CephConfig staticCephConf;

    @PostConstruct
    public void init() {
        staticCephConf = cephConfig;
    }

    public static AmazonS3 getClient() {
        AWSCredentials credentials = new BasicAWSCredentials(staticCephConf.getAccessKey(), staticCephConf.getSecretKey());

        ClientConfiguration clientConfiguration = new ClientConfiguration();
//            clientConfiguration.setSignerOverride("AWSS3V4SignerType");//凭证验证方式S3SignerType/AWSS3V4SignerType/AWS3SignerType
//            clientConfiguration.setUserAgentPrefix("s3browser");
        clientConfiguration.setProtocol(Protocol.HTTP);
//            clientConfiguration.setMaxConnections(100);

        clientConfiguration.setRequestTimeout(10000);
        clientConfiguration.setConnectionTimeout(10000);
        System.out.println(staticCephConf.getHosts());
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(staticCephConf.getHosts(), Regions.US_EAST_1.name());//Regions.US_EAST_1.name()
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
//                    .withCredentials(credentialsProvider)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(endpointConfiguration)
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfiguration)
                .build();
        return amazonS3;
    }

    public static AmazonS3 getClient(String hosts,String accessKey,String secretKey) {
        AWSCredentialsProvider credentialsProvider = new AWSCredentialsProvider() {
            public AWSCredentials getCredentials() {
                return new BasicAWSCredentials(accessKey,secretKey);
            }
            @Override
            public void refresh() {
            }
        };
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setProtocol(Protocol.HTTP);
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(hosts, null);
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withCredentials(credentialsProvider).withEndpointConfiguration(endpointConfiguration).withClientConfiguration(clientConfiguration).build();
        return amazonS3;
    }

}
