# GPU server(AI 연산용)
Path Through
GPU를 고르면 그에 맞는 세팅이 자동 결정




# load balancer
서버가 장애가 생기면 처리하기 위해 또는 성능을 위해 서버를 늘릴 때 컴퓨터에게 작업을 나눠주는 장치, 즉 부하를 분산처리하는 장치

# Global DNS
## DNS 서비스
nslookup
dig

www.naver.com 을 찾아가고자 할 떄

1단계
- 1st DNS에 naver.com의 IP 주소를 물어본다
- 2nd DNS에 naver.com의 IP 주소를 물어본다
168.126.63.1 => KT
168.126.63.2 => KT

2단계
168.126.63.1의 1st DNS에 naver.com의 IP 주소를 물어본다.

3단계 
4단계 
- IANA => 최상위 도메인 관리 기관
naver.com의 주소는 125.209.248.6이라는 것을 확인 

5단계 
168.126.63.1이 125.209.248.6이라는 것을 확인
그러면 naver.com 네임 서버는 223.130.200.247이라고 응답한다. 이정보를 나에게 보내준다.

6단계 
223.130.200.247에 접속한다.

### 레코드 타입
A
IPv4의 IP를 지정

NS, delegation(위임)
```
낙수 전자 => www.elec.naksoo.com
mail.elec.naksoo.com
...
..
```

SOA
www.naksoo.com => 175.45.203.138

PTR
175.45.203.138 => www.naksoo.com

CNAME
아이피가 변경되었을 때 캐싱된 데이터를 가지고 옛 아이피로 접속하는 상황을 방지하기 위해 사용
www.naksoo.com => 175.45.203.138
www.naksoo.com => web001.naksoo.com
web001.naksoo.com => 175.45.203.138

# Global Traffic Manager(GTM)
GTM
- Geolocation
- Round-Robin
- Failover

GSLB
- RR, W, FO


# CDN(Content Delivery Network)
컨텐츠를 Cashing 하여 보다 빠르고 안정적으로 사용자에게 전송하는 서비스 

언제 필요한가?
- 대규모 파일 배포나 이미지/동영상 서비스 등 대규모 트래픽이 발생하는 경우

CDN이 터지는 경우는?
없다. 서버는 터지지만 네트워크는 눌린다.
만약 1GB짜리 파일이 있을 때 

* purge
cache의 Expiry 기한이 지나지 않아도 강제로 변경사항을 CDN에 적용하는 기술

# VPN
SSL VPN
- 외부에서 서버 접속시 보안 통신이 가능하도록 보안 소켓 계층 기반의 가상 사설망

IPSEC VPN
- 고객의 사내망과 플랫폼 간 사설 통신을 위한??????

*gateway: 네트워크에서 다른 네트워크로 이동하기 위해 거쳐야하는 지점

# Auto Scaling
서버 수를 자동으로 증가 또는 감소시켜 안정적인 서비스를 유지하고 비용을 절감하는 기술. 미리 등록한 설정 ex) cpu 사용률 50% 차면 100% 증가, 

## Event Rule
1단계: 모니터링할 상품 선택
2단계: 모니터링할 항목 선택
3단계: 임계치 설정
4단계: 임계치 돌파 시 취할 액션

## Cloud Fucntion
별도의 서버나 인프라를 프로비저닝 하거나 관리하지 않고 코드를 실행할 수 있는 서버리스 컴퓨팅. 
auto scaling, kubernetis를 사용하지 않고 코드를 올려 서버에 대한 코드를 실행한다.????


# File System
os에서 보조 기억 장치에 저장되는 파일을 관리하는 시스템의 통칭르로 파일을 저장할 클러스터를 관리하고 파일명에 대한 규칙, 데이터의 저장과 검색을 관장한다.
File System은 os에 종속적이라 os가 달라지면 저장방식도 달라진다.

## 로컬 파일 시스템 방식
**연속 할당 방식**  

**비연속 할당 방식**  
- 연결 할당 방식: 지금 안씀
- 색인 할당 방식: 최근 대부분이 이 방식 사용

## 로컬 파일 시스템의 단점
- 용량의 한계
- 장애 복구 한계
- 다양한 접근 경로를 제공하지 않음

## 네트워크 파일 시스템(NFS, CIFS)
네트웤을 통해 파일 시스템에 접근, 용량을 높이려면 고가의 인프라 비용이 발생한다.

- 분산 파일 시스템(HDFS, ADF, CODA): 파일을 개서 노드에 분산&복제하는 방식 

## 스토리지 타입
- block storage
- object storage

