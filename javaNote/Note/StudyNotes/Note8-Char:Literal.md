# 데이터 저장방법2 (character set;charset = 문자를 2진수로 변환하는 규칙)

## 1. ASCII(American Standard Code for Information Interchange)
- 영어만 정의, 7비트, 33개의 인쇄 불가능한 제어문자와 95개의 인쇄가능 문자로 구성
- 인쇄가능한 문자(95자): 알파벳 대/소문자, 숫자, 특수문자 등
- 인쇄불가능한 문자(33자): 제어문자, 공백문자
```
A = 100 0001[0x41]
B = 100 0010[0x42]
C = 100 0011[0x43]
a = 110 0001[0x61]
b = 110 0010[0x62]
c = 110 0011[0x63]
! = 010 0001[0x21]
? = 010 1111[0x3F]
# = 010 0011[0x23]
& = 010 0110[0x26]
+ = 010 1011[0x2B]
스페이스 = 010 0000[0x20]

LF(Line Feed) = [0x0A]
CR(Carriage Return) = [0x0D]
```

### 1.1 문자를 저장하는 원리 
```
                               CR LF
AB12 (엔터)  -----> 41 42 31 32 0D 0A 61 62 0D 0A (각 1byte, 총 10byte)
ab (엔터)
```
*키보드에서 Enter 키를 입력하면, 줄바꿈 코드 값이 삽입된다.
*Window: CRLF(2byte), Unix/Linux: LF(1byte)

## 2. ISO-8859-1(=ISO-latin-1)
- ASCII + 유럽 문자, 8bit 
```
A -> 0100 0001[0x41]
```

## 3. EUC-KR(16bit)
- 한글 문자에 대한 코드를 정의, 2byte, 국제 표준
- ISO-8859-1 + 한글 문자
- 총 2350자의 한글 + 한자(한중일 공통) + 한자(한국 전용) + ...
```
A -> 0x41(1byte)
가 -> 0xB0A1(2byte)
각 -> 0xB0A2(2byte)
똘 -> 0xB6CA(2byte)
똠 -> X 정의되어 있지 않다.
똡 -> X
똣 -> X
똥 -> 0xB6CB
  
가각똠방각하AB -> B0A1 B0A2 B6C7 A4B1 B9E6 B0A2 C7CF 41 42 (16byte)
```
## 4. 조합형
- 초성(5) + 중성(5) + 종성(5)
- 각 성마다 32자
- 모든 한글 표현 가능, 국제표준이 아니다. 
```
      초성    중성    종성
00000
00001 채움           채움
00010 ㄱ     채움     ㄱ
00011 ㄲ      ㅏ      ㄲ
00100 ㄴ      ㅐ      ㄳ
00101 ㄷ      ㅑ      ㄴ
00110 ㄸ      ㅒ      ㄵ
00111 ㄹ      ㅓ      ㄶ
...

4개씩 잘라서 16진법 변환 
ㅏ 깨 단 -> 8461 8B81 9465
ㅏ -> 1 00001 00011 00001 (16bit)
    0x 8    4    6    1
깨 -> 1 00011 00100 00001 (16bit)
    0x 8    B    8    1
단 -> 1 00101 00011 00101 (16bit)
    0x 9    4    6    5
```

## 5. MS949(CP949)
- EUC-KR + 나머지 한글 = 11172자 지원, 16비트
- 국제표준이 아니다.
```
가 -> 0xB0A1    |
각 -> 0xB0A2    | -> 기존 EUC-KR
똘 -> 0xB6CA    |
똠 -> 0x8C63  |
똡 -> 0x      | -> MS949에서 추가
똣 -> 0x      |
```

## 6. Unicode(2byte ~ 4byte)
- 영어도 2byte 사용 => 메모리 낭비
- 새로 정의했기 때문에 한글은 순서대로 코드값이 부여됨
- Java는 문자를 다룰 때 Unicode(UCS2)를 사용한다.
- UCS2 = UTF-16 BE
- 메모리 낭비를 줄이기 위해 UTF-8 등장
```
A -> 0x0041 
B -> 0x0042
C -> 0x0043
...
가 -> 0xAC00 
각 -> 0xAC01
간 -> 0xAC04 
...
똘 -> 0xB618
똠 -> 0xB620
똡 -> 0xB621
똣 -> 0xB623
똥 -> 0xB625
```

### LE vs BE
가: 00AC AC00
A: 4100  0041

## 7. UTF-8(Universal coded character set Transformation Format-8bit)
com.eomcs/lang/ex03/Exam0
Unicode(UTF16BE) --> UTF-8
```
(1byte)
000000 ~ 00007F: 기존 ASCII 코드와 동일해졌다
0000 0000 0xxx xxxx -> 0xxx xxxx

(2byte)
000080 ~ 0007FF:  
0000 0000 0000 0000 0000 0yyy xxxx xxxx -> 110yyyxx 10xxxxxx

(3byte: 한글)
000800 ~ 00FFFF: 
yyyy yyyy xxxx xxxx -> 1110yyyy 10yyyyxx 10xxxxxx

(4byte)
100000 ~ 10FFFF: 11110zzz 10zzyyyy 10yyyyxx 10xxxxxx 

3byte 예시)
11101010 10110000 10000000
  E A       B 0      8 0   --> EA B0 80
```

# 데이터 저장방법3 (Boolean)
논리값 ---> true ---> 1(4byte/1byte)
          false --> 0(4byte/1byte)

# 자바 기초 문법
- 리터럴(literal)
- 변수(variable)
- 연산자(operator)
- 조건문(condition)/분기문
- 반복문(loop)

## 1. 리터럴: 데이터를 표기한 것
- 숫자 
    - 정수 : 12
    - 부동소수점(실수) : 3.14
- 문자: 'a', '가'
- 문자열: "A", "ABC"
- 논리: true, false

### 1.1 정수 literal(2가지)
- 32bit(4byte, int): 100
- 64bit(8byte, long): 100L or 100l

### 1.2 부동소수점 literal(2가지)
- 32bit(4byte, float): 3.14f or 3.14F
- 64bit(8byte, double): 3.14(D)

## 2.1 문자 literal 
- 'A' -> 0x0041
- '가' -> 0xAC00  
*single quatation 연산자: 주어진 문자의 unicode(2byte) 값을 리턴한다.




