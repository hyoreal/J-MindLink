# J-MindLink 개발 명세서

## 1. 기술 스택
- Java 17, Spring Boot 3.x, H2 Database
- Spring REST Docs (API 문서화)
- JSP, Vanilla JS (Frontend)

## 2. 데이터 모델 (WikiPage)
- `id` (Long, PK, Auto Increment)
- `title` (String, Unique, Not Null) - 링크 키워드
- `content` (String/Lob, Not Null) - 본문

## 3. API 명세 (REST Docs 대상)
- `POST /api/v1/wiki`: 위키 등록
- `GET /api/v1/wiki/{id}`: 상세 조회 (본문 내 키워드 자동 링크 처리 필수)
- `GET /api/v1/wiki`: 목록 조회

## 4. Frontend 명세
- `PageController`: `/`, `/wiki/write`, `/wiki/{id}` 등 화면 라우팅만 담당.
- `detail.jsp`: 로딩 후 JS `fetch`로 데이터를 받아 화면 그리기.
