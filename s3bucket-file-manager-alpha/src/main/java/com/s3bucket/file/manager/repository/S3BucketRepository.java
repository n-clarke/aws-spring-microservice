package com.s3bucket.file.manager.repository;

import com.s3bucket.file.manager.domain.S3Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3BucketRepository extends JpaRepository<S3Bucket, Long> { }
