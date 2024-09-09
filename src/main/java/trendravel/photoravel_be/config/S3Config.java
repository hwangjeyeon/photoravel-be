package trendravel.photoravel_be.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class S3Config {

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String accessSecret;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Value("${minio.access.key}")
    private String minioAccessKey;

    @Value("${minio.access.secret}")
    private String minioSecretKey;

    @Value("${minio.url}")
    private String minioUrl;


    @Bean
    public S3Client amazonS3(){
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider
                        .create(AwsBasicCredentials
                                .create(accessKey, accessSecret)))
                .build();
    }



    @Bean
    public S3Client minioClient(){
        return S3Client.builder()
                .endpointOverride(URI.create(minioUrl))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider
                        .create(AwsBasicCredentials
                                .create(minioAccessKey, minioSecretKey)))
                .forcePathStyle(true)
                .build();
    }



}
