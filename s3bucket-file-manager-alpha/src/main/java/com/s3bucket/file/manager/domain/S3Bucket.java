package com.s3bucket.file.manager.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class S3Bucket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private List<String> object;
}
