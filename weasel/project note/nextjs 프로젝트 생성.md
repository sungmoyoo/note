# NextJS 프로젝트 생성

![img]{}

```
npx create-next-app@latest
```

위 코드를 실행하기에 앞서 위 명령은 Node.js를 시스템 내부적으로 사용하므로 Node.js가 설치되어 있지 않다면 Node.js를 설치해주어야 한다.

### 1. Node.js 설치

아래의 링크에서 LTS 버전을 직접 다운/설치를 하거나, 패키지 매니저(Homebrew, chocolatey)를 통해 설치한다.

### 2. nextjs 설치

기본 터미널 또는 명령 프롬프트에서 프로젝트를 폴더를 생성할 경로에서 아까 확인한 설치 명령을 입력해준다.

위 코드를 실행하면 생성 옵션을 물어보는데, 상황에 맞게 사용할 옵션을 선택한다.

```
✔ What is your project named? … weasel-frontend
✔ Would you like to use TypeScript? … No / Yes // Javascript + 정적 타입
✔ Would you like to use ESLint? … No / Yes // 정적 코드 분석 툴
✔ Would you like to use Tailwind CSS? … No / Yes // utility-first CSS 프레임워크
✔ Would you like to use `src/` directory? … No / Yes // jsconfig.json 파일을 어디에 배치할 지 묻는 내용
✔ Would you like to use App Router? (recommended) … No / Yes // No 선택 시 페이지 라우터 방식으로 처리한다.
✔ Would you like to customize the default import alias (@/*)? … No / Yes // 사용자 정의한 import alias를 사용할지 묻는 내용
```

### 3. 프로젝트 실행

NextJS 프로젝트가 성공적으로 실행되었다면 다음과 같이 프로젝트가 생성이 된 것을 확인할 수 있다.

프로젝트 생성 시에는 node_modules가 자동으로 install 되어 있으므로 따로 npm install할 필요가 없다.

터미널/명령 프롬프트에서 프로젝트 경로로 이동한 후 `npm run dev`를 입력하여 개발 서버를 바로 실행할 수 있다.  
실제 배포 서버를 실행 시키고 싶다면 빌드 후 `npm run start` 명령으로 실행 시킬 수 있다.

[사진3]

터미널에 나와있는 주소로 접속했을 때 다음과 같이 NextJS에서 반겨준다면 성공적으로 프로젝트를 생성한 것이다.

### 결론

nextJS를 처음 써본다는 점과 아직 앱 라우터/페이지 라우터의 차이점 및 개념을 완전히 이해하지 못한 상태여서 어느 방식을 선택해야 할 것인지가 고민이었다. 일반적으로는 페이지 라우터 방식을 사용하여 프로젝트를 완성하고 앱 라우터 방식으로 migration하는 것이 학습 목적으로는 최고지만 시간이 발목을 잡을 것 같아 바로 앱 라우터 방식을 사용하여 프로젝트를 진행하고 페이지 라우터 방식과 비교해보려고 한다.
