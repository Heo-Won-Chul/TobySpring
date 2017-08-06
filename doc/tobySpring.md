 
## Application Context

### 종류

- StaticApplicationContext
   - 개발을 하면서 테스트용도 이외에는 쓸 일이 없다. ( 스프링을 이용한 자체 프레임워크 개발에는 사용? )

- GenericApplicationContext
   - 가장 일반적인 애플리케이션 컨텍스트 구현이다. xml 등록 가능 ( GenericXmlApplicationContext 를 더 많이 사용 )

- WebApplicationContext
   - 가장 많이 쓰는 애플리케이션 컨텍스트 구현이다. ( XmlWebApplicationContext 를 더 많이 사용 )

       > 웹은 자바 애플리케이션의 main() 메소드가 없기 때문에 서블릿을 만들어 질 때 컨텍스트를 생성해둔 다음
          요청이 서블릿으로 들어올 때마다 getBean()으로 필요한 빈을 가져와 정해진 메소드를 실행해준다.
          서블릿에서 DispatcherServlet 이라는 것을 제공하여 가능하다. (web.xml에 등록)
          

### 특징

- ApplicationContext는 계층 구조를 갖을 수 있다.
   - 해당 컨텍스트에서 원하는 빈을 발견하지 못하면, 자신의 부모 컨텍스트에서 빈을 찾는다.
  
### Web Application

1. 웹 모듈 안에 컨테이너를 두는 방법
    - 독립적으로 배치 가능한 웹 모듈(war) 형태로 애플리케이션
    - 웹 애플리케이션은 여러 개의 서블릿을 가질 수 있다.
    - 몇 개의 서블릿이 중앙집중식으로 모든 요청을 다 받아서 처리하는 방식 => 프론트 컨트롤러 패턴
2. 엔터프라이즈 애플리케이션 레벨에 두는 방법

    > 일반적으로는 두가지 방식을 모두 사용해 컨테이너를 만든다.
      두 개의 컨테이너, 즉 WebApplicationContext가 만들어진다.
      두개의 서블릿이 하나의 웹 애플리케이션에서 사용되는 경우, 두 서블릿의 컨텍스트에서 공통적으로 사용하는 별도의 컨텍스트를 만들어준다.
   
## IoC/DI

### IoC Container

- IoC 컨테이너의 가장 기본적인 역할은 코드를 대신해서 애플리케이션을 구성하는 오브젝트를 생성하고 관리하는 것이다.
- 컨테이너는 빈 설정 메타정보를 통해 빈의 클래스와 이름을 제공받는다. 빈을 만들기 위한 설정 메타정보는 파일이나 애노테이션 같은 리소스로부터 전용 리더를 읽혀서 BeanDefinition 타입의 오브젝트로 변환된다.

    ```text
    XML 문서  ↘
    애노테이션 → BeanDefinition → IoC 컨테이너
    자바 코드  ↗
    
    ```

- 몇 가지 필수항목을 젱하면 컨테이너에 미리 설정된 디폴트 값이 적용된다.
- 빈 설정 메타정보 항목 중에서 가장 중요한 것은 클래스 이름이다.

### Bean

#### 빈 등록 방법

1. bean 태그
   - 가장 처음 접할 때 바람직한 방법 중 하나 이다.
   - 자주 사용하는 기술이라면, bean 태그 대신 간결한 커스텀 태그를 만드는 것이 좋다.
   
2. 자동인식을 이용한 빈 등록
   - 스프링의 빈 스캐너는 지정된 클래스패스 아래에 있는 모든 패키지의 클래스를 대상으로 필터를 적용하여 빈 등록을 위한 클래스들을 선별해낸다.
   - 디폴트 필터에 적용되는 애노테이션을 스프링에서는 스테레오타입 애노테이션이라고 부른다 ( @Component, @Service, @Repository, ... )
   - XML을 이용한 빈 스캐너 등록 ( 빈 스캐너를 내장한 애플리케이션 컨텍스트 사용 )
   
      ```xml
      <context:component-scan base-package="com.tistory.tobyspring" />
      ```

3. @Configuration 클래스의 @Bean 메소드
   - 컴파일러나 IDE를 통한 타입 검증이 가능하다.
   - 이해하기 쉽다.
   
#### Scope

- @Scope나 xml 속성값 scope 변경 가능하다.
- 기본적으로 singleton이다. ( singleton, prototyep, request, session, globalsession )
- prototype은 오브젝트 중심 아키텍처에 적합(?)할 수 있다.
- session은 DL 방식으로는 Provider나 ObjectFactory에 넣어두고 사용할 수 있다. DI 방식으로는 프록시 설정을 변경하여 사용할 수 있다.

#### Meta 정보

- 식별자 ( id, name을 이용 )
   - name 경우, 별칭처럼 사용하여 다양하게 참조될 수 있다.

- 초기화 메소드

   1. InitializingBean 구현
   2. xml 속성값 init-method 지정   
   3. __@PostConstruct__
   4. @Bean(initMethod)
   
- 제거 메소드

   1. DisposableBean 구현
   2. xml 속성값 destroy-method 지정
   3. __@PreDestroy__
   4. @Bean(destroyMethod)
   
#### Bean 종류

- 애플리케이션 로직 빈 
   - 비즈니스 로직을 담당하는 빈
- 애플리케이션 인프라 빈 
   - 스프링에서 제공하지 않는 부분을 빈으로 만드는 것
   - ex) DataSource, DataSourceTransactionManager
- 컨테이너 인프라 빈
   - 빈 등록&생성, 환경설정, 초기화
   - @PostConstruct
   
      > @Configuration, @Bean, @PostConstruct은 스프링 기본 스펙이 아니다.
        빈 후처리기에 의해 등록 되는 것이기 때문에, 빈 후처리기를 등록해주는 <context:annotation-config /> 를 추가해줘야 한다.
        만약 <context:component-config basepackages="..." /> 를 등록해줬다면, component scan과정에서 annotation-config에서 
        등록해주는 빈을 등록 해주기 때문에 <context:annotation-config /> 를 생략해도 된다.
        
   - @ComponentScan, @Import, @ImportResource, @Enable~~
   
