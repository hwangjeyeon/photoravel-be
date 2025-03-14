package trendravel.photoravel_be.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
@Profile({"dev"})
public class S3ConfigInLocal{

    @Value("${minio.region.static}")
    private String region;

    @Value("${minio.access.key}")
    private String minioAccessKey;

    @Value("${minio.access.secret}")
    private String minioSecretKey;

    @Value("${minio.url}")
    private String minioUrl;



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
