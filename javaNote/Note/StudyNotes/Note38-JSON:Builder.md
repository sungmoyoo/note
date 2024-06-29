# JSON(JavaScript Object Notation)
CSV는 간결하다, 하지만 각각의 데이터가 어떤 데이터인지 설명하지 않으면 모른다.

메타데이터, 태그 방식은 데이터/계층 구조 방식을 이해하기가 쉽다. 다만 메타데이터가 데이터보다 많아질 수 있다. 

>JSON은 위 두가지 방식의 단점을 보완하여 나온 형식이다. 

- 자바스크립트에서 객체 리터럴을 표기 형식을 모방해 만들었다. '모방'이라 규칙이 완전 동일하지 않다. 
- property 명은 반드시 문자열로 표현, 문자열은 반드시 " "로 표현

**특징**
1. XML 처럼 구조적인 데이터를 표현하기 쉽다.
2. tag처럼 데이터를 구분하는 메타데이터가 있다. 
3. text형식으로 프로그래밍 언어 간에 교환, 즉 주고 받기 쉽다.

> 따라서 Open API에서 많이 사용한다.

# Gson
---
직접 json 형식을 작성하지 않고 라이브러리를 통해 편하게 작성할 수 있다.

## 설정
>json.org -> Groovy DSL 코드 복사 -> build.gradle dependencies에 추가 -> gradle eclipse 후 refresh

# 객체 형식
객체는 프로퍼티명과 값을 중괄호로 묶어 표현한다.  
프로퍼티명은 반드시 문자열로 표현한다.  
```
{"프로퍼티명" : 값, "프로퍼티명" : 값, ...}
```
## 객체 -> 문자열
```
public class Exam {
  public static void main(String[] args) {
    // 1) 객체 준비
    Member m = new Member();
    m.setNo(100);
    m.setName("홍길동");
    m.setEmail("hong@test.com");
    m.setPassword("1111");
    m.setPhoto("hong.gif");
    m.setTel("010-2222-1111");
    m.setRegisteredDate(new Date(System.currentTimeMillis()));
    m.setSchool(new School("학사","xx대학교"));

    // 2) JSON 처리 객체 준비
    Gson gson = new Gson();

    // 3) 객체의 값을 JSON 문자열로 얻기
    String jsonStr = gson.toJson(m);

    System.out.println(jsonStr);
  }
}
```

>gson과 jackson은 문자열 저장방식이 다르다. 
- gson: 프로퍼티명 
- jackson: getter/setter명

따라서 JSON 라이브러리에 따라 원하는 형식으로 출력해야 할 상황도 발생한다. 

### GoF의 Builder 패턴
JSON 형식을 바꾸어 출력하고자 할 때 객체 생성 과정이 복잡해지는 문제가 발생하는데 이를 처리하기 위한 설계 기법이 Builder 패턴이다.

복합 객체의 생성 과정을 캡슐화하는 설계 기법
```
public class Exam {
  public static void main(String[] args) {

    // 1) 객체 준비
    Member m = new Member();
    m.setNo(100);
    m.setName("홍길동");
    m.setEmail("hong@test.com");
    m.setPassword("1111");
    m.setPhoto("hong.gif");
    m.setTel("010-2222-1111");
    m.setRegisteredDate(new Date(System.currentTimeMillis()));
    m.setSchool(new School("학사", "xx대학교"));

    // 2) JSON 처리 객체 준비
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    // 3) 객체의 값을 JSON 문자열로 얻기
    String jsonStr = gson.toJson(m);

    System.out.println(jsonStr);
  }
}
```
만약 날짜 플러그인.., xx플러그인이 포함된 객체를 만들어야 한다면?
=> 여러 단계와 다양한 절차를 거쳐 객체를 만들 수 있는 Builder 패턴을 사용하는 것이 좋다.

>여러 객체를 조립해서 만들어야 하는 경우, 복잡한 객체 생성 과정을 감추기 위해 외부객체(빌더)에 의뢰해서 객체를 얻는 기법

