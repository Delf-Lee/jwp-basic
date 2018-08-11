#### 1. Tomcat 서버를 시작할 때 웹 애플리케이션이 초기화하는 과정을 설명하라.
1. Tomcat은 ServletContainer를 생성한다.
2. 웹 애플리케이션이 필요한 Servlet을 생생해 놓는다 (servlet pool)

#### 2. Tomcat 서버를 시작한 후 http://localhost:8080으로 접근시 호출 순서 및 흐름을 설명하라.
1. 해당 주소 해당 포드에 요청이 들어오면 Tomcat을 `DispatcherServlet`에게 요청을 전달한다.
2. 그 전에 fileter를 거쳐 지정한 작업을 완료한다. resource에 대한 응답 등
3. 요청을 받은 `DispatcherServlet`은 url에 따라 알맞은 Controller를 호출하여 작업을 위임한다.
4. Controller는 그에 맞는 요청을 처리하고 view를 반환한다.(jsp페이지 반환 등)
5. 반환된 view를 브라우저에게 전달한다.

#### 7. next.web.qna package의 ShowController는 멀티 쓰레드 상황에서 문제가 발생하는 이유에 대해 설명하라.
* DB의 접근이 많아진다?
