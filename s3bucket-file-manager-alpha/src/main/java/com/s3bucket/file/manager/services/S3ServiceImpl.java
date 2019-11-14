package com.s3bucket.file.manager.services;

import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.s3bucket.file.manager.domain.S3Bucket;
import com.s3bucket.file.manager.repository.S3BucketRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Service
public class S3ServiceImpl implements S3Service {

    private final Connection conn = new Connection("accessKey", "secretKey");
    private final S3BucketRepository s3BR;
    private Logger log = Logger.getLogger(S3ServiceImpl.class.getName());

    public S3ServiceImpl(S3BucketRepository s3BR) {
        this.s3BR = s3BR;
    }

    //list all the available objects in our S3 bucket
    private ArrayList objectList = new ArrayList();

    @Override
    public List<S3Bucket> getObjectsList() { return objectList; }

    private static boolean validUploadFile = false;

    @Override
    public void setObjectsList()
    {
        ObjectListing objectListing = conn.s3client.listObjects(this.bucketName);
        for(S3ObjectSummary os : objectListing.getObjectSummaries())
        {
            objectList.add(os.getKey());
        }
    }

    @Override
    public S3Bucket findObjectById(Long id)  { return this.s3BR.findById(id).get(); }

    @Override
    public S3Bucket addObject(S3Bucket s3Bucket)  {
        String filePath = "";
        uploadObject(key, filePath);
        return this.s3BR.save(s3Bucket);
    }

    //Uploads the specified file to Amazon S3 under the specified bucket and key name.
    public void uploadObject(String key, String file)
    {
        try {
            checkUploadFile(file);
            if (validUploadFile == true) {
                conn.s3client.putObject(
                        bucketName,
                        key,
                        new File(file)
                );
            }
            else {
                log.info("Uploaded File does not match template and has been rejected.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    private void checkUploadFile(String inputFile) throws IOException, InvalidFormatException
    {
        //replace with https://s3-[yourRegion].amazonaws.com/[yourBucket]/[yourCollectionName]/[yourFolderName]/[yourKeyName]
        String templateFileUrl = s3bd.getPath() + "template-file.xlsx";

        Workbook wkTemp = WorkbookFactory.create(new File(templateFileUrl));
        Workbook wkUpload = WorkbookFactory.create(new File(inputFile));
        ArrayList tempColName = new ArrayList();
        ArrayList uploadColName = new ArrayList();

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        // Getting the Sheet at index zero
        Sheet tempSheet = wkTemp.getSheetAt(0);
        Sheet uploadSheet = wkTemp.getSheetAt(0);

        //Iterating over all the rows and columns in a Sheet
        tempSheet.forEach(row -> {
            row.forEach(cell -> {
                String cellValue = dataFormatter.formatCellValue(cell);
                tempColName.add(cellValue);
            });
        });

        //Iterating over all the rows and columns in a Sheet
        uploadSheet.forEach(row -> {
            row.forEach(cell -> {
                String cellValue = dataFormatter.formatCellValue(cell);
                uploadColName.add(cellValue);
            });
        });

        wkTemp.close();
        wkUpload.close();

        tempColName.forEach((n) -> {
            if(Arrays.stream(uploadColName.toArray()).anyMatch(n::equals)) {
                validUploadFile = true;
            }
        });
    }

    @Override
    public void downloadObject(String objKey, String saveLocalFileCopyUrl) throws IOException
    {
        S3Object s3object = conn.s3client.getObject(bucketName, objKey);
        try (InputStream inputStream = s3object.getObjectContent()){
            //Write to a new file at specified path
            Files.copy(inputStream, Paths.get(saveLocalFileCopyUrl));
        }
    }

    @Override
    public void deleteObject(String objKey)
    {
        conn.s3client.deleteObject(bucketName, objKey);
    }

    @Override
    public void deleteObject(String[] objKeyArr)
    {
        DeleteObjectsRequest delObjReq = new DeleteObjectsRequest(bucketName)
                .withKeys(objKeyArr);
        conn.s3client.deleteObjects(delObjReq);
    }
}