### Gson와 JsonSerializerAdapter
일반 json 형식이나 GsonBuilder에서 제공하는 메서드를 통해 변경할 수 있는 형식의 데이터를 출력한다면 위의 코드처럼 객체를 생성하면 된다. 
>하지만, 사용자가 원하는 형태로 출력 형식을 바꾸고자 한다면 registerTypeAdapter를 사용해 Gson객체에 형식을 바꿔주는 어댑터 객체를 장착해주어야 한다.

```
public class Exam0112 {
  public static void main(String[] args) {

    // 1) 객체 준비
    Member m = new Member();
    m.setNo(100);
    m.setName("홍길동");
    m.setEmail("hong@test.com");
    m.setPassword("1111");
    m.setPhoto("hong.gif");
    m.setTel("010-2222-1111");
    m.setRegisteredDate(new Date(System.currentTimeMillis()));
    m.setSchool(new School("학사", "비트대학교"));

    // 2) JSON 처리 객체 준비
    // Date 타입의 값을 내보내고 가져올 때 사용할 변환 도구(어댑터)를 준비
    class GsonDateFormatAdapter implements JsonSerializer<Date> {

      private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

      @Override
      public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        // 객체를 JSON 문자열로 변환할 때 호출된다.
        // 그 중 Date 타입의 프로퍼티 값을 JSON 문자열로 바꿀 때 호출된다.
        System.out.println(src.getTime());
        return new JsonPrimitive(dateFormat.format(src));
      }
    }

    // School 타입 값 변환하는 어댑터 준비
    class GsonSchoolFormatAdapter implements JsonSerializer<School> {

      private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

      @Override
      public JsonElement serialize(School src, Type typeOfSrc, JsonSerializationContext context) {
        // 객체를 JSON 문자열로 변환할 때 호출된다.
        // 그 중 School 타입의 프로퍼티 값을 JSON 문자열로 바꿀 때 호출된다.
        return new JsonPrimitive(String.format("%s(%s)", src.level, src.name));
      }
    }

    // Gson 객체를 만들어 줄 도우미 객체
    GsonBuilder builder = new GsonBuilder();

    // Date 타입의 프로퍼티 값을 JSON 형식의 문자열로 바꿔줄 변환기를 등록한다.
    builder.registerTypeAdapter(
        Date.class, // 원래 데이터의 타입
        new GsonDateFormatAdapter() // Date 형식의 데이터를 JSON 문자열로 바꿔줄 변환기
        );

    builder.registerTypeAdapter(
        School.class, // 원래 데이터의 타입
        new GsonSchoolFormatAdapter() // Date 형식의 데이터를 JSON 문자열로 바꿔줄 변환기
        );
    // gson 객체 생성
    Gson gson = builder.create();

    // 3) 객체의 값을 JSON 문자열로 얻기
    String jsonStr = gson.toJson(m);

    System.out.println(jsonStr);
  }
}
```

리팩토링(익명클래스 사용, 체인 방식 사용)
```
public class Exam {
  public static void main(String[] args) {

    Member m = new Member();
    m.setNo(100);
    m.setName("홍길동");
    m.setEmail("hong@test.com");
    m.setPassword("1111");
    m.setPhoto("hong.gif");
    m.setTel("010-2222-1111");
    m.setRegisteredDate(new Date(System.currentTimeMillis()));
    m.setSchool(new School("학사", "비트대학교"));

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
          @Override
          public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(dateFormat.format(src));
          }
        })
        .registerTypeAdapter(School.class, new JsonSerializer<School>() {
          @Override
          public JsonElement serialize(School src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(String.format("%s(%s)", src.level, src.name));
          }
        })
        .create();
        // 위 코드는
        // 빌더().어댑터().어댑터().객체생성()의 구조이다.
        // new GsonBuilder().registerTypeAdapter().registerTypeAdapter().create(); 

    String jsonStr = gson.toJson(m);

    System.out.println(jsonStr);
  }
}
```

