# JavaScript
## function(함수)
모든 함수는 arguments라는 배열을 가지고 있고 입력받은 값을 배열에 저장한다. 
파라미터로 숫자, 문자열, 객체, 함수 등 자유롭게 모든 타입을 넣을 수 있다.  

함수 안에 선언된 변수는 함수에서만 사용하는 변수이다. 따라서 전역변수가 존재해도 변수 선언을 해도 함수에 종속되기 때문에 전역변수와 연관이 없다. 
즉, 함수안에 선언된 var 변수는 함수변수, 함수 밖에 선언되면 전역변수, let은 상관없이 블록변수이다.  

## 함수와 레퍼런스
함수는 자바스크립트에서 객체로 분류된다, 따라서 자바스크립트의 함수도 주소가 있다. 함수의 주소는 함수 이름을 가진 변수에 저장된다.  
```javascript
function f1() { 
	console.log("안녕!");
}

var f2;
f2 = f1; // f1에 저장된 함수 주소를 f2에 복사한다.

// 복사한 함수 주소를 가지고 원래의 함수처럼 사용할 수 있다.
f2();
window.f2();
```

### CallBack
함수 안에 함수를 넣어 호출할 수 있다. 이를 callback 함수, 줄여서 cb로 표현한다.
```javascript
function play(a, b, cb) {
  return cb(a, b);
}

function plus(a, b) {
  return a + b;
}

function minus(a, b) {
  return a - b;
}

console.log(play(200,300,minus))
```

### 익명 함수
함수 이름없이 함수를 정의할 수 있다. 이름없는 함수를 정의한 후 사용하려면 그 함수의 주소를 어딘가에 저장해야 한다.  



### 호이스팅 기법
만약 함수의 정의나 변수를 선언하기 전에 사용하려고 하면 에러가 발생하는 것이 일반적인 자바 규칙이다. 하지만 자바스크립트에서는 이러한 제약을 없애기 위해 script 태그를 실행할 때 함수 정의가 있으면 제일 먼저 실행한다. 이를 호이스팅 기법이라고 한다.  

호이스팅 기법의 단위는 스크립트 태그로서 스크립트 태그가 다르면 호이스팅 기법이 적용되지 않는다는 점을 유의해야 한다.  
```javascript
function play(a, b, cb) {
  return cb(a, b);
}

function plus(a, b) {
  return a + b;
}

function minus(a, b) {
  return a - b;
}

console.log(play(200,300,minus))
```

호이스팅 기법은 함수 정의와 변수 선언만 먼저 실행한다. 따라서 변수초기화 문법 같은 경우 선언과 할당문을 분리하여 변수 선언 이우 할당문이 실행될 때 까지 변수는 값이 비어있는 상태이다. 따라서 익명함수를 통해 변수에 함수를 할당해 사용하려고 하면 변수 선언문과 함수 선언문만 먼저 실행되고 변수에 함수가 할당되지 않은 상태가 되므로 사용할 수 없다.  

```javascript
// 사용 불가능
console.log(f1(100, 200)) 

let f1 = function (a,b) {
  return a + b;
};

// 호이스팅 기법 적용된 코드
let f1;

console.log(f1(a,b)); // f1은 비어있는 변수이다.

f1 = function f1(a,b) {
  return a + b;
};
```

## 클로저와 바깥 함수의 로컬 변수 (함수 안에 함수)
함수 안에 정의된 함수를 클로저(closure)라고 부른다. 자바에서 중첩클래스와 같은 개념이다.  
```javascript
function a() {
  let f1 = function() { // closure
    console.log("홍길동");
  };
  return f1
}

let b = a();
b();
```

closure에서 바깥 함수의 로컬 변수를 사용할 때 바깥 함수의 호출이 끝나면 해당 로컬 변수가 제거되기 때문에 클로저는 존재하지 않는 변수를 사용하는 상황이 발생한다. 이런 경우를 방지하고자, 클로저에서 사용하는 바깥함수의 로컬 변수는 클로저의 별도 메모리에 복제된다.  

+자바도 마찬가지로 컴파일러가 클래스를 변환시켜 자동으로 로컬변수를 파라미터로 받는 생성자를 만들고 로컬 변수를 메모리에 저장시켜 사용한다.  
```javascript
function a() {
  let name = "임꺽정";

  let f1 = function() {
    console.log(name);
  };
  return f1
}

let b = a();
b();
```

