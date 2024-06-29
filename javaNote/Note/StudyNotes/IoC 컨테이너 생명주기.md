#IoC 컨테이너 생명주기
`DispatcherServlet`와 `ContextLoaderListener`는 각각 다른 시점에 IoC 컨테이너를 생성합니다. 아래는 각각의 시점에 대한 설명입니다:

1. **`ContextLoaderListener`의 IoC 컨테이너 생성 시점:**
   - `ContextLoaderListener`는 웹 애플리케이션이 초기화될 때 `ServletContext` 리스너로 등록되어 있는데, 이 때 IoC 컨테이너가 생성됩니다.
   - `ContextLoaderListener`의 `contextInitialized` 메서드가 호출될 때 IoC 컨테이너가 생성되며, 이 때 애플리케이션 전체에서 공유되는 빈들이 초기화됩니다.
   - 주로 애플리케이션 전반적인 설정 및 초기화 작업을 처리합니다.

2. **`DispatcherServlet`의 IoC 컨테이너 생성 시점:**
   - 각 `DispatcherServlet`는 웹 애플리케이션에 등록되고, 서블릿 컨테이너에 의해 초기화됩니다.
   - `DispatcherServlet`의 `init` 메서드가 호출될 때 해당 서블릿의 IoC 컨테이너가 생성되며, 웹 계층에서 사용되는 빈들이 초기화됩니다.
   - 각 `DispatcherServlet`는 독립적인 IoC 컨텍스트를 갖고 있으므로, 웹 요청을 처리하는 컴포넌트들을 담당합니다.

일반적으로는 웹 애플리케이션이 시작될 때 `ContextLoaderListener`가 먼저 실행되어 애플리케이션 전체에서 공유되는 빈들을 초기화하고, 그 후에 각 `DispatcherServlet`가 요청을 처리할 때마다 해당 서블릿의 IoC 컨테이너가 생성됩니다. 이렇게 함으로써 전역 설정과 웹 계층의 빈들이 서로 격리되어 모듈화되고, 성능 향상에 기여할 수 있습니다.


ㄴ