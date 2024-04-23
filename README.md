# 숙소 예약 시스템
숙소 예약 시스템


## 프로젝트 소개
관리자는 숙소를 등록하고, 사용자는 숙소를 예약하는 온라인 숙소 예약 시스템


## 기술
- Java 17
- Spring Boot 3.1.8
- Spring Data Jpa
- Spring Security
- JWT
- MySQL
- Redis
- Swagger


## 개발 기간
2024.03.19 ~ 


## ERD
![image](https://github.com/GiSung-Song/room_reservation/assets/83264696/d16b752c-9903-4029-b8f0-516271f9c8dc)


## 회원가입(회원)
![image](https://github.com/GiSung-Song/room_reservation/assets/83264696/e4822c56-102f-4c1b-904f-d4047c932b46)
- 별명과 이메일을 unique키로 가지고 있어 체크 후 회원가입을 합니다.


## 회원가입(관리자)
![image](https://github.com/GiSung-Song/room_reservation/assets/83264696/8269744a-a09f-4fd0-96b5-dbdaf07ce9d1)
![image](https://github.com/GiSung-Song/room_reservation/assets/83264696/c9fb4a40-3a23-41d9-a3c7-90e1bd9aa25b)
 - 사업자등록번호가 중복되지 않은지 체크하고, 사업자등록번호가 유효한지 체크 후 회원가입을 진행합니다.

## 예약
![image](https://github.com/GiSung-Song/room_reservation/assets/83264696/3eaae0e3-022f-4eda-a286-88daf25b6973)
- 해당 객실이 이미 예약되어있는지 체크한 후 예약이 되어있지 않은 경우 예약을 진행합니다.

## 결제
![image](https://github.com/GiSung-Song/room_reservation/assets/83264696/414fba3f-6997-40ac-9d1a-2216594e09fa)
![image](https://github.com/GiSung-Song/room_reservation/assets/83264696/3acbf640-2772-4ea2-87f6-abb78d8ab0c0)
![image](https://github.com/GiSung-Song/room_reservation/assets/83264696/97b6a41b-4d94-4648-bec9-f0cd7fdba90c)
- 결제 전 결제 할 예약을 요약하여 보여주고, 결제 성공 시 결제 정보들을 저장합니다.
