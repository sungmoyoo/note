# 멀티태스킹과 멀티VM

## Multi-Processing VS Multi-Threading
- 작업을 실행하기 위해 프로세스를 fork(복제)하는 방식으로 멀티태스킹을 구현한다. 이 방식의 문제점은 코드와 Heap 메모리도 중복 생성되어 메모리가 낭비된다는 점이다.

- 멀티스레드는 Stack만 가지고 실행코드와 Heap 메모리는 공유한다. 따라서 멀티프로세싱 방식에 비해 메모리를 덜 사용하기 때문에 더 많은 작업을 수행할 수 있다. 

## VM방식 VS Container 방식


- 컨테이너 방식은 컨테이너 엔진을 통해 OS 기능을 공유한다. OS를 설치를 하지 않고도 VM처럼 독립적으로 동작하고 메모리를 적게 사용한다. 

<br>

# Docker

## Docker 명령
> docker 명령 명령 옵션 ex) # docker container ls -a
> docker 단축명령 옵션 ex) # docker ps -a

### 도커 이미지 다루기
- `sudo docker image ls` : 도커 이미지 리스트
- `sudo docker image rm eeb6ee3f44bd` : 도커아이디를 통해 도커이미지 삭제
- `sudo docker image pull ubuntu:14.04`


## 도커 컨테이너 외부에 노출하기
- `$ sudo docker run -i -t --name network_test ubuntu:14.04`
- `# ifconfig`: NIC 확인

컨테이너를 노출 시키기
- `$ ssudo docker container create -i -t --name mywebserver -p 80:80 ubuntu:14.04`

**포트번호?**
> docker container create -p 80:8080 
80은 리눅스 서버에 보내는 요청이고, 8080은 컨테이너의 포트번호가 들어가는 자리이다. 80 요청이 들어오면 8080 포트번호를 가진 컨테이너를 연결한다는 뜻이다.

**컨테이너에 아파치 웹 서버 설치 및 시작시키기**
- `root@xxx# apt-get update`
- `root@xxx# apt-get install apache2 -y`
- `root@xxx# service apache2 start`

**아파치 웹 서버 종료**
- `root@xxx# service apache2 stop`

**호스트와 바인딩된 포트번호 확인**
- `$ sudo docker port mywebserver`

### Detached 모드 컨테이너의 내부 셸을 사용하기
- `$ sudo docker container exec` : 컨테이너 바깥에서 컨테이너에게 명령을 실행시키기  
-> attach를 통해 container에 접속하지 않아도 컨테이너에 접속한 것처럼 단일 명령을 처리할 수 있다. 

- `$ sudo docker container exec -i -t 컨테이너이름 /bin/bash`: 상호 입출력 가능한 상태로 접속하기  
-> attach와 동일하게 컨테이너에 접속한다. attach와 달리 가볍고 exit해도 컨테이너가 자동 stop되지 않고 계속 실행한다. 

- `$ sudo docker exec 컨테이너이름 ls`: 컨테이너 내부의 실행 결과만 확인하기


## 도커 컨테이너 활용
### 데이터베이스 컨테이너와 웹서버 컨테이너 생성
- `$sudo docker run -d --name wordpressdb -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=wordpress mysql:5.7` : 데이터베이스 도커 이미지 다운로드 

- `$ sudo docker run -d --name wordpress -e WORDPRESS_DB_HOST=mysql -e WORDPRESS_DB_USER=root -e WORDPRESS_DB_PASSWORD=password --link wordpressdb:mysql -p 80 wordpress`: 워드프레스 기반 블로그 서비스 생성, 이미지에 워드프레스 블로그 서비스가 포함되어 있기 때문에 설정값을 정해진 규칙에 따라 지정해주면 된다.

도커 컨테이너 run 옵션

- `-d` : Detached 모드로 컨테이너 실행. 컨테이너에서 백그라운드에서 동작하는 애플리케이션을 실행할 때 사용.
  - 이 모드에서는 컨테이너가 실행하는 프로그램이 없으면 자동 종료된다.
  - 테스트: `$ sudo docker run -d --name detach_test ubuntu:14.04`
  - `$ sudo docker ps -a` 로 확인해보면 컨테이너가 실행 즉시 종료되었음을 확인할 수 있다.
  - `-e 환경변수명=값` : 컨테이너 내부의 환경변수 설정. 컨테이너에서 실행되는 애플리케이션이 이 환경 변수를 사용한다.
- Detached 모드 컨테이너의 내부 환경 변수 확인하기
  - 셸 접속: `$ sudo docker exec -i -t wordpressdb /bin/bash`
  - 환경 변수 확인: `# echo $MYSQL_ROOT_PASSWORD`
- `--link 컨테이너명:별명` : 내부 IP를 알 필요 없이 컨테이너 별명으로 접근하도록 설정
  - 도커 엔진은 컨테이너에게 내부 IP를 172.17.0.2, 3, 4, ... 와 같이 순차적으로 할당.
  - 테스트: `$ sudo docker exec wordpress curl mysql:3306 --silent`

>-i -t 옵션을 주지 않았을 경우?  
>컨테이너가 실행하고 있는 프로그램이 없을 경우 컨테이너를 강제 실행시켜버린다.  
-i -t 옵션은 실행하고 있는 프로그램이 없더라도 컨테이너를 계속 start된 상태로 유지한다.