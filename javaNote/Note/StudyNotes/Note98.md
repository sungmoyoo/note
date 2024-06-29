# 도커에서 mysql 사용

## myapp 프로젝트 독립 저장소로 분리

## 터미널에서 gradle로 myapp 실행

## MySQL image 다운

## MySQL container 생성
- `$ sudo docker run --name mysql -e MYSQL_ROOT_PASSWORD=1111 -e MYSQL_DATABASE=studydb -e MYSQL_USER=study -e MYSQL_PASSWORD='Bitcamp!@#123'  -d mysql:latest --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci`

## mysql 실행
- `$ sudo docker container exec -i -t mysql /bin/bash`: 컨테이너 접속
- `# mysql -u study -p`
- mysql 사용

## 컨테이너 접속하지 않고 바로 mysql 접속
- `$ sudo apt install mysql-client`: MySQL 클라이언트 설치
- `$ mysql -h 원격DB IP -u study -p`: 바로 접속

## Ubuntu 22.04 컨테이너 생성 및 실행
- `$ sudo docker run -p 80:80 -it --name myapp ubuntu`

## ifconfig 등 네트워킹 관련 프로그램 추가
- `# apt update`
- `# apt install net-tools`

## nano 에디터 설치
- `# apt install nano`

## JDK 21 설치
- `# apt install openjdk-21-jdk -y`

## JAVA_HOME 환경 변수 설정
**주의! 안되는 방법**
- `# echo 'export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64' | tee /etc/profile.d/java21.sh`: `` 안에 내용을 화면으로도 출력하고 /etc/profile.d/java21.sh 경로에 파일로도 출력한다는 명령어
- `# cat /etc/profile.d/java21.sh` 파일 안에 내용 확인(화면에 출력)

- `# source /etc/profile.d/java21.sh`: 환경변수 소스 지정
- `# echo $JAVA_HOME`: 환경변수 확인
컨테이너를 종료하고 다시 접속하면 환경변수가 등록이 안되어 있다. 이 방법은 사용하지 말자

**제대로 작동하는 방법**
- `# echo 'export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64' | tee -a /etc/bash.bashrc`
- `# source /etc/bash.bashrc`
myapp을 종료하고 다시 와서 확인해보면 정상적으로 등록된 것을 확인할 수 있다.
- `# echo $JAVA_HOME`

>bash.bashrc?
>bash.bashrc는 bash를 시작할 때 자동으로 실행하는 파일이다. bash.bashrc에 환경변수를 등록하는 코드를 작성해놓으면 bash가 시작될 때 환경변수를 자동으로 등록한다.

## git 설치
- `# apt install git -y`

## wget 설치
- `# apt install wget -y`

## unzip 설치
- `# apt install unzip -y`

## Gradle 설치 (gradlew가 있으면 설치할 필요X)
- `# VERSION=8.7`: 환경변수 설정
- `# wget https://services.gradle.org/distributions/gradle-${VERSION}-bin.zip -P /tmp`: cli용 http client, ${VERSION} 자리에 환경변수값이 들어감
- `# unzip -d /opt/gradle /tmp/gradle-${VERSION}-bin.zip`: unzip 통해서 압축 풀기 
- `# ln -s /opt/gradle/gradle-${VERSION} /opt/gradle/latest`
- `# echo 'export GRADLE_HOME=/opt/gradle/latest' | tee -a /etc/bash.bashrc`: apt로 설치한 것이 아니라 wget으로 따로 다운로드받아 설치했기 때문에 환경변수를 직접 지정해주어야 한다. 
- `# echo 'export PATH=${GRADLE_HOME}/bin:${PATH}' | tee -a /etc/bash.bashrc`: PATH 경로 설정
- `# source /etc/bash.bashrc`

## myapp 프로젝트 클론
- `# mkdir git`
- `# git clone https://github.com/eomjinyoung/bitcamp-study`

- resources 폴더에서 ncp.secret.properties 생성
- application.properties
