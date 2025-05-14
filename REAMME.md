# 🏀 농구 팀 관리 시스템

## 📌 프로젝트 소개

**PBT_PROJECT**는 남녀노소 누구나 농구를 즐길 수 있도록 만든 웹 애플리케이션입니다.  
사용자는 팀을 효율적으로 생성하고 가입하며, 팀원을 관리하고 시합을 생성할 수 있습니다.

시합 생성 시, 매니저는 총 참가 인원과 포지션별 인원 수를 지정할 수 있으며,  
참가자는 미리 정해진 포지션뿐 아니라 **사용자 지정(커스텀) 포지션**으로도 신청이 가능합니다.

또한 사용자는 여러 팀에 소속될 수 있으며, 각 팀의 시합 일정을 **캘린더 형식으로 확인**할 수 있습니다.
## 🛠 사용 기술
- Java 17
- Spring Boot
- JPA
- JWT
- MySQL
- JavaScript, React
- JWT (토큰 기반 인증)

## ✨ 주요 기능
- 회원가입 / 로그인 (JWT 발급)
- 농구 팀 생성 / 가입 요청 / 승인 관리
- 마이페이지 및 회원 정보 수정
- 게시판 + 댓글 (계층형 구조)

## 🖥️ ERD
![ERD 이미지](./docs/erd.png)

## 🚀 실행 방법
```bash
git clone https://github.com/yourname/basketball-team-project.git
cd basketball-team-project
./mvnw spring-boot:run