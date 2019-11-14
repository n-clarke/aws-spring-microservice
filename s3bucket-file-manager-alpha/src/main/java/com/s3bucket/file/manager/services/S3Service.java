package com.s3bucket.file.manager.services;

import com.s3bucket.file.manager.domain.S3Bucket;
import com.s3bucket.file.manager.global.S3BucketData;
import com.s3bucket.file.manager.global.UserGroupData;

import java.io.IOException;
import java.util.List;

public interface S3Service
{
    S3BucketData s3bd = new S3BucketData();
    UserGroupData ugd = null;

    String bucketName = s3bd.getName();
    String key = ugd.getAccessKey();

    List<S3Bucket> getObjectsList();

    void setObjectsList();

    S3Bucket findObjectById(Long id);

    S3Bucket addObject(S3Bucket s3Bucket);

    void uploadObject(String key, String file);

    void downloadObject(String objKey, String saveLocalFileCopyUrl) throws IOException;

    void deleteObject(String objKey);

    void deleteObject(String[] objKeyArr);


}
