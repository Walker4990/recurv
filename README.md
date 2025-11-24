# 📌 Recurv — 구독 결제 자동화 시스템

정기 결제–구독 관리–인보이스–재무 기록을 **Webhook 기반으로 완전 자동화**한 정기 구독 플랫폼입니다.  
결제 성공부터 환불까지 **데이터 정합성 보장**을 최우선으로 설계했습니다.

---

# 🔥 1. 프로젝트 핵심 요약

- Toss Webhook 기반 결제 승인 → 구독 활성화 → 인보이스 처리 → 재무 기록 자동화  
- eventId 기반 **중복 결제 차단 (idempotency)**  
- 환불/취소 이벤트 자동 반영  
- 파트너–관리자 간 **실시간 상담(WebSocket)**  
- Redis 캐시 적용 및 3000명 부하 테스트 안정성 검증  
- Payment/Subscription/Invoice/Finance 트랜잭션 기반 정합성 확보

---

# 🏗️ 2. 핵심 기술 스택

**Backend:** Spring Boot, MyBatis, Java  
**DB:** MySQL 8  
**Cache:** Redis  
**Front:** React, Axios  
**Real-time:** STOMP WebSocket  
**Test:** JMeter  
**Infra:** 내장 Tomcat

---

# 📦 3. 기능별 핵심 모듈 요약

아래는 실제 비즈니스 단위로 분리된 기능 패키지입니다.

## 💳 Payment / Webhook
- Toss Webhook 기반 결제 처리  
- `data.status` 기반 상태 분기  
- eventId 저장으로 **중복 Webhook 처리 방지**  
- 원본 Webhook Payload JSON DB 보관 (감사 추적)

## 🔐 Subscription
- 결제 성공 시 `ACTIVE`로 상태 변경  
- BillingInfo 기반 다음 결제일 자동 계산  
- 환불/취소 시 자동 `CANCELED` 처리

## 🧾 Invoice
- 결제 성공 → `PAID`  
- 환불 수신 → `REFUNDED`  
- Payment 및 Subscription과 강하게 연동

## 💰 Finance Transaction
- 결제 성공 시 수익(INCOME) 자동 기록  
- 환불 시 지출(OUTCOME) 자동 생성  
- 대시보드용 기본 재무 데이터 생성

## 💬 Support Chat (WebSocket)
- STOMP 기반 실시간 상담  
- SupportMessage로 unread 관리  
- 파트너 페이지에서 실시간 반영

## ⚡ Redis Cache
- 파트너 목록 등 자주 호출되는 데이터 캐싱  
- MISS → DB 조회 후 Redis 저장  
- HIT → Redis 반환  
- 성능 및 응답 시간 안정화

---

# 🔗 4. 패키지 구조 (실제 프로젝트 구조)

/src/main/java/com/recurv

payment/
subscription/
invoice/
finance/
support/
partner/
common/
config/

yaml
코드 복사

각 모듈은 **Controller / Service / Mapper / XML / VO** 동일한 구조로 구성하여 확장성과 유지보수성을 확보했습니다.

---

# 🚀 5. 핵심 기술 포인트 (면접에서 강조)

### 1) Webhook 기반 자동화  
결제 → 구독 → 인보이스 → 재무까지 전 과정 자동화.

### 2) 트랜잭션 기반 데이터 정합성  
여러 테이블이 동시에 업데이트되기 때문에  
모든 결제 흐름을 단일 트랜잭션으로 묶어 **전체 성공/전체 실패** 구조로 설계했습니다.

### 3) Idempotency (중복 처리 방지)  
Toss Webhook은 동일 이벤트가 여러 번 들어올 수 있어  
**eventId 저장 후 같은 이벤트 즉시 무시**하도록 구현했습니다.

### 4) Redis 적용  
대량 트래픽 대비 캐시 전략 적용 →  
JMeter 3000명 테스트에서 **0% 에러** 달성.

### 5) 실시간 서비스 구현  
파트너용 고객 상담 WebSocket 포함.

---

# 🧩 6. 대표 코드 링크

👉 https://github.com/Walker4990/recurv/tree/main/src/main/java/com/recurv

- PaymentWebhookService  
- PaymentService  
- SubscriptionService  
- InvoiceService  
- FinanceTransactionService  
- Support WebSocket  

*(원하면 파일 이름별 하이퍼링크도 세부적으로 넣어줄게.)*

---

# 📈 7. 어려웠던 점 & 해결

### ✔ Webhook status 파싱 오류  
- Toss가 status를 payload 최상단이 아닌 `data.status`에 전달  
→ JSON 구조 파악 후 파싱 로직 수정

### ✔ 중복 Webhook 처리로 이중 결제 발생  
→ eventId 기반 idempotency 테이블로 중복 즉시 차단

### ✔ 결제–구독–인보이스–재무 간 순서 오류  
→ 모든 처리를 단일 트랜잭션 경계로 통합해 정합성 확보

### ✔ 대량 트래픽에서 응답 지연 발생  
→ HikariCP 풀 확장 + Redis 캐시 + 인덱스 튜닝으로 해결

---
