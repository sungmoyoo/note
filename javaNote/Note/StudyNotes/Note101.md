# Jenkins

## 네트워크 브릿지 생성
- `$ sudo docker network create jenkins`


## 젠킨스 도커 컨테이너 생성 및 실행
- `$ sudo docker pull jenkins/jenkins:lts-jdk21` : 젠킨스 도커 이미지 가져오기
- `$ sudo docker image ls`



## 도커 이미지 만들기: 젠킨스 + JDK21 + 도커 클라이언트
젠킨스 컨테이너 안에 젠킨스 + JDK21 + 도커 클라이언트를 설치하는 이유?
- 편할라고....


- `mkdir jenkins`: 젠킨스를 설치할 폴더 준비
- `cd jenkins`

- `nano install-docker.sh`: 스크립트 파일
```shell
#!/bin/sh

apt-get update

apt-get -y install apt-transport-https \
     apt-utils \
     ca-certificates \
     curl \
     gnupg2 \
     zip \
     unzip \
     acl \
     software-properties-common

curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "$ID")/gpg > /tmp/dkey; apt-key add /tmp/dkey

add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/$(. /etc/os-release; echo "$ID") \
   $(lsb_release -cs) \
   stable" && \

apt-get update

apt-get -y install docker-ce
```

## 도커 빌드파일 생성
- `nano DockerFile`
```shell
FROM jenkins/jenkins:lts-jdk21

USER root

COPY install-docker.sh /install-docker.sh
RUN chmod +x /install-docker.sh
RUN /install-docker.sh

RUN usermod -aG docker jenkins
RUN setfacl -Rm d:g:docker:rwx,g:docker:rwx /var/run/

USER jenkins
```


## 도커 이미지 생성
- `$ docker build -t eomjinyoung/hello-docker:1.0 .`

## 도커 허브에 업로드
- `$ sudo docker login`

- `$ sudo docker push sungmoyoo/bitcamp:jenkins`


## 컨테이너 생성 및 실행
- `$ sudo docker run --privileged -d -v /var/run/docker.sock:/var/run/docker.sock -v jenkins_home:/var/jenkins_home -p 8080:8080 -p 50000:50000 --restart=on-failure --network="jenkins" --name docker-jenkins sungmoyoo/bitcamp:jenkins`

## 젠킨스 접속
- `$ sudo docker logs docker-jenkins`: 관리자 암호 찾기, initialAdminPassword 위의 부분이 암호

- 공인 IP 8080포트에 접속하여 잠금을 해제하고 Install suggested plugins를 눌러 플러그인 설치

- 젠킨스 관리자 등록
```
계정명: admin
암호: 1111
암호확인: 1111
이름: admin
이메일주소: xxx@xxx.xxx(내 이메일 주소)
```


## 젠킨스 환경설정
### JDK 및 Gradle 설정
- Dashboard / Jenkins 관리
  - System Configuration / Tools
    - JDK
      - `Add JDK` 클릭
      - Name: OpenJDK-21
      - JAVA_HOME: /opt/java/openjdk
        - 컨테이너에서 `which java`로 찾음
      - Apply 클릭
    - Gradle
      - `Add Gradle` 클릭
      - Name: Gradle 8.7
      - Install automatically: 체크
      - Version: 8.7 선택
      - Apply 클릭

## Jenkins에 프로젝트 등록 및 자동 배포하기

### github.com의 프로젝트 연동
- 새로운 Item
  - Enter an item name: `myapp`
  - `Freestyle project` 클릭
  - OK 클릭
- 설정
  - General
    - 설명: `myapp 빌드`
    - `GitHub project` 체크
      - Project url: `https://github.com/eomjinyoung/bitcamp-ncp-myapp.git`
  - 소스 코드 관리
    - `Git` 선택
      - Repository URL: `https://github.com/eomjinyoung/bitcamp-ncp-myapp.git`
      - Credentials: (push를 할 경우)
        - Add 버튼 클릭: `Add Jenkins` 선택
        - `Username with Password` 선택
          - Username: 깃허브 사용자이름
          - Password: 깃허브 토큰
          - 'Add' 클릭
        - `Username/토큰` 선택
      - Branch Specifier: \*/main
  - 빌드 유발
    - `GitHub hook trigger for GITScm polling` 선택
  - Build Steps
    - `Invoke Gradle script` 선택
      - `Invoke Gradle` 선택
        - Gradle Version: Gradle 8.7 선택
      - Tasks
        - `clean build` 입력
  - 저장
- `지금 빌드` 클릭
  - Console Output 확인
  - `docker exec -itu 0 docker-jenkins bash` 접속


### github webhook 연동

- Repository/Settings/Webhooks
  - `Add webhook` 클릭
    - Payload URL: `http://젠킨스서버주소:8080/github-webhook/`
    - Content type: `application/json`
    - 저장

### 스프링부트 애플리케이션 docker 이미지 생성 및 도커 허브에 push 하기

- Dashboard
  - `myapp` Freestyle 아이템 선택
    - `구성` 탭 선택
      - Build Steps
        - `Add build step` : Execute shell 클릭
          - `docker build -t [dockerHub UserName]/[dockerHub Repository]:[version] .`: Dockerfile 위치가 .
          - `docker login -u '도커허브아이디' -p '도커허브비번' docker.io`
          - `docker push [dockerHub UserName]/[dockerHub Repository]:[version]`
        - 저장

호스트 모드에서 권한 부여(permission denied)
- `$ sudo chmod 666 /var/run/docker.sock` 

- `$ `

- `$ `

- `$ `

- `$ `

- `$ `

- `$ `

- `$ `

- `$ `

- `$ `

- `$ `