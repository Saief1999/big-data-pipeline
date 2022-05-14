package com.example.importmicroservice.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class WasabiUploader {

    @Value("${wasabi.service_endpoint}")
    private String serviceEndpoint;

    @Value("${wasabi.region}")
    private String region;

    @Value("${wasabi.access_key}")
    private String accessKey;

    @Value("${wasabi.secret_key}")
    private String secretKey;
    @Value("${wasabi.bucket_name}")
    private String bucketName;

    private AmazonS3 amazonS3Client;

    @PostConstruct
    public void init() {
        this.amazonS3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }
    public String uploadFile(String filename, byte[] data) throws IOException {
        try (InputStream inputStream = new ByteArrayInputStream(data)) {


            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(data.length);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filename, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3Client.putObject(putObjectRequest);

            return amazonS3Client.getUrl(bucketName, filename).toString();
        }

    }

}
