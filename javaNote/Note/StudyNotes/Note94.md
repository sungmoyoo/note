# URL 주소 다루기
1. Web Browser 관점
- HTML이나 JavaScript에서 URL을 읽을 때
  >현재 URL = http://localhost:8888/web/app/board/form
  >http://localhost:8888/ <- server root
  >/ 
`<a href="add"/>` 
서버에서는 상대경로로 인식하여 http://localhost:8888/web/app/board/add가 요청된다.

`<a href="/add"/>`
절대경로로서 http://localhost:8888/add가 요청된다.

`<a href="../../add"/>`
리소스명을 제외한 폴더에서 상위 폴더로 두번 이동하여 http://localhost:8888/web/add가 요청된다.

2. Thymeleaf 관점
- 서버에 HTML 템플릿에 지정된 경로
  >현재 URL = http://localhost:8888/web/app/board/form
  >web/ <- context root, 현재 웹 애플리케이션 경로
  >app/ <- dispatcherServlet 경로
  >board/form <- requestHandler url

`@{add}`
상대경로로서 http:localhost:8888/web/app/board/add

`@{/add}`
context root를 가리킨다. http://localhost:8888/web/add

`@{~/add}`
server root를 가리킨다. 다른 웹 애플리케이션 경로로 설정할 때 사용한다. 
http://localhost:8888/add


Spring에서는 context root가 "/"로 설정되어 Web Browser의 context Root와 같다. 
그럼에도 Web Browser와 Thymeleaf(Server)에서 경로를 바라보는 관점이 다르니 알아두어야 한다.


# myapp 페이징 처리





# Docker
WSL
가상머신


## 도커 설치
### 도커 설치 스크립트 다운
- `# apt-get update`: 업데이트
- `$ curl https://get.docker.com > docker-install.sh`: 쉘스크립트 다운
- `$ chmod u+x docker-install.sh`: 유저가 실행할 수 있도록 실행 권한 부여



### 도커 설치
- `$ ./docker-install.sh`: 쉘스크립트에 bin/sh 표시 있으면 앞에 sh 생략가능

## 도커 컨테이너 다루기
도커 컨테이너를 다루기에 앞서 컨테이너, 이미지의 개념과 구동원리를 이해해야 한다.

1. 이미지  
운영체제 또는 컨테이너에 디렉토리 구조를 그대로 압축하여 만든 것을 이미지  파일이라 한다. 이미지 파일은 컨테이너 실행에 필요한 파일과 설정 값 등 모든 것을 포함하는 템플릿이다.

2. 컨테이너  
컨테이너는 이미지를 실행한 런타임 인스턴스이다.
이미지 파일을 다운받아 실행하게 되면 이미지 파일에 담겨있는 디렉토리 구조가 그대로 해당 저장장치에 설치되는 방식이다.

자바에 비유하면 이미지는 클래스, 컨테이너는 인스턴스라 할 수 있다. 

<img src="../../img/OS-ImageFile.png">

<br>

## 도커

### 우분투 14.04 컨테이너 생성 및 종료
- `$ sudo docker run -i -t ubuntu:14.04`: 도커허브에서 ubuntu:14.04 버전의 이미지를 찾고 없으면 다운받으라는 명령어, 이후 컨테이너에 접속한다.
- `-i` : 상호 입출력 하겠다고 설정, 컨테이너 바깥에서 
- `-t` : tty를 활성화해서 bash 셸을 사용하도록 설정, TeleTypewriter의 약자 유닉스나 유닉스와 같은 운영체제에서 standard input 에 연결된 터미널의 파일 이름을 출력하기 위한 명령어이다.
출처: https://jake-seo-dev.tistory.com/115 [제이크서 위키 블로그:티스토리]


- `$ exit` 또는 `Ctrl + D` : 배시셸을 종료함으로써 컨테이너를 정지시킨다.
- `Ctrl + P` 다음에 `Ctrl + Q(Ctrl + P, Q)`: 단순히 컨테이너의 셸만 빠져나온다.


### 도커 컨테이너 실행 세부 단계
- `$ sudo docker pull centos:7`: 도커 이미지 내려받기
- `$ sudo docker images`: 도커 이미지 목록 확인하기
- `$ sudo docker create -i -t --name mycentos centos:7`: 도커 이미지로 컨테이너 생성하기
- `$ sudo docker start mycentos` : 도커 컨테이너 실행하기
- `$ sudo docker start 해시아이디일부분` : 위와 동일
- `$ sudo docker attach mycentos`: 도커 컨테이너에 들어가기

> 위 과정을 한번에 수행해주는 것이 `$ sudo docker run -i -t ubuntu:14.04` 명령이다.

<br>

- `$ sudo docker container ls (-a, al, ...)`: 도커 컨테이너 목록 확인
- `$ sudo docker inspect mycentos`: 컨테이너 정보?

- `$ sudo docker stop interesting_brown`: 실행 중인 도커 컨테이너 중지
- `$ sudo docker rm mycentos` : 컨테이너 삭제하기

- `$ sudo docker rm -f mycentos`: `-f` 옵션을 사용하여 종료와 삭제를 한 번에 처리하기
- `$ sudo docker container prune`: 정지된 모든 컨테이너 삭제하기



