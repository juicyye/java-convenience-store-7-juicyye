# 🏪 java-convenience-store-precourse

----
## 📌 프로젝트 개요
- 이 프로젝트는 Java로 구현된 간단한 편의점 관리 시스템입니다. 상품 재고 관리, 프로모션 할인, 멤버십 할인, 영수증 출력을 포함하여 고객의 편리한 쇼핑을 지원합니다.
---
## ⚙️ 주요 기능

### 📦 재고 관리
- **재고 수량 확인**: 재고를 확인해 결제 가능 여부를 판단합니다.
- **재고 차감**: 결제 후에는 해당 수량만큼 재고가 차감됩니다.
- **프로모션 재고 우선 차감**: 프로모션 중인 상품은 프로모션 재고를 우선 차감하며, 부족하면 일반 재고를 사용합니다.

### 💸 할인 
#### 1. 프로모션 할인
- **프로모션 기간 내 할인**: 프로모션 기간에만 할인이 적용됩니다.
- **N+M 혜택**: N개 구매시 M개 무료 증정 혜택을 제공합니다
- **재고 한정**: 프로모션 혜택은 재고 내에서만 적용됩니다.
- **혜택 안내**: 고객이 프로모션 혜택을 놓치지 않도록 추가 수량 안내를 제공합니다
- **정가 결제 안내**: 프로모션 재고가 부족해 일부 정가 결제 시 안내 메시지를 제공합니다.

#### 2. 멤버십 할인
- **할인 여부 입력**: 고객에 멤버십 할인을 선택할 수 있습니다.
- **30% 할인**: 멤버십 회원은 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용합니다.
- **최대 한도 8,000원**: 멤버십 할인의 최대 한도는 8,000원입니다.

### 🛒 주문
- 사용자는 아이템 주문 수량을 입력하여 주문할 수 있습니다.

### 영수증 출력
- **구매 내역 요약**: 고객의 구매 내역과 할인 정보를 영수증으로 제공합니다.
- **영수증 항목**
  - 구매 상품 내역
  - 증정 상품 내역
  - 금액 정보
    - 총 구매액
    - 행사 할인
    - 멤버십 할인
    - 최종 금액

---
## 🚨 예외 상황 및 유효성 검사
| 예외 상황                        | 설명                                                            |
|----------------------------------|-----------------------------------------------------------------|
| 유효하지 않은 날짜 입력          | 유효하지 않은 날짜가 입력되면 에러를 반환합니다.               |
| 프로모션 종료 날짜 오류          | 종료 날짜가 시작 날짜보다 빠른 경우 에러를 반환합니다.         |
| 재고 초과 주문                   | 주문 수량이 재고를 초과하면 에러를 반환합니다.                 |
| 잘못된 값 입력                   | 사용자가 잘못된 값을 입력할 경우 에러를 반환합니다.           |
| 잘못된 주문 형식 입력            | 주문 입력 형식이 올바르지 않으면 에러를 반환합니다.           |
| 없는 상품 주문                   | 주문하는 상품이 존재하지 않으면 에러를 반환합니다.           |


---
### 📝 기능 목록
- [x] ```promotions.md``` 파일을 읽어 프로모션 정보 저장
- [x] 주문 수량과 남은 재고 중에서 가능한 무료 증정 아이템의 개수를 알려준다
- [x] ```products.md``` 파일을 읽어들여서 저장한다
- [x] ```products.md``` 파일 중에 프로모션이 있는 아이템만 있는 경우 프로모션 없는 아이템도 생성시킨다
- [x] 재고를 예쁘게 출력되도록 꾸민다
- [x] 주문을 하면 재고가 감소된다
- [x] 프로모션 중인 상품을 주문할 때 재고를 초과하여 주문하면 초과분을 알려준다
- [x] 프로모션 재고만 주문 가능 여부를 설정할 수 있다
- [x] 프로모션 주문 조건 만족 시 무료 증정 아이템 수량을 안내한다
- [x] 프로모션 주문 조건에 만족하여 받고 싶다면 무료 증정 아이템을 받을 수 있다 
- [x] 프로모션 적용 여부를 알려준다
- [x] 프로모션 진행 중인 상품을 주문하면 프로모션 재고가 먼저 차감되고 재고가 부족하면 일반 재고가 차감된다
- [x] 멤버십 할인을 받는다면 프로모션 금액이 차감된 금액의 0.3%를 받을 수 있다
  - 최대금액은 8,000원이다
- [x] 영수증을 출력한다 (멤버십 할인 및 무료 증정 상품 포함)
- [x] 주문한 내역들을 예쁘게 출력되도록 꾸민다