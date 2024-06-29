## 호스트 볼륨 공유하기

- `$ sudo docker run -d --name wordpressdb_hostvolume -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=wordpress -v /home/wordpress_db:/var/lib/mysql mysql:5.7 `
- `-v 호스트의공유디렉토리:컨테이너의공유디렉토리`

호스트의 공유 디렉토리가 없으면 도커가 자동 생성한다.

즉 호스트의 디렉토리를 컨테이너가 사용하는 개념으로 호스트의 디렉토리가 컨테이너의 디렉토리를 가리킨다.

추가로 컨테이너가 삭제되어도 호스트 디렉토리에 데이터는 보존된다.


## 도커 볼륨 사용하기
- `sudo docker volume create --name myvolume` : 가상 하드디스크(볼륨) myvolume을 생성
- `sudo docker run -i -t --name docker1 -v myvolume:/root/ ubuntu:14.04` : myvolume을 사용하는 docker1 컨테이너를 생성하고 접속

- `cd root`

- `echo Hello, volume!` : 일반 출력
- `echo Hello, volume! >> test.txt` : 출력 결과를 파일로 리디렉션

- `sudo docker run -i -t --name docker2 -v myvolume:/root/ ubuntu:14.04` : myvolume을 사용하는 docker2 컨테이너를 생성하고 접속

- `cd root`
- `ls` : 다른 컨테이너이지만 같은 Volume을 공유하였기에 test.txt 파일이 존재하는 것을 확인할 수 있다. 

## 도커 볼륨의 실제 위치 알아내기
- `$ sudo docker inspect --type volume myvolume` : 컨테이너, 이미지, 볼륨 등 도커의 모든 구성 단위의 정보를 확인할 때 `inspect --type [image|volume|container]` 명령을 사용한다.

## 도커 볼륨 자동생성
- `sudo docker run -i -t --name volume_auto -v /root/ ubuntu:14.04` : 도커를 생성할 때 볼륨을 지정하지 않으면 자동으로 생성한다.

- `sudo docker container inspect volume_auto` : 권장되는 inspect 방법

<!-- ## 사용하지 않는 볼륨 자동삭제
- `$ sudo docker volume prune` --> 제대로 실행되지 않음

# 도커 네트워크
- `$ ifconfig` : 실행 중인 컨테이너 개수 만큼 `vethxxxx`(virtual ethernet) 가상 이더넷 카드가 생성된 것을 확인 할 수 있다.

## 컨테이너의 네트워크
- `$ sudo docker network ls` : 도커 엔진의 네트워크 목록 조회

> bridge: 컨테이너를 생성할 때 자동으로 연결되는 docker0 브리지를 활용하도록 설정됨.  
>172.17.0.x IP 대역을 컨테이너에 순차적으로 할당한다.

- `$ sudo docker network inspect bridge` : 특정 네트워크의 상태 조사

# 브릿지 네트워크
## 브릿지 바인딩 조회
- `$ sudo apt-get install bridge-utils` : 브릿지 바인딩 정보 조회를 위한 도구 설치
- `$ brctl show docker0` : 브릿지 바인딩 정보 조회

## 브릿지 네트워크 생성
- `$ sudo docker network create --driver bridge mybridge` : 새 브릿지 네트워크 생성
- `$ sudo docker run -it --network="mybridge" --name network1 ubuntu:14.04` : 새 컨테이너에 내가 만든 브릿지 네트워크 연결
- `$ sudo docker network inspect mybridge` : 브릿지 네트워크 상세 정보 보기, 컨테이너가 실행되고 있을 때 container 정보를 확인할 수 있다.

브릿지 네트워크를 직접 생성하면 새 IP address를 부여할 수 있다. 예를 들면 브릿지를 생성할 때 새로 생기는 IP 서브넷에 컨테이너 IP가 묶인다.

## 컨테이너에 브릿지 네트워크를 붙이기/떼기
- `$ sudo docker network disconnect mybridge network1` : 브릿지 네트워크 제거 후 확인
- `$ sudo docker attach network1`
- `# ifconfig`

