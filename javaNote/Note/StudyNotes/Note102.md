# Jenkins 
# Continuous Deployment

## 작업 디렉토리 만들기
- `mkdir springboot`


## ssh key 생성
- `$ sudo docker container exec -itu 0 docker-jenkins /bin/bash`:루트 사용자로 컨테이너 접속, 0이 root

- `# ssh-keygen -t rsa -C "docker-jenkins-key" -m PEM -P "" -f /root/.ssh/docker-jenkins-key`: ssh 인증키 생성, 잠글 때 사용하는 public 키, 푸는 데 사용하는 private 키가 생성된다.

*fingerprint: 디지털 지문


## SSH-KEY 개인키 파일을 젠킨스 홈 폴더에 두기
- `# cp ~/.ssh/docker-jenkins-key /var/jenkins_home/`: 복사
- `# chmod +r /var/jenkins_home/docker-jenkins-key` : 사용자 권한 추가

## docker-jenkins 컨테이너의 SSH-KEY 공개키 파일을 Host로 복사해오기
server1
- `docker cp docker-jenkins:/root/.ssh/docker-jenkins-key.pub ./docker-jenkins-key.pub`
- `cat docker-jenkins-key.pub`: 확인


## 스프링부트 서버에 SSH-KEY 공개키 등록하기
server2 
- `$ sudo -i`
- `# mkdir .ssh`
- `# cd .ssh`
- `nano authorized_keys` -> 컨테이너 공개키 복사


## Jenkins에 Publish Over SSH 플러그인 설정

플러그인 설치

- Jenkins 관리
  - 플러그인 관리
  - `Available plugins` 탭
    - `Publish Over SSH` 플러그인 설치

플러그인 연동

- Jenkins 관리
  - 시스템 설정
    - Publish over SSH
      - Passphrase: 접속하려는 컨테이너의 root 암호 <=== 암호로 접속할 때
      - Path to key: docker-jenkins-key <=== SSH-KEY로 접속할 때(private key 파일 경로, JENKINS_HOME 기준)
      - Key: 개인키 파일의 내용 <=== SSH-KEY 개인키 파일의 내용을 직접 입력할 때
      - 추가 버튼 클릭
      - SSH Servers
        - Name: 임의의서버 이름
        - Hostname: 스프링부트서버의 IP 주소
        - Username: 사용자 아이디
        - `Test Configuration` 버튼 클릭
          - `Success` OK!


## Jenkins 서버에서 스프링부트 서버를 제어하여 스프링부트 컨테이너 실행하기

스프링부트 서버에서 docker pull 및 run

- Dashboard
  - `myapp` Freestyle 아이템 선택
    - `구성` 탭 선택
      - 빌드 환경
        - `Send files or execute commands over SSH after the build runs` 선택
          - SSH Server Name: Publish over SSH에 등록한 서버를 선택
          - Transfers
            - Exec command
              - `docker login -u '도커허브아아디' -p '도커허브비번' docker.io`
              - `docker pull [dockerHub UserName]/[dockerHub Repository]:[version]`
              - `docker ps -q --filter name=myapp2 | grep -q . && docker rm -f $(docker ps -aq --filter name=myapp2)` : 기존 같은 이름의 컨테이너가 있다면 삭제하겠다는 의미
              - `docker run -d --name myapp2 -p 80:80 -v /root/config:/root/config sungmoyoo/bitcamp:myapp2`: 위에서 삭제한 컨테이너 이름으로 새로 생성하겠다는 의미

              