# Object Storage
인터넷상에 원하는 데이터를 저장하고 사용할 수 있도록 구축된 객체 기반의 무제한 파일 저장 스토리지

## storage 계층
- cold storage: 자주 사용되지 않는 정형 또는 비정형 데이터를 위한 스토리지
- warm storage: 적당히 자주 사용되는 구조화된 데이터를 위한 스토리지 
- hot storage: 자주 사용하는 구조화된 데이터를 위한 스토리지.

*data-lifecycle: hot storage에 있는 data가 시간이 지남에 따라 일정 조건(자주 사용x)에 맞으면 warm storage나 cold storage로 옮기는 것

# Archive Storage
데이터 아카이빙 및 장기 백업에 최적화된 스토리지 서비스
Object Storage보다 저렴하지만 데이터 처리 api는 비쌈

# NAS(Network-Attached Storage)
다수의 서버에서 공유하여 사용할 수 있는 스토리지, 파일로 데이터에 액세스.  
클라이언트 OS 관점에서 파일서버로 인식한다.

# Data Teleporter
대용량 데이터 이전을 위한 어플라이언스
매우 크고 빠른 USB라 보면 됨

# Backup
- Full Backup: 전체 백업
- 증분 Backup: 리스토어시 백업,  이전 백업을 기준으로 순차적으로 저장
- 차등 Backup: 리스토어는 Full 백업 원하는 시점, 풀 백업을 기준으로 저장

# Cloud DB for MySQL
RDBMS
이중화 지원
기본적인 백업 지원(1달)

```
디스크 시작용량: 10GB
디스크 증가용량: 10GB
디스크 최대용량: 6TB
백업기간: 30일
포트: 3306
슬레이브: 10대
private subnet: O
public subnet: O
```

데이터베이스의 이중화는 복잡. 로드 밸런서는 사용할 수 없다. 무결성이 깨져버리기 때문

*Fail-over?


# Cloud DB for Redis
자동 복구를 통해 안정적으로 운영되는 완전 관리형 클라우드 인메모리 캐시 서비스

```
디스크 시작용량: -
디스크 증가용량: -
디스크 최대용량: -
백업기간: 7일
포트: 6379
슬레이브: 4대
private subnet: O
public subnet: X
```


# Cloud DB for MS-SQL
Principal DB에서 로그를 가져와 Mirror DB에서 재구축, 따라서 실시간성은 없다.
```
디스크 시작용량: 100GB
디스크 증가용량: 10GB
디스크 최대용량: 2TB
백업기간: 30일
포트: 1433
슬레이브: 5대
private subnet: O
public subnet: O
```

# Cloud DB for MongoDB
NoSQL의 대표적인 MongoDB를 간편하게 사용할 수 있는 서비스

Document DB
데이터를 저장할 때 XML로 저장됨.
기존 정의된 변수를 사용하지 않기 때문에
사용자가 자유롭게 항목을 바꿀 수 있는 포맷의 데이터를 저장할 때 편함

```
디스크 시작용량: 10GB
디스크 증가용량: 10GB
디스크 최대용량: 2TB
백업기간: 30일
포트: 27000~
슬레이브: 7대
private subnet: - 둘다 필요 
public subnet: -
```

# Cloud DB for PostgreSQL
extension이라는 특이한 기능이 존재

```
디스크 시작용량: 10GB
디스크 증가용량: 10GB
디스크 최대용량: 6TB
백업기간: 30일
포트: 5432
슬레이브: 5대
private subnet: O
public subnet: O
```

# AI & Application

## Geo Location
사용자 IP를 통해 위치 정보를 제공하는 서비스
IP 주소에 따른 지역 정보 DB를 검색해 좌표 정보 전달

## SENS(Simple & Easy Notification Service)

## Outbound Mailer
대량 메일 발송을 위한 메일 발송 상품

## nShortURL
길고 복잡한 URL을 간단하고 짧게 변경하는 API

## CLOVA Speech Recognition(CSR)
음성(파일, 실시간 음성)을 텍스트로 변환해주는 ai

## CLOVA Summary
문장을 넣으면 내용을 요약해주는 ai

## CLOVA SENTIMENT
문장이 긍정인지 부정인지 판별하는 ai

## CLOVA ChatBot
CS나 주문시스템과 같은 고객 대응을 로봇으로 대체하는 상품, 생성형 ai가 아니라 정해진 답변만 할 수 있다. 

## CLOVA Dubbing
컨텐츠에 나레이션을 추가하는 ai

## CLOVA OCR(Optical Character Reader)
문서를 인식하고 사용자가 지정한 영역의 텍스트와 데이터를 정확하게 추출하는 ai

