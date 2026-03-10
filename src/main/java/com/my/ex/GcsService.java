package com.my.ex;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

//@Service
public class GcsService {
	
	private final Storage storage;
	
	@Value("${gcp.gcs.bucket-name}")
	private String bucketName;
	
	@Value("${gcp.gcs.content.folder}")
	private String contentFolder;
	
	@Value("${gcp.gcs.profile.folder}")
	private String profileFolder;
	
	public GcsService() {
		this.storage = StorageOptions.getDefaultInstance().getService();
	}
	
	public GcsService(Storage storage, String bucketName, String contentFolder, String profileFolder) {
		this.storage = storage;
		this.bucketName = bucketName;
		this.contentFolder = contentFolder;
		this.profileFolder = profileFolder;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getContentFolder() {
		return contentFolder;
	}

	public void setContentFolder(String contentFolder) {
		this.contentFolder = contentFolder;
	}

	public String getProfileFolder() {
		return profileFolder;
	}

	public void setProfileFolder(String profileFolder) {
		this.profileFolder = profileFolder;
	}

	public Storage getStorage() {
		return storage;
	}
	
	@Override
	public String toString() {
		return "GcsService [storage=" + storage + ", bucketName=" + bucketName + ", contentFolder=" + contentFolder
				+ ", profileFolder=" + profileFolder + "]";
	}

	public String uploadProfileImage(MultipartFile profileImage) throws IOException {
		String filename = profileImage.getOriginalFilename().trim().replace(" ", "_");
		String uniqueFileName = UUID.randomUUID().toString() + "_" + filename; 
		String objectName = profileFolder + uniqueFileName; // profileImages/filename
		
		BlobId blobId = BlobId.of(bucketName, objectName); // BlobId.of("hometalk-bucket", "profileImages/filename")
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
									.setContentType(profileImage.getContentType())
									.setCacheControl("public, max-age=31536000") // 브라우저가 이미지를 1년 동안 캐싱하도록 설정하여, 반복 요청을 줄이고 페이지 로딩 속도 향상
									.build();
		// GCS에 파일 업로드
		storage.create(blobInfo, profileImage.getBytes());
//		storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)); // Acl.User.ofAllUsers() - 모든 사용자, Acl.Role.READER → 읽기 권한
		
		// GCS에 저장된 파일의 공개 url 반환
		return String.format("https://storage.googleapis.com/%s/%s", bucketName, objectName);
	}
	
	public String uploadContentImage(MultipartFile contentImage) throws IOException {
		String filename = contentImage.getOriginalFilename().trim().replace(" ", "_");
		String uniqueFileName = UUID.randomUUID().toString() + "_" + filename; 
		String objectName = contentFolder + uniqueFileName; // contentImages/filename
		
		BlobId blobId = BlobId.of(bucketName, objectName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
									.setContentType(contentImage.getContentType())
									.setCacheControl("public, max-age=31536000") // 브라우저가 이미지를 1년 동안 캐싱하도록 설정하여, 반복 요청을 줄이고 페이지 로딩 속도 향상
									.build();
		// GCS에 파일 업로드
		storage.create(blobInfo, contentImage.getBytes());

		// GCS에 저장된 파일의 공개 url 반환
		return String.format("https://storage.googleapis.com/%s/%s", bucketName, objectName);
	}
	
}
