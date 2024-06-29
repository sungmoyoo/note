# JavaScript RichTextEditor에서 콘텐트에 그림을 삽입하는 방법
1. 콘텐트 + 그림
만약 텍스트와 함께 이미지를 삽입한다면 `HTML tag`와 `<img src="base64 인코딩">`이 사용된다. 

이 방법의 장점은 DB 컬럼을 다루기 편하다는 것이지만 용량이 커 DB 데이터 조회가 느리고 이미지에 대해 별도로 썸네일 이미지 생성하는 것이 어렵다는 단점이 있다. 

2. 콘텐트 + 이미지 링크
별도의 저장 공간인 Object Storage에 파일이 저장되고 `img src=""` 안에 이미지 링크가 들어간다.

Object Storage 방식은 다루기가 번거롭지만 DB 데이터 조회가 빠르고 썸네일 생성이 가능하다. 


## SummerNote 사용 - 이미지 분리 저장
Callback
- onInit
- onImageUpload - base64로 바꾸고 싶지 않다면 알려줘라 알려준대로 처리

..........................