- `$ sudo docker network connect bridge network1` : 컨테이너에 브릿지 네트워크 붙이고 확인
- `$ sudo docker attach network1`
- `# ifconfig`

# 컨테이너 로깅
## 도커 이미지 검색
- `$ sudo docker search 키워드` : 도커 허브라는 중앙 이미지 저장소에서 도커 이미지 검색하기

## 도커 이미지 생성
- `$ sudo docker image ls` : 이미지 목록 조회 

- `$ sudo docker sudo docker container exec -i -t commit_test /bin/bash` : 컨테이너 접속

- `# echo Hello! -> hello.txt` : 리디렉션(컨테이너 변경)

- `$ sudo docker container commit -a "bitcamp" -m "my first commit" commit_test commit_test:first` : commit_test라는 컨테이너를 first라는 태그를 가진 commit_test라는 이미지로 만든다.

- `$ sudo docker container commit -a "bitcamp" -m "my second commit" commit_test commit_test:second`: 커밋은 여러번 할 수 있다.

커밋한 컨테이너를 다시 가져와 확인해보면 리디렉션으로 생성한 hello.txt, test.txt도 그대로 들어있는 것을 알 수 있다.

>즉 커밋이란 컨테이너 정보를 포함한 모든 변경 사항을 덧붙여 백업한다는 의미이다. 
>따라서 어떤 컨테이너를 기준으로 커밋을 하느냐가 중요하다. 
>예를 들면 하나의 컨테이너로 커밋을 두번하더라도 두 커밋의 변경사항은 하나가 될 수 없다. 하지만 한 컨테이너로 커밋하고 그 이미지로 컨테이너를 생성한 후 다시 커밋하면 기존 백업 내용이 남아 두 변경사항이 모두 백업된다.
>이 때 백업된 내용(히스토리)를 Layer라고 한다. 


- `$ sudo docker history commit_test:first`: 이미지 레이어 구조에 대한 변경 내역 확인하기

## 도커 이미지 삭제
- `$ sudo docker image rm commit_test:first` : 이미지 삭제

이미지를 사용 중인 컨테이너가 있을 경우 삭제할 수 없다.  
컨테이너를 먼저 삭제한 후 이미지를 삭제해야 한다.

- `$ sudo docker stop commit_test2 && sudo docker rm commit_test2`

만약 강제로 삭제하고 싶다면 -f 옵션을 주면 된다.
- `$ sudo docker image rm -f commit_test:first`

이후 컨테이너를 조회하면 이미지 이름이 변경되어 있는 것을 확인할 수 있다. 또 image를 조회하면 이름과 태그가 `<none>`으로 변경되어 있다. 이를 댕글링 이미지라고 한다. 


## 도커 이미지 추출
- `$ sudo docker save -o ubuntu_14_04.tar ubuntu:14.04`: tar 확장자로 이미지를 묶는다.

- `$ sudo docker load -i ubuntu_14_04.tar`: 이미지를 로드한다.

## 도커 이미지 배포

- 도커 허브(<https://hub.docker.com/>)에 회원 가입 및 로그인
- 이미지 저장소 생성
- 테스트용 이미지 생성
  - `$ sudo docker container create -i -t --name docker1 ubuntu:14.04`
  - `$ sudo docker container start docker1`
  - `$ sudo docker container exec -i -t docker1 /bin/bash`
  - `$ echo test..ok! >> hello.txt`
  - `$ sudo docker container commit -a "bitcamp" -m "my image" docker1 myimage:first`

- 이미지 태깅
  - `$ sudo docker tag myimage:first bitcamp:first`
  - `$ sudo docker image tag bitcamp:first sungmoyoo/bitcamp:first`

- 도커 허브에 로그인
  - `$ sudo docker login`

- 저장소에 이미지 업로드
  - `$ sudo docker push sungmoyoo/bitcamp:first`

- 저장소에서 이미지 다운로드
  - `$ sudo docker pull sungmoyoo/bitcamp:first`
  - `$ sudo docker run -i -t --name docker1 sungmoyoo/bitcamp:first`

# WISIWIC 자바스크립트 에디터

