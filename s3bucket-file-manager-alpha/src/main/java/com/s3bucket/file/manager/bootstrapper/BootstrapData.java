package com.s3bucket.file.manager.bootstrapper;

import com.s3bucket.file.manager.domain.S3Bucket;
import com.s3bucket.file.manager.repository.S3BucketRepository;
import com.s3bucket.file.manager.services.S3Service;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {

    private final S3BucketRepository s3BR;
    private final S3Service s3Service;

    public BootstrapData(S3BucketRepository s3BR, S3Service s3Service) {
        this.s3BR = s3BR;
        this.s3Service = s3Service;
    }

    @Override
    public void run(String... args) throws Exception {
        //Loads All Current Objects in S3 Bucket.
        s3Service.setObjectsList();

        S3Bucket buck = new S3Bucket();

        //Initialisation of Current Objects
        List objList = s3Service.getObjectsList();
        buck.setObject(objList);

        s3BR.save(buck);
    }
}
