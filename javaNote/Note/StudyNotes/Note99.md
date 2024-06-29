# CICD
## Spring Boot 설정 분리
application.properties에 담긴 Spring Boot 설정은 개발할 때와 개발이 완료되어 배포할 때 설정이 다르다, 또한 Docker를 사용하여 자동화할 때도 설정이 달라진다. 매번 설정하기 귀찮으면 설정을 분리하여 원하는 설정을 선택하는 방법도 존재한다. 

1. 한 파일 안에서 분리
application.properties 안에서 # -- 로 분리하는 방법

2. 여러 파일로 분리
>application-dev.properties  
>application-prod.properties  
>로 분리

## 설정 파일을 프로젝트 저장소 밖으로 이동
설정파일을 repository 안에 두면 변경사항이 생겼을 때 pull을 할 수 없는 상황이 발생할 수 있다. 따라서 설정 파일을 저장소 밖의 로컬폴더로 이동하고 환경변수를 지정해 사용하는 것이 좋다.
PropertySource로 지정하는 범위는 프로젝트 폴더 밖도 가능하다. 시스템에서 제공하는 file 표현식을 사용
```java
@PropertySource({
    "file:${user.home}/config/jdbc.properties",
    "file:${user.home}/config/ncp-secret.properties",
    "file:${user.home}/config/ncp.properties"
})
public class App{...}
```

## 터미널에서 실행
설정을 분리하였기에 터미널에서 실행할 때도 profile을 찾지 못하는 에러가 발생한다. 
```
java -Dspring.profiles.active=dev -jar app/build/libs/app-0.0.1-SNAPSHOT.jar 
```

## Spring Boot를 실행할 때 모드 지정
1. JVM argument
- `$ java -Dspring.profiles.active=dev -jar`

- `$ java `

## Docker 컨테이너가 사용할 설정파일을 다루기
1. 컨테이너에 만드는 방식
컨테이너 /root/config에 직접 생성하는 방식

2. 호스트 볼륨 공유 방식
```
Host
~/config/*.properties 

Container
/root/config

Container
/root/config

Container
/root/config
```
위의 config 설정파일을 여러 컨테이너에 연결, 모든 컨테이너는 Host의 설정파일을 공유하는 방식이다. 

이미 컨테이너를 생성한 후에는 볼륨을 지정할 수 없다.


## Spring Boot를 Docker 컨테이너에서 실행하기
1. 방법1(기존)

2. 방법2

## 방법2 적용
### 자바 설치
- `sudo apt install openjdk-21-jdk -y`: 설치
- `sudo echo 'export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64' | tee -a .bashrc`: 환경변수 설정
- `sudo source .bashrc`: 환경변수 적용

### Gradle 설치
- `$ VERSION=8.7`: 환경변수 설정
- `$ sudo wget https://services.gradle.org/distributions/gradle-${VERSION}-bin.zip -P /tmp`: cli용 http client, ${VERSION} 자리에 환경변수값이 들어감
- `$ sudo unzip -d /opt/gradle /tmp/gradle-${VERSION}-bin.zip`: unzip 통해서 압축 풀기 
- `$ sudo ln -s /opt/gradle/gradle-${VERSION} /opt/gradle/latest`
- `$ sudo echo 'export GRADLE_HOME=/opt/gradle/latest' | tee -a .bashrc`
- `$ sudo echo 'export PATH=${GRADLE_HOME}/bin:${PATH}' | tee -a .bashrc`
- `$ source .bashrc`

### Docker 파일 작성
- git/myapp 에 Dockerfile 생성(이름 정확히)
```
FROM openjdk:17

ARG JAR_FILE=build/libs/app-server-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT [ "java", "-Dspring.profiles.active=prod", "-jar", "app.jar" ]
```

### Dockerfile로 이미지 생성하기
- `docker build -t sungmoyoo/bitcamp:myapp .`: 현재 작업폴더에서 Dockerfile을 찾아 명시된 대로 현재 폴더에 이미지를 생성해준다.


### 컨테이너 생성 및 실행하기
Window
-`docker run -d -p 80:80`

linux/macOS
