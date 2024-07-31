# photoravle-be

포토래블 백엔드 팀 메인 브랜치


## Team
|황제연|성현석|신동욱|
|:-:|:-:|:-:|
|<img src="https://github.com/user-attachments/assets/fbb50a3d-9b16-48d9-a202-5ceea62d16e0" width=130px alt="황제연">|<img src="https://github.com/user-attachments/assets/5af257f0-75d0-47fd-b1af-305a2526517d" width=130px alt="성현석">|<img src="https://github.com/user-attachments/assets/9a537fd8-a588-42fa-b84a-68468ae40868" width=130px alt="신동욱">|
|[hwangjeyeon](https://github.com/hwangjeyeon)|[Seong57](https://github.com/Seong57)|[DongUk-Shin](https://github.com/DongUk-Shin)|


## Git Convention
- Main 브랜치는 Prototype 테스트 통과 이후 merge한다
- Prototype 브랜치는 자신을 제외한 다른 두 팀원의 Code Review를 받은 후 Rebase한다.
- 모든 pull Request는 자신을 제외한 다른 두 팀원의 Code Review를 받은 후 수락한다

### Commit Message Convention
|Tag|Description|
|:-:|:-:|
|Feat|새로운 기능 추가|
|Fix|단순 버그 수정|
|Style|코드 포맷 변경, 세미콜론 누락, 필요없는 패키지 제거 등|
|!HOTFIX|치명적인 버그 수정|
|Refactor|코드 리팩토링|
|Comment|주석 추가 및 수정, 삭제|
|Test|테스트 코드 추가 테스트 코드 변경|
|Chore|빌드 도구(Gradle) 수정, 패키지 추가|
|Rename|파일,폴더명 수정|
|Remove|파일 삭제|
|Config|설정 파일 수정|
|Move|파일 위치 변경|
|Docs|문서 수정(README.MD 등)|

### Example
--- 

Feat: location CRUD 컨트롤러 기능 구현 **<-- Head (머릿말)**
<br> 1번 컨트롤러는 Create,DELETE 2번 컨트롤러는 READ, UPDATE를 담당합니다. **<-- Body (본문)**

--- 

Footer의 경우 개발 진행 중 버그가 발생했을 때, 컨벤션 논의 후 추가 예정


## Code Convention
- 변수명 camelCase 준수
- 들여쓰기 탭 사용
- 한줄 최대 글자수 80자 제한
