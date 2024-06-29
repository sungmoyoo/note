# Nodejs
Node.js는 V8으로 빌드된 이벤트 기반 자바스크립트 런타임이다. 웹 서버와 같이 확장성 있는 네트워크 프로그램 제작을 위해 고안되었다. 즉 CLI에서 자바스크립트를 동작할 수 있도록 하는 환경

# NVM
nvm install --lts

# NPM(Node Package Manager)
package.json이라는 스크립트 파일을 읽어서 실행(준비)한다. package.json에는 Nodejs로 만든 어플리케이션 정보와 어플리케이션이 사용하는 의존 모듈 정보가 담겨있다. build.gradle을 읽어서 의존 모듈을 준비하는 gradle과 유사하다. 

package.json은 npm init 명령어로 설치할 수 있으며, npm install 명령어로 필요한 모듈을 설치하면 자동으로 package.json의 dependencies에 해당 모듈과 의존하는 모듈도 기입된다.

모듈을 설치하면 node_modules 폴더가 생기는데 .gitignore에 추가해서 깃 리포지토리에서 제외한다. 왜 why? package.json에 이미 의존 모듈에 대한 정보가 있기 때문에 실행 시 자동으로 설치할 수 있기 때문이다.

package-lock.json에는 기존 라이브러리 버전을 고정하는 스크립트 파일이다. 
자바스크립트 라이브러리는 버전 업이 될 때 메서드가 사라지는 경우가 많기 때문에 package-lock.json도 따로 존재하는 것이다.

버전을 지정하는 방법은 따로 찾아볼 것. ex) caret, tilde, greater than...

npm install 명령어를 주면 package.json에 있는 의존 라이브러리를 모두 설치한다.

# CORS(Cross-Origin Resource Sharing)
동일 출처 정책(Same-Origin Policy)을 우회하는 것

동일 출처 정책이란 HTML을 받아온 서버로만 AJAX(HTTP) 요청을 허락하는 규칙이다.  
Web Server A와 Web Server B가 있다고 가정하자, A에서 a.html을 get 요청을 하고 a.html에서 AJAX 요청을 할 때 A는 가능하지만 B는 보안상 제한을 걸어버린다. 이러한 제한이 동일 출처 정책이다. 

CORS는 Cross-Origin 요청을 서버의 동의를 구하고 요청하는 메커니즘이다.  
응답 헤더에 승인 헤더를 포함시킨다면 요청에 대한 리턴을 해주고, 승인 헤더가 없다면 리턴하지 않는 방식이다.
