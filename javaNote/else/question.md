# 다른 객체를 여러개 포함하는 경우
```
public class Exam0622 {
  public static void main(String[] args) {

    String jsonStr = "[{\"position\":\"대리\",\"fax\":\"02-1111-2222\",\"no\":101,\"name\":\"홍길동\",\"email\":\"hong@test.com\",\"registeredDate\":\"9월 16, 2021\"},{\"major\":\"컴퓨터공학\",\"hourPay\":10000,\"no\":103,\"name\":\"안창호\",\"email\":\"ahn@test.com\",\"registeredDate\":\"9월 16, 2021\"}]";

    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Member.class, new JsonDeserializer<Member>() {
          @Override
          public Member deserialize(JsonElement json, Type typeOfT,
              JsonDeserializationContext context) throws JsonParseException {

            JsonObject jsonObject = json.getAsJsonObject(); // 

            if (jsonObject.get("position") != null) {
              return context.deserialize(jsonObject, Manager.class);
            } else {
              return context.deserialize(jsonObject, Teacher.class);
            }
          }
        })
        .create();

    Type collectionType = TypeToken.getParameterized(Collection.class, Member.class).getType();
    Collection<Member> list = gson.fromJson(jsonStr, collectionType);

    for (Member m : list) {
      System.out.println(m);
    }
  }
}
```

위의 코드에서 `json.getAsJsonObject()`가 호출되는 이유는 다른 타입의 객체를 생성하기 위해 필요한 정보를 JSON 데이터에서 추출하기 위함입니다.

이 코드에서 사용된 `MyJsonDeserializer`는 `JsonDeserializer<Member>`를 구현한 사용자 지정 어댑터입니다. 이 어댑터는 JSON 데이터를 읽고, 해당 데이터를 `Member` 클래스의 인스턴스로 역직렬화하는데 사용됩니다.

어댑터 내부에서 `json.getAsJsonObject()`를 사용하는 이유는 JSON 데이터가 어떤 타입의 객체로 변환되어야 하는지 결정하기 위해서입니다. JSON 데이터에는 `"position"`이라는 필드가 존재하면 `Manager` 클래스로, 존재하지 않으면 `Teacher` 클래스로 객체를 생성하도록 설정되어 있습니다. 이를 위해 JsonObject로 변환하여 `"position"` 필드의 존재 여부를 확인하고, 그에 따라 적절한 타입의 객체로 역직렬화를 수행합니다.

아래는 `MyJsonDeserializer`에서 `json.getAsJsonObject()`를 사용하는 부분입니다:

```java
JsonObject jsonObject = json.getAsJsonObject();

if (jsonObject.get("position") != null) {
  return context.deserialize(jsonObject, Manager.class);
} else {
  return context.deserialize(jsonObject, Teacher.class);
}
```

여기서 `"position"`이라는 필드가 존재하면 `Manager` 클래스로, 존재하지 않으면 `Teacher` 클래스로 객체를 생성하고 있습니다. 따라서 JsonObject를 이용하여 JSON 데이터의 내용을 확인하고 그에 따라 적절한 객체를 생성하는 것이 이 코드에서의 목적입니다.

복합 객체, 의존객체?
Gson,Jackson setDateFormat 차이
json.getAsJsonObject() 뭔지 모르겠다.