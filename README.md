# ☁️ HomeTalk
**Weather-Responsive Interior Community & Real-time Communication Platform**

<p align="left">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white">
  <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
  <img src="https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white">
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
  <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white">
</p>

---

## ✨ Project Overview
**HomeTalk**는 사용자의 위치 데이터와 커뮤니티 활동을 결합한 인테리어 정보 공유 플랫폼입니다. <br>
외부 API를 활용한 **동적 UI 구현**과 **순수 WebSocket 기반의 실시간 통신**을 적용하는 데 중점을 두었습니다. 대용량 미디어 처리를 위한 클라우드 스토리지 활용 등 실제 서비스 운영 환경에 근접한 아키텍처 구성을 목표로 개발되었습니다.

---

## 🛠 Key Implementation

### 1. Weather-Driven Dynamic UI
OpenWeather API를 연동하여 사용자의 현재 날씨에 따라 메인 화면의 배경 영상이 실시간으로 변화
- **기술 포인트**: 기상 상태(Clear, Clouds, Rain, Snow)에 따른 배경 에셋 매핑 로딩 속도 개선을 위해 `GCS` 연동 및 비디오 포맷 최적화 수행
- **사용자 경험**: 접속 위치의 날씨를 시각적으로 즉각 동기화하여 몰입감 있는 서비스 환경 제공

<p align="center">
  <img src="https://github.com/user-attachments/assets/9f433c70-fd22-49ce-a2b8-e1c3d1794415" width="80%" />
  <br>
  <em>▲ 실시간 날씨 데이터 기반 배경 전환 시연</em>
</p>

---

### 2. Real-time Chat System (WebSocket)
Java 표준 `@ServerEndpoint`를 활용하여 프레임워크 의존성 없이 저지연(Low-latency) 실시간 메시징 환경을 구축
* **구현 포인트**: 
    - **자체 프로토콜 설계**: `Gson`을 활용하여 메시지 타입(입장, 퇴장, 채팅, 이모티콘)별 고유 코드를 부여하고 로직 분기 처리
    - **효율적인 세션 관리**: `ConcurrentHashMap` 기반의 세션 저장소를 직접 설계하여 방(Room) 단위 및 개별 유저 단위의 타겟 메시지 전송 로직 구현
- **기술적 해결**: `Configurator` 커스터마이징을 통해 웹소켓 서버 클래스 내 의존성 주입(`@Autowired`) 문제 해결

<p align="center">
  <img src="https://github.com/user-attachments/assets/444b57a9-1959-4954-8c77-7cdbffe70433" width="80%" />
  <br>
  <em>▲ 실시간 채팅 및 메시지 수신 시연</em>
</p>

---

### 3. Email Authentication
비밀번호 변경 등 주요 보안 절차에서 SMTP를 통한 실시간 인증 시스템을 적용
- **구현 방식**: Spring Email 라이브러리를 활용한 인증번호 발송 및 세션 내 유효성 검증 로직 구현
- **사용자 경험**: 안전한 정보 수정을 위한 2단계 인증 절차 제공

<p align="center">
  <img src="https://github.com/user-attachments/assets/71ebe877-a586-4b54-9ee8-008bbe3afbc5" width="80%" />
  <br>
  <em>▲ 이메일 인증번호 발송 및 보안 검증 시연</em>
</p>

---

### 4. Interactive Content Authoring
Kakao Maps API와 해시태그 시스템을 결합하여 정보성이 높은 게시글 작성을 지원
- **주요 기능**: 장소 키워드 검색을 통한 지도 좌표 바인딩, 동적 해시태그 생성 및 관리
- **구현 방식**: 프론트엔드 API 연동을 통한 위치 데이터 직렬화 및 DB 저장 로직 구축

<p align="center">
  <img src="https://github.com/user-attachments/assets/a8e30647-e58f-46d6-9777-daf12d66a7e9" width="80%" />
  <br>
  <em>▲ 위치 정보 첨부 및 태그 기반 게시글 작성 시연</em>
</p>

---

### 5. Other Service Features
* **인터랙션**: 게시글 좋아요, 댓글/답글 알림 시스템
* **콘텐츠 관리**: 제목/내용/작성자 통합 검색, 페이징 처리, 무한 스크롤, 내가 쓴 글/댓글 모아보기
* **사용자 편의**: OAuth 2.0(Google, Naver) 소셜 로그인, 커스텀 프로필 업로드 및 닉네임 변경
* **확산성**: 게시글 외부 공유(네이버/카카오) 기능 및 URL 클립보드 복사 인터페이스

---

## ⚙️ Engineering Focus
- **Performance Optimization (ffmpeg & GCS)**: 
    - 고해상도 배경 영상 로딩 시 발생하는 네트워크 병목 현상을 해결하기 위해 **`ffmpeg` 라이브러리로 비디오를 압축 및 최적화**하여 리소스 크기를 대폭 축소
    - 최적화된 미디어 파일을 **Google Cloud Storage(GCS)** 에서 서빙함으로써 서버 부하 분산 및 안정적인 스트리밍 환경 구축
- **Operational Logging**: 
    - `Logback` 설정을 활용하여 개발 환경에서는 `DEBUG` 레벨로 상세한 실행 로그 및 SQL 쿼리를 모니터링하고, 운영 시에는 시스템 부하를 고려하여 `WARN` 이상의 레벨로 조정하여 관리 효율성 제고
- **Data Integrity**: MyBatis 환경에서 효율적인 CRUD 로직 설계 및 비즈니스 로직 내 예외 처리 강화

---

## 📅 Roadmap
- [ ] **Recommendation Engine**: 태그 데이터 분석 기반 맞춤형 인테리어 포스팅 추천

---

## ⚖️ License
© 2025 Jihye. All Rights Reserved.  

The source code in this repository is publicly available for personal study and reference only.  
Unauthorized copying, distribution, or commercial use is strictly prohibited.
