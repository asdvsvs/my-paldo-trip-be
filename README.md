# MyPaldoTrip
[MyPaldoTrip 계획서](https://teamsparta.notion.site/e53af96127f64955911960eb67482e95)
> 해외여행보다 더 좋은 국내여행을 활성화시키기 위한 서비스

## 목차
[1. 프로젝트 소개](#프로젝트-소개) 

[2. 팀원 소개](#팀원-소개)

[3. 아키텍처](#아키텍처)

[4. ERD](#erd)

[5. 사용 기술](#사용-기술)

[6. 주요 기술](#주요-기술)

[7. 기술적 의사결정](#기술적-의사결정)

[8. 트러블 슈팅](#트러블-슈팅)

## 프로젝트 소개

* 프로젝트 기간 : 2024.1.4 ~ 2024.2.8
* 시연 영상 : [MyPaldoTrip 시연 영상]()
* 베포 사이트 : [MyPaldoTrip](https://www.mypaldotrip.site/)
* Front Repository : [MyPaldoTrip-Fe](https://github.com/MyPaldoTrip/my-paldo-trip-fe)

> 다수의 사용자가 동시에 이용해도 안정적으로 서비스를 제공하며, 사용자의 다양하고 복잡한 검색 요구를 충족시키는 뛰어난 검색 기능을 보유하였습니다. 또한, 실시간 채팅 기능을 통해 사용자 간의 커뮤니케이션을 원활하게 지원하며, 이를 통해 정보를 효과적으로 공유하는 여행 커뮤니티 서비스를 제공합니다.

## 팀원 소개

* 리더 : 전주현 [@asdvsvs](https://github.com/asdvsvs)
* 부리더 : 정지명 [@Jimyung0418](https://github.com/jimyung0418)
* 팀원 : 이현구 [@Sk1palong](https://github.com/Sk1palong)
* 팀원 : 김용운 [@yoooooungwoon](https://github.com/yoooooungwoon)
* 팀원 : 장숭혁 [@Mcgeolypazun](https://github.com/Mcgeolypazun)

## 아키텍처
![image](https://github.com/MyPaldoTrip/my-paldo-trip-fe/assets/94377282/99753f56-1091-4906-951f-c8650e0b00c4)
## ERD
![image](https://github.com/MyPaldoTrip/my-paldo-trip-fe/assets/94377282/c7048f6e-398b-4bf9-8530-5701be4a8a4b)
## 사용 기술
<details>
    <summary>BE</summary>
  
* JAVA 17
* SpringBoot 3.2.1
* websocket
* queryDSL 5.0.0
* oauth

</details>

<details>
    <summary>FE</summary>
  
* Vue
* stompjs

</details>

<details>
    <summary>DB</summary>
  
* mysql 8.1
* h2
</details>

<details>
    <summary>Infra</summary>
  
* S3
* Docker
</details>

## 주요 기술
### 1. 동적 조회 기능
- 여행 커뮤니티인만큼 여러 지역별로 조회하는 기능을 위해 동적 조회 기능 개발을 편하게 해주는 QueryDSL 도입
  
### 2. 채팅 기능
- 여행 커뮤니티에서의 채팅 기능을 위해 WebSocket,Stomp 등 도입

### 3. 소셜 로그인 기능
- 현대 사회에서 회원가입이라는 장벽을 조금이라도 허물고자 소셜 아이디로 간단하게 로그인이 가능한 OAtuh 도입


## 기술적 의사결정

### 1. QueryDSL
- QueryDSL을 활용해서 복잡한 조회 조건을 쉽고 간결한 형태로 생산성 높은 코드로 개발
- JPQL, Criteria 보다 뛰어난 오류 발견 기능 및 직관성

### 2. Websocket
- WebSocket을 활용해서 양방향 통신이 가능한 채팅 기능 개발
- Polling, Long Polling, SSE 방식에 비해 서버 리소스는 높지만 HTTP 오버헤드 발생 가능성이 적음

### 3. MongoDB
- MongoDB를 활용해서 채팅 내역 저장 및 관리에 유용한 채팅 데이터 베이스
- redis에 비해 영구적인 데이터 저장이 용이하고 MySQL에 비해 데이터 구조 변경이 쉬워 추후 채팅 내역 확장성에 용이

## 트러블 슈팅

### 1. 동시성 문제
- 다수의 이용자가 동시에 사용하는 상황에서는 동시성 문제가 발생하지 않음
- 한명의 이용자가 동시에 여러번 사용하는 상황에서는 문제가 발생
- 문제를 해결하기 위해 동시성 문제의 해결 방법으로 많이 쓰이는 방법인 락킹, 싱크로나이즈드 등의 방법을 적용
- 한명의 이용자가 동시에 부하를 거는 일은 정상적이지 않은 상황이고 위와 같은 방법으로 해결이 되지 않아 추후에 동시성 문제에 대해 더 깊이 있게 공부하는 것으로 보류

### 2. 캐싱
- 웹사이트의 메인 페이지의 경우 사용자에 의해 빈번하게 호출 됨
- 자주 호출되는 만큼 데이터베이스에 잦은 접근에 따른 비효율 발생
- 메인 페이지의 내용이 변동이 적은 데이터라는 점을 이용하여 redis를 이용한 캐싱을 도입
- 캐싱된 메인페이지의 응답 시간 90% 단축
