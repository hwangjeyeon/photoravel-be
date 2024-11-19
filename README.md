# Photoravel | 사진관광 플랫폼

![KakaoTalk_20241023_004439050_02](https://github.com/user-attachments/assets/c14f3d41-d423-48a3-8c60-235afe27a2e3)

TourAPI 및 공공데이터를 활용한 충청남도 사진관광 웹애플리케이션 서비스

## 백엔드 파트의 주요 개발 목표
- 사용자 위치기반 장소 탐색 서비스 개발
- 다수의 이미지 데이터 관리 시스템 개발
- 로컬 또는 소셜(카카오)계정 인증 시스템 개발
- 사진작가와 일반 회원간의 매칭 시스템 개발

## 백엔드 배포 아키텍처
![관광데이터활용공모전백엔드배포아키텍처](https://github.com/user-attachments/assets/5d427c36-fa86-44fe-9cea-44b377dd49dd)

## 백엔드 서버 아키텍처
![관광데이터활용공모전백엔드아키텍처](https://github.com/user-attachments/assets/27f82dd5-e68e-410e-960d-fca1b139b65e)

## DB ERD
![사진관광플랫폼공모전백엔드ERD](https://github.com/user-attachments/assets/4eb24cb8-9e68-4ca0-9273-45f0fd4de954)

## 주요 기능
![관광데이터활용공모전주요기능](https://github.com/user-attachments/assets/7a9cb05e-7e18-409d-a7f6-748d73a0fb2d)

## 담당 업무
- Location/Spot/Review 도메인 개발 담당 (CRUD)
- 이미지 데이터 관리 시스템 개발
  ![관광데이터활용공모전이미지개발운영환경](https://github.com/user-attachments/assets/46a81ccd-9919-4228-a694-8fd3c7313794)    
    - 이미지 데이터 관리환경 분리 (개발/운영환경)
    - 개발환경은 MinIO / 운영환경은 AWS S3로 분리하되, 동일한 AWS SDK 코드로 동작하도록 구성

#### 시퀀스 다이어그램
##### 이미지 저장
  ![이미지저장 로직](https://github.com/user-attachments/assets/33254873-aefb-4518-a55f-8c9ab783f59d)
  
S3에는 이미지 데이터, DB에는 S3 이미지 저장위치를 관리하도록 구성

- 사용자 위치기반 장소 탐색 서비스 개발
  ![위치기반장소탐색기능](https://github.com/user-attachments/assets/fd879e69-2f75-4594-9705-2dc48f8e92e8)
  - 탐색 범위 내에 위치하는 모든 장소 탐색 (경도/위도 기반)
  - Hibernate-Spatial을 구현한 MySQLSpatialDialect를 이용하여 개발  

## 기술 스택
| **Java 17** | **MySQL** | **Docker** | **Hibernate-Spatial** |
| --- | --- | --- | --- |
| **Spring boot 3** | **Junit5** | **AWS (EC2, RDS, S3)** | **AWS (Cloudfront)** |
| **Spring Data JPA** | **Git** | **Jenkins** | **MinIO** |
| **QueryDSL** | **Swagger** |  |  |
| **Spring Security** | |  |  |

## 프로젝트 기간
2024.05. ~ 2024.10.

## 팀구성
|황제연|성현석|신동욱|
|:-:|:-:|:-:|
|<img src="https://github.com/user-attachments/assets/fbb50a3d-9b16-48d9-a202-5ceea62d16e0" width=130px alt="황제연">|<img src="https://github.com/user-attachments/assets/5af257f0-75d0-47fd-b1af-305a2526517d" width=130px alt="성현석">|<img src="https://github.com/user-attachments/assets/9a537fd8-a588-42fa-b84a-68468ae40868" width=130px alt="신동욱">|
|[hwangjeyeon](https://github.com/hwangjeyeon)|[Seong57](https://github.com/Seong57)|[DongUk-Shin](https://github.com/DongUk-Shin)|

# 협업 방법

## 프론트엔드 개발자와 협업 방법
![swagger](https://github.com/user-attachments/assets/66133ef7-5050-42f9-a417-f74fe93695f4)
- Swagger로 REST API 문서화

## 백엔드 개발자간 협업 방법
### Github Wiki
- 협업 간, 이해를 돕기위해 Github Wiki를 작성하였습니다.
  - [MinIO 사용방법 및 사용목적](https://github.com/Trendravel/photoravel-be/wiki/Minio-%EC%82%AC%EC%9A%A9%EB%B0%A9%EB%B2%95-%EB%B0%8F-%EC%82%AC%EC%9A%A9%EB%AA%A9%EC%A0%81-%EC%A0%95%EB%A6%AC%EA%B8%80)


### Code Review Culture
![코드리뷰3](https://github.com/user-attachments/assets/7db1ed20-9263-412c-a130-361222102399)
![코드리뷰1](https://github.com/user-attachments/assets/17af43d2-2b00-4d26-a079-911002337122)
![코드리뷰2](https://github.com/user-attachments/assets/33ba2fa9-ef0f-47de-924e-27e68441f791)

- 코드 리뷰문화를 정착하여, 32번의 PR에서 296번의 코드리뷰를 작성했습니다


### Git Convention
- Main 브랜치는 Prototype 테스트 통과 이후 merge한다
- 모든 pull Request는 자신을 제외한 다른 두 팀원의 Code Review를 받은 후 수락한다

### Commit Message Convention
|Tag|           Description            |
|:-:|:--------------------------------:|
|Feat|            새로운 기능 추가             |
|Fix|             단순 버그 수정             |
|Style| 코드 포맷 변경, 세미콜론 누락, 필요없는 패키지 제거 등 |
|!HOTFIX|            치명적인 버그 수정            |
|Refactor|             코드 리팩토링              |
|Comment|          주석 추가 및 수정, 삭제          |
|Test|          테스트 코드 추가 / 변경          |
|Chore|       빌드 도구(Gradle) 추가/수정        |
|Rename|            파일,폴더명 수정             |
|Remove|              파일 삭제               |
|Config|             설정 파일 수정             |
|Move|             파일 위치 변경             |
|Docs|        문서 수정(README.MD 등)        |

### Example

```
Feat: location CRUD 컨트롤러 기능 구현 **<-- Head (머릿말)**
<br> 1번 컨트롤러는 Create,DELETE 2번 컨트롤러는 READ, UPDATE를 담당합니다. **<-- Body (본문)**
```

### Code Convention
- 변수명 camelCase 준수
- 들여쓰기 탭 사용
- 한줄 최대 글자수 100자 제한