## 문자열 -> 객체
객체 -> 문자열과는 다르게 fromJson으로 객체 변환 시 클래스 정보도 필요하다.
```
public class Exam {
  public static void main(String[] args) {

    // 1) JSON 문자열 준비
    String jsonStr = "{\"no\":100,\"name\":\"홍길동\",\"email\":\"hong@test.com\",\"password\":\"1111\",\"photo\":\"hong.gif\",\"tel\":\"010-2222-1111\",\"registeredDate\":\"2023-07-03\"}";

    // 2) JSON 처리 객체 준비
    //    Gson gson = new Gson(); // 실행 오류! yyyy-MM-dd 형식으로 된 날짜를 인식하지 못해서 오류 발생.
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    // 3) JSON 문자열을 가지고 객체 만들기
    Member m = gson.fromJson(jsonStr, Member.class);

    System.out.println(m);
  }
}
```

JSON 일반 형식이 아닌 문자열을 변환할 때는 어댑터를 통해 해석하여 객체를 생성한다.
```
public class Exam {
  public static void main(String[] args) {

    // 1) JSON 문자열 준비
    String jsonStr = "{\"no\":100,\"name\":\"홍길동\",\"email\":\"hong@test.com\",\"password\":\"1111\",\"photo\":\"hong.gif\",\"tel\":\"010-2222-1111\",\"registeredDate\":\"2023-07-03\"}";

    // 2) JSON 처리 객체 준비
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
      @Override
      public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
          throws JsonParseException {
        return Date.valueOf(json.getAsString());
      }
    });
    Gson gson = builder.create();

    // 3) JSON 문자열을 가지고 객체 만들기
    Member m = gson.fromJson(jsonStr, Member.class);

    System.out.println(m);
  }
}
```

# 배열 형식
json 배열 형식은 대괄호로 묶여서 나타낸다.
```
[{ 객체 정보 },{ 객체 정보}, ...]
=>  [
      {"프로퍼티명":값,"프로퍼티명":값, ...},
      {"프로퍼티명":값,"프로퍼티명":값, ...},
      {"프로퍼티명":값,"프로퍼티명":값, ...},
      ...
    ]
```

# 컬렉션 형식
배열을 출력한 것과 같다.  
JSON은 배열과 컬렉션을 구분하지 않는다.
```
[{ 객체 정보 },{ 객체 정보 }, ...]
=> [
      {"프로퍼티명":값,"프로퍼티명":값, ...},
      {"프로퍼티명":값,"프로퍼티명":값, ...},
      {"프로퍼티명":값,"프로퍼티명":값, ...},
      ...
    ]
```

## 컬렉션 제네릭 처리
객체를 직렬화 시 타입이 소실되는 문제가 발생할 수 있다. 따라서 TypeToken을 통해 컬렉션 타입 정보를 fromJson에 전달하여 처리하도록 해야 한다.
```
public class Exam {
  public static void main(String[] args) {

    String jsonStr = "[{\"no\":101,"
        + "\"name\":\"홍길동\"},"
        + "{\"no\":102,"
        + "\"name\":\"임꺽정\"},"
        + "{\"no\":103,"
        + "\"name\":\"안창호\"}]";

    // 1) TypeToken 클래스의 서브 클래스를 만든다.
    class MyTypeToken extends TypeToken<Collection<Member>> {
      // 수퍼 클래스를 지정할 때 제네릭의 타입을 설정한다.
      // TypeToken 클래스에는 Type 인터페이스의 구현체를 만드는 메서드가 있기 때문에
      // 이 클래스의 서브 클래스를 만드는 것이다.
      // 타입 파라미터에 컬렉션 타입을 전달하는 목적 이외에는 다른 이유가 없다.
      // 그래서 서브 클래스에 뭔가를 추가할 필요가 없다.
    }

    // 2) TypeToken 객체 준비
    MyTypeToken typeToken = new MyTypeToken();

    // 3) TypeToken 객체를 통해 Type 구현체를 얻는다.
    Type collectionType = typeToken.getType();

    // 4) Type 객체에 저장된 정보를 바탕으로 JSON 문자열로부터 컬렉션 객체를 만든다.
    Collection<Member> list = new Gson().fromJson(jsonStr, collectionType);

    for (Member m : list) {
      System.out.println(m);
    }
  }
}
```

