# 🏀 농구 팀 관리 시스템

## 📌 프로젝트 소개
팀을 생성하고, 가입 요청 및 승인을 관리할 수 있는 웹 애플리케이션입니다.  
Spring Boot와 JWT를 이용해 로그인/인증 시스템을 구현했습니다.

## 🛠 사용 기술
- Java 17
- Spring Boot
- JPA
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