package kr.co.broadwave.aci.common;

import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author InSeok
 * Date : 2019-07-26
 * Remark : 미티어 타입구분
 * https://github.com/woobong/spring-boot-jpa-summernote-image-upload-example 소스 참고함
 */
public class MediaUtils {

    private static Map<String, MediaType> mediaMap;

    static {
        mediaMap = new HashMap<>();

        mediaMap.put("JPEG", MediaType.IMAGE_JPEG);
        mediaMap.put("JPG", MediaType.IMAGE_JPEG);
        mediaMap.put("GIF", MediaType.IMAGE_GIF);
        mediaMap.put("PNG", MediaType.IMAGE_PNG);
    }

    public static MediaType getMediaType(String type) {
        return mediaMap.get(type.toUpperCase());
    }

    public static boolean containsImageMediaType(String mediaType) {
        return mediaMap.values().contains(MediaType.valueOf(mediaType));
    }
}