TypeToken.getParameterized(컬렉션 타입, 원소 타입) 메서드를 통해 제네릭 타입에 대한 TypeToken을 더 간단하게 생성할 수 있다.
```
public class Exam {
  public static void main(String[] args) {

    String jsonStr = "[{\"no\":101,\"name\":\"홍길동\"},{\"no\":102,\"name\":\"임꺽정\"},{\"no\":103,\"name\":\"안창호\"}]";

    // Exam0321과 다른 방법으로 Type 객체를 얻기
    //    Type collectionType = TypeToken.getParameterized(Collection.class, Member.class).getType();
    //    Collection<Member> list = new Gson().fromJson(jsonStr, collectionType);

    Collection<Member> list = new Gson().fromJson(jsonStr,
        TypeToken.getParameterized(Collection.class, Member.class).getType());

    for (Member m : list) {
      System.out.println(m);
    }
  }
}
```

# Jackson
---
Gson과 같은 json 라이브러리이다. 사용법은 거의 유사하다.

## 객체 -> 문자열
Json 처리 객체를 ObjectMapper로 생성하고 toJson() 대신 writeValueAsString()을 사용한다.
```
public class Exam {
  public static void main(String[] args) throws Exception {

    // 1) 객체 준비
    Member m = new Member();
    m.setNo(100);
    m.setName("홍길동");
    m.setEmail("hong@test.com");
    m.setPassword("1111");
    m.setPhoto("hong.gif");
    m.setTel("010-2222-1111");
    m.setRegisteredDate(new Date(System.currentTimeMillis()));
    m.setSchool(new School("학사", "비트대학교"));

    // 2) JSON 처리 객체 준비
    ObjectMapper mapper = new ObjectMapper();

    // 3) 객체의 값을 JSON 문자열로 얻기
    String jsonStr = mapper.writeValueAsString(m);

    System.out.println(jsonStr);
  }
}
```

## 문자열 -> 객체
fromJson() 대신 readValue()을 사용한다.
```
public class Exam0120 {
  public static void main(String[] args) throws Exception {

    // 1) JSON 문자열 준비
    String jsonStr = "{\"no\":100,\"name\":\"홍길동\",\"email\":\"hong@test.com\",\"password\":\"1111\",\"photo\":\"hong.gif\",\"tel\":\"010-2222-1111\",\"registeredDate\":1642991105179}";

    // 2) JSON 처리 객체 준비
    ObjectMapper mapper = new ObjectMapper();

    // 3) JSON 문자열을 가지고 객체 만들기
    Member m = mapper.readValue(jsonStr, Member.class);

    System.out.println(m);
  }
}
```

## 주의사항
>Gson 과 달리 JSON 프로퍼티에 해당하는 객체 프로퍼티가 없다면 예외가 발생한다.  
>@JsonIgnoreProperties 애노테이션을 사용하여 JSON 프로퍼티 중에서 무시할 항목을 지정해야 한다.

## 날짜 형식 변경
Jackson 라이브러리에서는 빌더를 사용하지 않고도 직접 ObjectMapper에 setDateFormat() 메서드를 사용하여 날짜 형식을 변경할 수 있다. 
```
public class Exam0210 {
  public static void main(String[] args) throws Exception {

    // 1) 객체 준비
    Member m1 = new Member();
    m1.setNo(101);
    m1.setName("홍길동");
    m1.setEmail("hong@test.com");
    m1.setRegisteredDate(new Date(System.currentTimeMillis()));

    ObjectMapper mapper = new ObjectMapper();
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

    String jsonStr = mapper.writeValueAsString(m1);

    System.out.println(jsonStr);
  }
}
```
