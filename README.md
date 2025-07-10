# VYBZ Payment Service

VYBZ 플랫폼의 결제 및 구독 서비스를 담당하는 마이크로서비스입니다.

## 📋 목차

-   [개요](#개요)
-   [기술 스택](#기술-스택)
-   [주요 기능](#주요-기능)
-   [프로젝트 구조](#프로젝트-구조)
-   [API 문서](#api-문서)
-   [설치 및 실행](#설치-및-실행)
-   [환경 설정](#환경-설정)
-   [배치 작업](#배치-작업)
-   [이벤트 처리](#이벤트-처리)

## 🎯 개요

VYBZ Payment Service는 다음과 같은 기능을 제공합니다:

-   **일반 결제**: Toss Payments를 통한 일반 결제 처리
-   **구독 결제**: 정기 결제를 위한 빌링키 기반 자동 결제
-   **결제 관리**: 결제 생성, 승인, 취소, 환불 처리
-   **구독 관리**: 구독 생성, 해지, 자동 결제 처리
-   **배치 작업**: 정기 결제 자동 처리 스케줄러

## 🛠 기술 스택

### Backend

![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-59666C?style=for-the-badge)
![QueryDSL](https://img.shields.io/badge/QueryDSL-0097A7?style=for-the-badge)
![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

### Infra

![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)
![Docker Hub](https://img.shields.io/badge/Docker_Hub-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Amazon EC2](https://img.shields.io/badge/Amazon_EC2-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)

### 협업

![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)


### Database & Cache

-   **MySQL 8.0**
-   **Redis**

### Message Queue

-   **Apache Kafka**

### Documentation

-   **Swagger/OpenAPI 3.0**

### Build & Deploy

-   **Gradle**
-   **Docker**

## 🚀 주요 기능

### 1. 결제 서비스 (`/api/v1/payment`)

-   **결제 생성**: 결제창 호출을 위한 결제 요청 생성
-   **결제 승인**: 결제 승인 처리
-   **결제 실패**: 결제 실패 처리
-   **결제 취소**: 결제 환불/취소 처리
-   **결제 내역 조회**: 사용자별 결제 히스토리 조회

### 2. 구독 서비스 (`/api/v1/membership`)

-   **빌링키 등록**: 자동 결제를 위한 빌링키 발급
-   **첫 자동 결제**: 빌링키를 이용한 첫 결제 실행
-   **구독 해지**: 자동 결제 해지

### 3. 배치 작업

-   **정기 결제 처리**: 매일 오전 11시 실행되는 자동 결제 배치
-   **결제 실패 재시도**: 실패한 결제에 대한 재시도 로직
-   **구독 상태 관리**: 만료된 구독 처리

## 📁 프로젝트 구조

```
src/main/java/back/vybz/paymentservice/
├── common/                    # 공통 모듈
│   ├── config/               # 설정 클래스들
│   ├── dto/                  # 공통 DTO
│   ├── entity/               # 공통 엔티티
│   ├── exception/            # 예외 처리
│   └── util/                 # 유틸리티
├── payment/                  # 결제 도메인
│   ├── application/          # 결제 서비스 로직
│   ├── domain/               # 결제 도메인 모델
│   ├── dto/                  # 결제 DTO
│   ├── infrastructure/       # 결제 리포지토리
│   ├── presentation/         # 결제 컨트롤러
│   ├── scheduler/            # 결제 스케줄러
│   └── vo/                   # 결제 VO
├── subscription/             # 구독 도메인
│   ├── application/          # 구독 서비스 로직
│   ├── batch/                # 배치 작업
│   ├── domain/               # 구독 도메인 모델
│   ├── dto/                  # 구독 DTO
│   ├── infrastructure/       # 구독 리포지토리
│   ├── presentation/         # 구독 컨트롤러
│   └── vo/                   # 구독 VO
└── kafka/                    # Kafka 이벤트 처리
    ├── config/               # Kafka 설정
    ├── event/                # 이벤트 모델
    └── producer/             # 이벤트 프로듀서
```

## 📚 API 문서

Swagger UI를 통해 API 문서를 확인할 수 있습니다:

-   **URL**: `http://localhost:8080/payment-service/swagger-ui/index.html`
-   **API 그룹**:
    -   Payment-Service: 결제 관련 API
    -   Subscription-Service: 구독 관련 API

### 주요 API 엔드포인트

#### 결제 API

-   `POST /api/v1/payment` - 결제 생성
-   `POST /api/v1/payment/confirm` - 결제 승인
-   `POST /api/v1/payment/fail` - 결제 실패
-   `POST /api/v1/payment/refund/{paymentKey}` - 결제 취소
-   `GET /api/v1/payment/{userUuid}` - 결제 내역 조회

#### 구독 API

-   `POST /api/v1/membership/billing/register` - 빌링키 등록
-   `POST /api/v1/membership/billing/execute` - 첫 자동 결제
-   `DELETE /api/v1/membership` - 구독 해지

## 🚀 설치 및 실행

### 1. 사전 요구사항

-   Java 17
-   Gradle 8.4+
-   Docker (선택사항)
-   MySQL 8.0
-   Redis
-   Kafka

### 2. 로컬 실행

```bash
# 프로젝트 클론
git clone <repository-url>
cd vybz-payment

# Gradle 빌드
./gradlew clean build

# 애플리케이션 실행
./gradlew bootRun
```

## ⚙️ 환경 설정

### 주요 설정 파일

-   `application.yml`: 기본 설정
-   `application-dev.yml`: 개발 환경 설정
-   `application-db.yml`: 데이터베이스 설정

### 환경 변수

```yaml
# Toss Payments 설정
payment:
  secret-key: ${TOSS_SECRET_KEY}
  base-url: ${TOSS_BASE_URL}
  success-url: ${SUCCESS_URL}
  fail-url: ${FAIL_URL}

# 데이터베이스 설정
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

# Kafka 설정
spring:
  kafka:
    bootstrap-servers: ${KAFKA_SERVERS}
```

## 🔄 배치 작업

### 정기 결제 배치

-   **실행 시간**: 매일 오전 11시 (`0 0 11 * * *`)
-   **기능**:
    -   만료된 구독에 대한 자동 결제 처리
    -   결제 실패 시 재시도 로직
    -   구독 상태 업데이트

### 배치 구성 요소

-   `BillingPaymentBatchScheduler`: 배치 스케줄러
-   `BillingPaymentBatchJob`: 배치 작업 정의
-   `BillingPaymentReader`: 데이터 읽기
-   `BillingPaymentProcessor`: 데이터 처리
-   `BillingPaymentWriter`: 데이터 쓰기

## 📡 이벤트 처리

### Kafka 이벤트

-   **PaymentConfirmEvent**: 결제 승인 이벤트
-   **PaymentRefundEvent**: 결제 환불 이벤트
-   **SubscriptionEvent**: 구독 이벤트
-   **SubscriptionCancelEvent**: 구독 취소 이벤트

### 이벤트 프로듀서

-   `PaymentConfirmProducer`: 결제 승인 이벤트 발행
-   `PaymentRefundProducer`: 결제 환불 이벤트 발행
-   `SubscriptionEventProducer`: 구독 이벤트 발행
-   `SubscriptionCancelEventProducer`: 구독 취소 이벤트 발행

## 🏗 아키텍처

### 도메인 주도 설계 (DDD)

-   **Domain Layer**: 비즈니스 로직과 엔티티
-   **Application Layer**: 서비스 로직과 유스케이스
-   **Infrastructure Layer**: 외부 시스템 연동
-   **Presentation Layer**: API 엔드포인트

### 마이크로서비스 패턴

-   **Service Discovery**: Eureka Client를 통한 서비스 등록
-   **Circuit Breaker**: Feign Client를 통한 외부 서비스 호출
-   **Event-Driven**: Kafka를 통한 비동기 이벤트 처리

## 🔧 개발 가이드

### 코드 컨벤션

-   **패키지 구조**: 도메인별 계층 분리
-   **네이밍**: 명확하고 일관된 네이밍 규칙
-   **예외 처리**: BaseException을 통한 통일된 예외 처리
-   **로깅**: Slf4j를 통한 구조화된 로깅

### 테스트

```bash
# 단위 테스트 실행
./gradlew test

# 통합 테스트 실행
./gradlew integrationTest
```

## 📝 라이선스

이 프로젝트는 VYBZ 팀의 내부 프로젝트입니다.

## 👥 팀

-   **개발팀**: VYBZ Backend Team

---

**VYBZ Payment Service** - 안전하고 신뢰할 수 있는 결제 서비스
