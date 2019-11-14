package com.s3bucket.file.manager.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.s3bucket.file.manager.global.UserGroupData;

import java.util.logging.Logger;

public class Connection
{
    UserGroupData ugd = null;

    Connection(UserGroupData group)
    {
        try {
            ugd = group;
            setAWSCredentials();
        } catch (AmazonServiceException e) {
            log.info("Connection to AWS has Failed Please Try Again\n" + e.getErrorMessage());
            return;
        }
    }
    Connection(String accessKey, String secretKey)
    {
        try {
            setAWSCredentials(accessKey, secretKey);
            setAWSCredentials();
        } catch (AmazonServiceException e) {
            log.info("Connection to AWS has Failed Please Try Again\n" + e.getErrorMessage());
            return;
        }
    }


    private Logger log = Logger.getLogger(Connection.class.getName());
    private AWSCredentials credentials;

    AmazonS3 s3client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.DEFAULT_REGION)
            .build();

    private void setAWSCredentials()
    {
        try {
            credentials = new BasicAWSCredentials(ugd.getAccessKey(), ugd.getSecretKey());
        } catch (AmazonServiceException e) {
            log.info(e.getErrorMessage());
            return;
        }
    }
    private void setAWSCredentials(String accessKey, String secretKey)
    {
        try {
            credentials = new BasicAWSCredentials(accessKey, secretKey);
        } catch (AmazonServiceException e) {
            log.info(e.getErrorMessage());
            return;
        }
    }
}