함수 안에 클로저가 2개 이상 존재하고 그 클로저들이 바깥 함수의 로컬 변수를 사용한다면 closure 전용 메모리에 복제된 로컬변수를 클로저들이 공유한다.  
```javascript
function a() {
  let name = "임꺽정";

  let setter = function(n) {
    name = n;
  };

  let getter = function() {
    return name;
  }

  return [setter, getter];
}

let arr = a();
console.log(arr);

console.log(arr[1]()) // 1번 클로저 호출 (함수이기 때문에 '()' 붙임)
console.log(arr[2]()) // 2번 클로저 호출
```
단 동일한 함수를 새로 만든다 해도 같은 메모리를 공유하지 않는다. 예를 들면 arr1 과 arr2가 a(); 함수를 할당받았을 때 arr1과 arr2 의 name 변수는 서로 공유하지 않는 엄연한 다른 변수이다.  

### 분해할당 문법(JavaScript Distructuring)
배열에서 값을 꺼내 바로 변수에 저장하는 문법이다.  
```javascript
function a() {
    let name = "임꺽정";

    let setter = function(n) {
      name = n;
    };

    let getter = function() {
      return name;
    };

    return [setter, getter];
  }

  let [setName, getName] = a();
  console.log(arr);

  console.log(setName()) 
  console.log(getName())
```

## Object(객체)
자바스크립트의 객체는 원형 객체를 만들고 값을 넣어주는 방식을 사용한다. 이를 프로토타이핑 방식이라 부른다.  
```javascript
let s1 = new Object();
s1.name = "홍길동";
s2.age = 20;

let s2 = new Object();
s2.name = "임꺽정";
s2.age = 30;
  
console.log(s1);
console.log(s2);
```

자바스크립트 객체에는 함수도 추가할 수 있다.  
함수를 추가하고 객체의 값을 넣고 싶을 때는 반드시 this를 붙여주어야 한다.  
```javascript
let s1 = new Object();
  s1.sname = "홍길동";
  s1.age = 20;
  s1.print = function() {
    console.log(this.sname, this.age);
  };

  let s2 = new Object();
  s2.sname = "임꺽정";
  s2.age = 30;
  s2.print = function() {
    console.log(this.sname, this.age);
  };

  s1.print();
  s2.print();
```

객체를 생성하는 함수 "팩토리 메서드"와 객체 리터럴을 사용하는 게 더 일반적인 방식이다.  
```javascript
function createStudent(name, age) {
  return {
    sname: name,
    age: age,
    print() { // 단축 문법
      console.log(this.sname, this.age);
    }
  };
}

let s1 = createStudent("홍길동", 20)
let s2 = createStudent("임꺽정", 30)

s1.print();
s2.print();
```

함수 안에서 객체를 만들지 않고 new를 통해 원형 객체를 만들고 함수에 값을 할당하는 방법도 있다.  
```javascript
function createStudent(name, age) {
    this.sname = name;
    this.age = age;

    this.print = function() { // 단축 문법
      console.log(this.sname, this.age);
    };
  }

let s1 = new createStudent("홍길동", 20)
let s2 = new createStudent("임꺽정", 30)

s1.print();
s2.print();
```

이 방법은 new 할 때마다 print라는 익명함수가 계속 생성되므로 prototype에 함수를 저장해놓는 방식을 사용한다.  

**일반객체 vs 함수객체**  
일반객체나 함수객체나 같은 객체이다. 단 함수 객체는 함수 정의 코드를 value값으로 가진다.  

이때 프로토타입에 함수를 담아두면(객체이름.prototype.함수이름) 기본 객체에서 함수를 찾아보고 없을 때 해당 객체를 만든 객체의 프로토타입에서 찾아 함수를 실행한다. 

프로토타입에 저장된 함수, 또는 값들은 해당 객체로 만든 모든 인스턴스에서 공유되므로 함수를 여러번 생성하지 않고도 사용할 수 있는 장점이 있다.  
단 new 로 생성하지 않고 바로 초기화시킨 객체는 prototype이 존재하는 객체를 참조하지 않는다. 반드시 new로 생성하고 따로 초기화해야 prototype에 있는 인스턴스를 사용할 수 있다.

이처럼 new를 통해 프로토타입을 공유하는 특별한 함수를 `Constructor 함수`라고 한다.

생성자 역할을 하는 함수는 구분을 하기 위해 함수명 시작을 대문자로 하고 함수명으로 저장한다. 
```javascript
function Student(name, age) {
  this.sname = name;
  this.age = age;
}

Student.prototype.print = function() { // 단축 문법
  console.log(this.sname, this.age);
};

let s1 = new Student("홍길동", 20)
let s2 = new Student("임꺽정", 30)

s1.print();
s2.print();
```

### 함수의 주소 활용
함수 주소를 가지고 호출할 때는 
```javascript
// 언제든지 호출 가능
let x = function(name) {
  console.log(name);
};

x("홍길동");
-----------------------------
// 함수를 정의한 후 딱 한번만 호출하고 싶을 경우 사용하는 방법, 자바의 익명클래스와 같다.
(function(name) {
  console.log(name);
})("홍길동");
```











