# Spring Boot

## Spring Boot의 배포 방식
SpringBoot에서 
web-app은 jar파일로 배포할 때 묶이지 않아 리소스로 옮긴다.


기존의 86번까지는 설정 보면 경로가 DispatcherServlet의 경로는 /app/*인데 SpringBoot에서는 /*이다.


## monolithic과 microservice

## docker
기능을 잘게 분리시켜 작은 독립컴퓨터가 실행하는 구조,
위처럼 microservice 기술 구동에 적합한 클라우드 아키텍쳐 기술


## kubernetis
docker를 자동화하는 것


## microservice의 잘못된 예
모든 서비스가 DB를 공유하는 것은 완전한 microservice가 아니다.

*그렇다고 교조주의에 빠지지 말 것*
- 서비스별로 DB도 분리해야 완전한 마이크로 서비스이다.
- 하지만 무작정 분리하여 마이크로 서비스로 만드는 것이 정답이 아니다. 
- 바람직한지 아닌지 판단하여 조율하는 것이 필요하다.
