package kr.co.broadwave.aci.files;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author InSeok
 * Date : 2019-12-05
 * Remark :
 */
@Service
public class AWSS3Service {

    @Value("${aci.aws.s3.bucket}")
    private String AWSBUCKET;


    private final AmazonS3 s3Client;

    @Autowired
    public AWSS3Service(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }


    public void uploadObject(MultipartFile multipartFile, String storedFileName,String uploadPath) throws IOException {

        ObjectMetadata omd = new ObjectMetadata();
        omd.setContentType(multipartFile.getContentType());
        omd.setContentLength(multipartFile.getSize());
        omd.setHeader("filename", multipartFile.getOriginalFilename());
        String awsFilePath =AWSBUCKET+ uploadPath;

        // Copy file to the target location (Replacing existing file with the same name)
        PutObjectResult putObjectResult = s3Client.putObject(new PutObjectRequest(awsFilePath, storedFileName,
                multipartFile.getInputStream(), omd));
//
//        System.out.println(putObjectResult.toString());
//
//        System.out.println("filesize: " + multipartFile.getSize() );
//        System.out.println("OriginalFilename: " + multipartFile.getOriginalFilename() );
//        System.out.println("File path : '" + awsFilePath +"'");
//        System.out.println("File name : '" + storedFileName +"'");


    }

    public void deleteObject(String bucketPath, String fileName) throws AmazonServiceException {

        s3Client.deleteObject(new DeleteObjectRequest(AWSBUCKET +  bucketPath, fileName));

    }

    public Resource getObject(String date, String storedFileName) throws IOException {
        S3Object o = s3Client.getObject(new GetObjectRequest(AWSBUCKET + "/" + date, storedFileName));
        S3ObjectInputStream objectInputStream = o.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        Resource resource = new ByteArrayResource(bytes);
        return resource;
    }



}
