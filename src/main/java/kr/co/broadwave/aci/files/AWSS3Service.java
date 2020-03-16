package kr.co.broadwave.aci.files;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

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
        omd.setHeader("filename", storedFileName);//한글명들어가면 오류남
        String awsFilePath =AWSBUCKET+ uploadPath;

        // Copy file to the target location (Replacing existing file with the same name)
        PutObjectResult putObjectResult = s3Client.putObject(new PutObjectRequest(awsFilePath, storedFileName, multipartFile.getInputStream(), omd));

        //이미지 화일이면 섬네일변환후 울리기 파일명앞에 "s_" 를 붙임
        if(multipartFile.getContentType().substring(0,5).toUpperCase().equals("IMAGE")){
            BufferedImage originamImage = ImageIO.read(multipartFile.getInputStream());
            BufferedImage destImg = Scalr.resize(originamImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 200);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(destImg, getExtension(storedFileName), os);
            byte[] buffer = os.toByteArray();
            InputStream destImgInputStream = new ByteArrayInputStream(buffer);


            ObjectMetadata s_omd = new ObjectMetadata();
            s_omd.setContentType(multipartFile.getContentType());
            s_omd.setContentLength(buffer.length);
            s_omd.setHeader("filename", "s_" +storedFileName);//한글명들어가면 오류남

            // Copy file to the target location (Replacing existing file with the same name)
            PutObjectResult putObjectResultThumbnail = s3Client.putObject(new PutObjectRequest(awsFilePath, "s_"+storedFileName,
                    destImgInputStream, s_omd));

            

        }



    }

    public void deleteObject(String bucketPath, String fileName) throws AmazonServiceException {

        s3Client.deleteObject(new DeleteObjectRequest(AWSBUCKET +  bucketPath, fileName));

    }

    public byte[] getObject(String bucketPath, String fileName) throws IOException {
        S3Object o = s3Client.getObject(new GetObjectRequest(AWSBUCKET +  bucketPath, fileName));
        S3ObjectInputStream objectInputStream = o.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);
        return bytes;
        //Resource resource = new ByteArrayResource(bytes);
        //return resource;
    }

    private static String makeThumbnail(String uploadPath, String path, String fileName) throws Exception {

        BufferedImage sourceImg = ImageIO.read(new File(uploadPath + path, fileName));

        BufferedImage destImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 100);

        String thumbnailName = uploadPath + path + File.separator + "s_" + fileName;

        File newFile = new File(thumbnailName);
        String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);

        ImageIO.write(destImg, formatName.toUpperCase(), newFile);

        return thumbnailName.substring(uploadPath.length()).replace(File.separatorChar, '/');
    }


    private String getExtension(String fileName) {
        int dotPosition = fileName.lastIndexOf('.');

        if (-1 != dotPosition && fileName.length() - 1 > dotPosition) {
            return fileName.substring(dotPosition + 1);
        } else {
            return "";
        }
    }

}
