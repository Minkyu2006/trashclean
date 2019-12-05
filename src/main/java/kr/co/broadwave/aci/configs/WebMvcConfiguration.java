package kr.co.broadwave.aci.configs;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @author InSeok
 * Date : 2019-09-20
 * Remark :
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
    private static final String CLASSPATH_RESOURCE_LOCATIONS ="classpath:/static/";

    @Value("${aci.aws.s3.access.id}")
    private String AWSS3ACCESSID;

    @Value("${aci.aws.s3.access.key}")
    private String AWSS3ACCESSKEY;

    @Value("${aci.aws.region}")
    private String AWSREGION;


    @Override // 본Config 파일을 추가하면 Pageable 관련 생성자 에러가나는부분을 해결하기위함
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        String uploadfilesUrl = "file:///" + recordUploadFileRoot;
//        registry.addResourceHandler("/uploadfiles/**").addResourceLocations(uploadfilesUrl).setCachePeriod(31536000);  // 유효기간을 초단위로 지정 (604800 = 7일)
        //registry.addResourceHandler("/assets/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS + "assets/").setCachePeriod(31536000);
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS).setCachePeriod(31536000);
    }

    @Bean
    public BasicAWSCredentials AwsCredentianls(){
        BasicAWSCredentials AwsCreds = new BasicAWSCredentials(AWSS3ACCESSID,AWSS3ACCESSKEY);
        return AwsCreds;
    }

    @Bean
    public AmazonS3 AwsS3Client(){
        AmazonS3 s3Builder = AmazonS3ClientBuilder.standard()
                .withRegion(AWSREGION)
                .withCredentials(new AWSStaticCredentialsProvider(this.AwsCredentianls()))
                .build();
        return s3Builder;
    }

}
