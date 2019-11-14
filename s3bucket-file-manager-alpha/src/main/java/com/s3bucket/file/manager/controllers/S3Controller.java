package com.s3bucket.file.manager.controllers;

import com.s3bucket.file.manager.domain.S3Bucket;
import com.s3bucket.file.manager.services.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(S3Controller.BASE_URL)
public class S3Controller {

    public static final String BASE_URL = "s3bucket/api/v1/objects";

    private S3Service s3Service;

    public S3Controller(S3Service s3Service) { this.s3Service = s3Service; }

    @GetMapping
    public List<S3Bucket> getAllObjects(){ return this.s3Service.getObjectsList(); }

    @GetMapping("/{id}")
    public S3Bucket getObjectById(@PathVariable Long id) {
        return this.s3Service.findObjectById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public S3Bucket createObject(@RequestBody S3Bucket s3Bucket) {
        return this.s3Service.addObject(s3Bucket);
    }
}