## AiTEMS
머신러닝(ML) 전문지식이 없어도 쉽고 빠르게 사용자의 취향에 맞는 추천 서비스 구현

## Papago Translation 
인공신경망 기반 기계 번역 API.  
텍스트 번역, 문서 번역, 웹사이트 번역, 이미지 번역을 지원한다.

## Cloud Insight
통합 관리 모니터링 서비스.  
애플리케이션 성능/운영 지표를 하나의 페이지로 통합하여 확인할 수 있다.

## Sub Account
서브 계정 별 역할 부여를 통한 리소스 관리(무료)
다수의 사용자가 동일한 자원을 이용하고 관리할 수 있도록 서브 계정을 제공하는 서비스(RBAC)
혼자 사용하는 것이 아니라면 무조건 사용해야 한다.

## Cloud Activity Tracer
다양한 계정 활동 로그를 수집하는 기능, 계정별 액션 로그와 비 계정 활동에 대한 로깅 기능 제공.  Sub Account를 반드시 사용해야 누가 어떤 작업을 했는지 tracing하여 알 수 있다.

## Web service Monitoring System(WMS)
고객의 웹페이지 품질 측정 도구.  
웹서비스 URL을 입력하여 실시간으로 테스트를 진행할 수 있고, 스케줄을 등록하여 모니터링 수행도 가능하다.
또 경보 설정을 통해 오류가 감지되면 알람 발송하는 기능도 제공한다.

## Cloud Advisor(VPC only)
사용자에게 다양한 카테고리별 점검 리포트를 제공해주는 서비스. 항목별 점검 결과 리포트 제공/점검 항목 선택/ 주 단위 메일 발송 기능 제공

## Organization

## MRTG

## Cloud Log Analytics
시스템 로그 수집 분석 플랫폼

## Cloud Hadoop
빅데이터 분석 도구
자동 하둡 클러스터를 보다 쉽고 편리하게 생성 및 관리
클라우드에 구축함으로써 간단하게 사용할 수 있다.

## Cloud Data Box
네이버의 빅데이터와 이를 분석할 수 있는 플랫폼 제공
효과적으로 또 안전하게 분석할 수 있다.
단, 제공된 데이터는 외부 반출 불가하고 분석된 결과는 심사 후 반출이 가능하다.



# Hypervisor
하드웨어와 VM 사이에 VM과 하드웨어를 관리하기 위한 가상화 관리 소프트웨어
- native type (TYPE 1:bare metal): 하드웨어 위에 Hypervisor 가 바로 설치되고 여러개의 OS를 사용한다.
- hosted (TYPE 2): 이미 설치되어 있는 OS 위에 가상화 환경을 얹는 형태

# 전가상화와 반가상화
- 전가상화: 하드웨어 전체를 가상화
하드웨어 위에 하이퍼바이저가 올라가기 때문에 모든 명령이 하이퍼바이저를 거친다. 모든 리소스를 관리하는 만큼 성능적으로 오버헤드가 발생
- 반가상화: 전가상황의 성능 저하를 해결하고자 모든 명령을 DOM0를 통해 요청하는 방식이 아닌 하이퍼콜을 통해 직접 요청한다. 하이퍼콜은 VM의 OS가 지원해주어야 함

# 서버 타입 Generation2
다음 중 standard server type이 아닌 것은?
비율을 외워라 
standard = 1:4
High Memory = 1:8
High CPU = 1:2

# Bare Metal Server
하이퍼바이저 없이 하드웨어의 자원을 그대로 사용.  
클라우드에서 제공하는 '물리' 서버이다. 기본적인 관리는 IPMI를 통해 관리

- 서버 사양 및 지원 OS
Bare Metal Server에 한해 RAID 5, 1+0를 제공

# GPU Server
병렬 처리에 최적화된 GPU 서버의 고성능 컴퓨팅 파워를 제공.
NVIDIA GRID 기술이 아닌 Pass Through를 적용하여 제공한다.
V100은 서버 당 최대 4장의 GPU를, T4는 서버당 최대 2장의 GPU를 제공

# NAS
- 다수의 VM이 공유 가능한 네트워크 볼륨 디바이스  
- 용량은 500~10,000G으로 생성 가능, 100G 단위로 용량 증설 가능

## NAS 특징
- NFS/CIFS 프로토콜 제공, 이기종 OS간 공유 사용을 위해서는 Samba 등의 추가 지원 필요
- Live resizing 기능, 마운트 되어 있어도 가능
- 스냅샷 기능 제공

## 접근 제어
- NFS의 경우 IP로 접근 제어
- CIFS의 경우 ID와 패스워드로 접근 제어

## NAS
- 스냅샷의 보관 기간은 7일

