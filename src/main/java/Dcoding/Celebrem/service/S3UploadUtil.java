package Dcoding.Celebrem.service;

import Dcoding.Celebrem.common.exception.BadRequestException;
import Dcoding.Celebrem.common.exception.InternalServerErrorException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class S3UploadUtil {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new InternalServerErrorException("MultipartFile -> File로 전환이 실패했습니다."));


        return upload(uploadFile, "celebrem", multipartFile.getOriginalFilename());
    }

    private String upload(File uploadFile, String dirName, String originalName) {
        //파일명 중복방지를 위한 UUID
        String fileName = dirName +
                "/" + UUID.randomUUID() +
                "_" + originalName;

        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client
                .putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    public void fileDelete(String fileUrl) {
        try{
            String fileKey = fileUrl.substring(49);
            String key = fileKey; // 폴더/파일.확장자
            final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion("ap-northeast-2").build();
            try {
                System.out.println("key = " + key);
                s3.deleteObject(bucket, key);
            } catch (AmazonServiceException e) {
                System.err.println(e.getErrorMessage());
                System.exit(1);
            }
        } catch (Exception exception) {
            throw new InternalServerErrorException("이미지 삭제 실패");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File("/" + UUID.randomUUID());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}
