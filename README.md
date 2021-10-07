# Enterprise Application 개발을 위한 모듈 및 코드 구조
오늘날 Enterprise Application이 지향하고 있는 modern arcitecture에 대해서 이해하고 그러한 아키텍처로 애플리케이션의 모듈과 코드가 어떻게
구조화되어야 하는지에 대한 탬플릿을 포함한 실제 예제까지 포함된 샘플 프로젝트이다. 먼저 architecture에 대한 이론적인 부분을 이해하고 그 다음에는
실제로 구현된 샘플 코드에 대한 설명을 하도록 하겠다.

## DDD
DDD는 오늘날의 modern application의 개발 아키텍처, 방법론 등등을 포괄하는 개념으로 볼 수 있고 많은 내용들이 거론되고 있다. 
간단한 애플리케이션이고 빠르게 개발하는게 더 중요하며 앞으로 큰 변화나 발전이 없은 것으로 예상되는 애플리케이션에 적용하는 아키텍쳐 방법론은 아니다.
오래동안 지속되고 발전되며 시스템의 규모도 계속 커지는 그러한 애플리케이션을 개발할 때 적용되는 방법론이다.
많은 내용이 있지만 여기서는 소스 탬플릿을 위해서 필요한 DDD 개념들에 대해서만 언급한다.

### Domain Model
비즈니스에 핵심이 되는 모델들의 집합이다. 이 모델은 영구히 저장되어야 하는 Entity들과 하나의 모델을 표현하는 객체, Entity의 집합체인 Aggregator(Root Entity) 등등이
포함된다. 여기서 중요한 것은 이러한 모델들은 객체지향적이어야 한다. 엔티티가 데이타만 가지고 있고 이 데이타에 대한 제어권은 application에서 가지고 있는 것은
전혀 객체지향적이지 못하다. OOP에서 객체는 데이타와 그 데이타를 조작하는 로직이 하나로 추상화된 것을 말한다. 엔티티 내부에 있는 데이타의 소유권은 엔티티에게
있다. 따라서, 외부에서 엔티티에 데이타를 직접 수정해서는 안되고 엔티티 method를 통해서만 수정되어야 한다. 그렇다고 setter 메소드를 제공하는 것은 아니며
명확하게 비즈니스 관점에서 엔티티에게 어떤 요구를 하는 것인지를 명시적으로 method로 표현한다.

### Domain Service
여러개의 domain model들에 대해서 오케스트레이션을 해야하거나 하나의 도메인 모델과 다른 도메인 모델을 연결시키거나, 도메인 모델이 infrastructure layer와
연결이 필요할 경우 domain model이 직접 의존성을 갖지 않고 domain service가 이러한 부분을 처리해줌으로써 domain model이 외부의 의존성으로 인해
오염되는 것을 막는다. 

### Application Service
애플리케이션 service는 하나의 business use case를 구현하는 역할을 담당한다. 하나의 use case를 구현하기 위해서 repository,
domain service, domain model 등등을 모두 사용하여 하나의 use case를 구현하게 된다. domain service와 application service를 명확하게
구분하기 어려울 수도 있다. 다만, domain service는 domain model을 위한 서비스를 제공하고, application layer가 domain model을 사용하는데
필요한 서비스들을 제공하는 관점으로 봐야 한다. application serivce는 business use case에 더 중점을 두고 이러한 use case를 구현하기 위해서
domain layer, persistence layer, infra layer와 소통한다. 

### Persistence
애플리케이션 내에 Entity들은 영구히 저장되어야 한다. 또한, 저장된 데이타는 나중에 다시 추출할 수 있어야 한다. Repository는 이러한 persistence 역할을
담당한다. 그러나 실질적으로 데이타를 영구히 저장하는 역할의 담당은 애플리케이션이 아니라 외부에 있는 데이타베이스이거나 Storage가 된다. 이 부분은 애플리케이션
외부에 있는 infrastructure layer이다. 따라서, 애플리케이션은 이러한 infra와 직접적인 의존성을 가져서는 안된다. 따라서, Repository interface와
그 interface를 실제로 구현한 구현체로 구분되어야 한다. repository interface는 domain layer에 존재하며, 그 구현체는 infrastructure layer에
존재해야 한다. 애플리케이션 layer에 있는 service들은 이 repository interface를 통해서 entity에 대해서 read/write를 수행한다.

## Layered Architecture (Onion Architecture)
Onion Architecture는 Layered Architecture의 한 종류로 볼 수 있다. Onion Architecture는 DDD의 철학을 담고 있고, 오늘날 modern 
application이 지향하고 있는 아키텍처이다. 가장 중요한 것은 layer이고 하위의 layer는 상위의 layer에 영향을 받지 않는다.  

### Domain Layer
application core의 가장 중심에 있는 layer이다. 이 layer는 그 어떤 layer와도 의존성을 가지지 않는다. 모든 테스트는 외부 의존성 없이 자체적으로
테스트가 되어야 한다. business core 로직이 들어가는 곳이고 여기에는 Domain Model, Domain Service, Repository가 포함된다. 

### Application Layer
Application Service가 존재하는 layer이다. business use case들의 구현체들이 모여 있는 layer이다. application layer는 
domain layer와 interface를 하며 infrastructure layer의 서비스를 호출한다. 여기서 중요한 것은 infra layer의 서비스 객체들은 
application layer에 Dependency Injection 되어야 한다. 즉, Inversion of Control 형태로 객체가 주입되어야 한다. 이유는
application layer가 infra layer에 의존성을 가져서는 안되기 때문이다.

### Presentation Layer
Application Core에 포함되지 않으며 오로지 사용자와의 interface에 관련된 로직만을 가진다. 웹의 경우 MVC 패턴으로 개발되도록 한다.
여기 layer는 application layer와 필요하다면 infrastructure layer와만 소통한다. domain layer를 직접 접근해서는 안된다.
presentation layer도 마찬가지로 infra 서비스는 IoC 형태의 의존성 주입이 되어야 한다.

### Infrastructure Layer
애플리케이션이 외부와의 세상과 소통이 필요하다면 그것은 다 infra layer가 된다. Database, Network 통신, 외부 Service, Storage 등등이 
모두 infra에 해당한다. 이러한 infra 서비스들은 application 에서 직접 참조해서 infra에 의존성을 가져서는 안되고 application은 
외부와 무엇을 소통할 것인지에 대한 interface를 정의하고 infra layer는 이 interface에 대한 구현체를 application에 제공하도록 한다.

## Application Source 구조
앞에서 언급한 DDD, Layered Architecture를 명심하고 애플리케이션의 소스를 구조화하여야 한다. 이러한 구조화를 위해서 애플리케이션 모듈은 
web 애플리케이션의 경우 아래와 같은 모듈을 갖는다. 여기서 모듈이란 IntelliJ에서 모듈 개념과 동일하다.

### domain 모듈
여기에는 Domain Model, Domain Service, Repository interface가 존재하는 곳이다. Domain 모듈에서 필요한 외부 의존성은 domain 모듈내에
infra layer를 구성한다.
### application 모듈
application layer에 해당한다. application layer에서 필요한 외부 의존성은 application 모듈 내에서 infra layer를 구성한다.
### web 모듈
presentation layer에 해당한다. 
### core-common 모듈
application core에 해당하는 공통 기능들이 들어간다. 이 common 모듈은 application core에서만 사용하는 것이다. web이나, infra 모듈에서
필요한 공통 기능은 해당 모듈 내부에 구현한다. 
### 기타 모듈
* batch : 배치 애플리케이션 배포가 필요한 경우 별도의 batch 모듈을 만든다.
* api : 외부에 API 형태로 서비스를 제공하는 경우 api 모듈을 만든다. 
* common : application의 모든 모듈에서 공통으로 사용될 수 있는 공통성 코드를 갖는 모듈이다. 비즈니스 로직을 가지지 않는 순수한 공통 기능이다.

## CQRS(Command and Query Responsibility Segregation)
CQRS는 데이타를 write하는 부분과 read하는 부분을 분리하는 패턴이다. Modern application의 아키텍쳐에서 중요한 부분중에 하나로서 
애플리케이셔의 성능 향상, 효과적인 데이타 관리, 확장성, read와 write를 분리함으로써 각자가 독립적으로 움직임으로서 모듈 및 서비스간의 의존성을 제거하고
자율성을 보장할 수 있게 된다. 보통 CQRS는 분산환경에서 효과적인 데이타를 처리를 위한 방법이지만, 하나의 애플리케이션 내에서 CQRS 목적은
read의 로직 변경으로 인해 write 로직이 변경되거나 write 로직의 변경으로 인해서 read 로직이 영향을 받는 것을 없애기 위함이다. 또한, RDB의 경우
복잡한 데이타 추출을 위해서 join과 where절이 매우 복잡한 쿼리를 생성하여 쿼리에 대한 가독성을 떨어뜨리고 유지보수가 어려운 코드를 양성하는 것을
방지할 수 있다. 또한, 이러한 복잡한 쿼리는 이미 쿼리 내부에 겉으로는 드러나지 않는 비즈니스 로직이 함께 숨겨져 있게 된다. 이러한 쿼리는 결국 애플리케이션
로직과 직접적인 의존성을 갖게되고 business 로직 변경은 이러한 쿼리 또한 변경될 수 밖에 없는 구조가 된다. 즉, CQRS란 write를 위한 스키마와
read를 위한 스키마가 다르도록 구성하는 것이다. read에 최적화된 스키마를 별도로 구성하여 read로 인한 DB의 부하, 복잡한 쿼리를 제거하고 
명시적으로 명시적으로 데이타를 관리하고 그 데이타에 대한 test case도 작성할 수 있다. read를 위한 스키마를 보통 materialized view라고 말한다. 
예로, 애플리케이션 내에서 좀 더 Intelligent한 데이타를 생성하여 분석 정보를 제공하고 싶다면 그것을 위한 별도의 schema를 구성하고 write 스키마의
데이타가 변경될 때마다 event를 받아서 read 스키마의 데이타를 동기화한다.

## Transaction 관리
트랜잭션은 최대한 짧아야 한다. 트랜잭션이 길면 길수록 DB 자원에 lock이 길어기지 때문에 자원의 효율성이 떨어지고 트랜잭션 관련 에러가 자주 발생할 수 있다.
트랜잭션은 기본적으로 repository layer에서 수행한다. 하나의 use case가 트랜잭션 보장이 되어야 하는 경우에는 application layer에서 수행한다.
단, 트랜잭션 시작과 종료 사이에 side effect가 발생할 수 있는 코드는 제거해야 한다. side effect로 인해 트랜잭션이 실패하지 않도록 한다. 대표적인
side effect는 외부 시스템과의 integration 부분이다.

## Utility
아래의 utility나 framework은 Enterprise Java 애플리케이션 개발 시에 기본적으로 포함되어야 하는 것들이다. 
* Lombok : Java 언어는 verbose한 코드를 많이 가지고 있다. Lombok을 이용하면 이러한 verbose한 코드를 제거하여 코드를 깔끔하고 정리할 수 있다.
* Security Vault

## Test
### Unit Test
하나의 클래스, 하나의 메서드에 대한 specification을 정의하고 구현한 후에 spec대로 동작하는지를 검증한다.
unit test는 클래스나 메서드의 역할과 행위에 대해서 규정하는 문서의 역할을 한다.
unit test시에는 외부 의존성과 대상 클래스 이외의 다른 클래스들은 Mock 처리한다.

### Integration Test
이 테스트는 통합에 촛점을 맞춘 것이다. business 로직을 테스트하는 것이 아니다.
Integration test에 있는 ProductManager 테스트는 business logic보다는 integration에 촛점을 맞춘 테스트이다. 
외부 의존성도 함께 테스트하는 것이기 때문에 외부 객체에 대해서 Mock 객체를 사용하지 않는다. 만약, 외부 시스템과의 연동이 현실적으로 불가능한 경우 
Mock Server를 사용하여 가상의 외부 시스템을 구성할 수 있다.

### Functional Test
이 테스트는 integration test보다도 상위에 있는 테스트로서 integration까지 포함하여 실제 business use case가 제대로 동작하는지를
검증하는 테스트이다. 전체 기능 검증으로서 Mock 객체를 사용하지 않는다. 만약, 외부 시스템과의 연동이 현실적으로 불가능한 경우 Mock Server를 사용하여
가상의 외부 시스템을 구성할 수 있다.

### Acceptance Test
이것은 가장 최상위의 테스트이다. 사전적인 의미로 접근한다면 functional test가 기능이 제대로 동작하는지를 검증하는 것이라면 UAT는 최종 사용자 관점에서 
이 테스트의 결과가 사용자(고객)가 만족하는 결과인지를 보는 것이다. 따라서, 기능의 동작 뿐만 아니라 그 결과가 보여지는 UI 및 UX까지도 포함하는 더 크고
넓은 테스트이다. 훨씬 더 비즈니스 관점에서 테스트의 결과를 평가하는 것이다. 그러나, 여기서는  WebMvc 레벨의 test를 accpetance test로 보도록 하자.

### Test 코드 작성 가이드
* Test case 작성 순서
  * 첫째, business use case를 정의하고 거기에 맞는 AC(acceptance criteria)를 정의한다.
  * 둘째, AC는 application layer내에 unit test로 정의한다.
  * 셋째, 하나의 AC에 대해서 given/when/then을 정의하고 거기에 맞는 domain model, domain service를 구현한다. 
  * 넷째, domain model, domain service에 대해서 unit test를 작성하고 코드를 구현해가면서 test case와 구현로직을 완성시킨다.
  * 다섯째, application layer에 있는 application service에 대한 unit test를 정의하고 코드를 구현해 나간다. 이중에 application layer에서 
  테스트하기 어려운 case는 functional test case로 이동시킨다. 
  * 여섯째, integration test를 작성한다. repository나 외부 시스템과의 연동이 제대로 되는지 테스트한다.
  * 일곱째, functional test로서 business use case에 대해서 가장 상위에서 가장 실제와 같은 환경에서 전체를 테스트하는 코드를 작성한다. 예로, MVC
    의 Controller를 테스트하는 것이 여기에 해당한다. 실제로 애플리케이션이 로딩이 된 상태에서 테스트가 이루어진다.
    
* Test case 작성 핵심
  TDD에서 말하듯이 test case -> 구현 -> 리팩토링 형태로 하나의 test case와 관련된 코드만을 작성한다.하나의 test case를 작성하면서 구현되는 코드 
  이외에는 추가적인 코드를 작성하지 않는다. 해당 test case와 무관한 로직이 생각났다고 바로 작성하게되면 그 부분은 나중에 test case에서 빠질 수 있다. 
  현재의 test case에만 해당하는 코드만을 작성함으로써 test coverage에서 누락되는 코드가 없게된다. 또한, 이렇게 작성하면 개발할 때 한번에 너무 많은 
  구현 로직을 머리속에 생각하느라 코드 구현이 어려워지는 문제도 해결된다. 지금, 작성중인 test case에만 집중함으로써 하나에만 집중할 수 있게 해준다.

* 실패한 Test case가 지속적인 구현과 리팩토링으로 성공하였을 때의 짜릿함을 즐겨야 한다.
  구현이 완료되지 않은 상태에서 test case를 먼저 작성한 후 수행하면 당연히 test case가 실패한다. 그랬던 test case를 하나하나 구현해가면서 코드가
  완성되가고 점점 test case가 성공에 가까워지는 것을 느끼면서 개발하면 즐거움이 더하다. 최종적으로 성공했을 때 녹색마크를 보면 짜릿함을 느끼게 된다.

### Helper 클래스
Test를 하기 위해서는 여러가지 초기화하는 작업이나 반복적인 작업들이 있게된다. 이러한 코드를 test case 내부에 넣게되면 테스트를 위한 준비 작업과
실제 테스트를 통한 검증 코드가 섞이게 되어 가독성이 떨어지게 된다. 이러한 코드를 매번 test 코드안에 넣지 말고 별도로 helper 클래스에 구현하여
test case를 간결하게 만들고 가독성을 높인다. 

# 샘플 코드 설명

## 주요 요구사항
예제로 작성한 애플리케이션은 예약 시스템이다. 요구사항은 아래와 같다.
* 현재 예매 가능한 상품만을 보여준다.
  * 상품은 상품명, 가격, 카테고리, 판매 시작날짜, 판매 종료날짜, 옵션(one bed, twin bed, two beds, 조식 포함 여부), valid 여부, 상품 contents 등등을 가진다.
  * 판매 종료날짜가 정해지지 않은 경우 무기한 판매이다.
  * 상품에 문제가 있는 경우 강제로 invalid할 수 있다. invalid되면 예매 가능한 상품이 아니다.
  * 오늘 날짜가 판매 시작날짜와 종료날짜 사이가 아닌 상품은 보여주지 않는다.
* 상품 옵션은 각 옵션별 무료이거나 추가금액이 존재한다.
* 예매는 룸의 종류와 기간을 선택한 후 결제할 수 있다.
* 결제는 외부의 Payment Gateway 업체와 연동되어 결제를 수행한다.
* 결제가 성공한 경우 주문한 상품의 해당 날짜는 주문 가능 목록에서 사라진다.
* 결제시작 시에 해당 룸을 예약 기간내에 예약할 수 있는지 확인한다.
* 결제가 시작하고 완료될 때까지 해당 룸과 기간은 해당 사용자에게 귀속된다.
* 결제실패할 경우 해당 사용자에게 귀속되었던 룸의 기간은 해지된다.
* 자신이 주문한 상품 목록을 볼 수 있다.

## 모듈 설명
* hotel-api
  * 외부로 제공되는 API가 있는 경우에 별도의 api application으로 수행되기 위한 모듈이다. API 서비스를 위한 application layer가 존재한다.
* hotel-batch
  * 배치 application을 위한 모듈이다. 배치를 위한 application layer가 존재한다.
* hotel-common
  * 모든 모듈에서 공통적으로 사용될 수 있는 유틸리티나 공통적인 기능을 가진다. 다만, 여기에 있는 클래스는 어떤 애플리케이션 비즈니스 로직과 
    관련된 로직도 가지고 있지 않는다. business context와 무관한 공통이다. 예로, yaml 파싱이라던가, event bus를 구현한 로직과 같이
    전혀 비즈니스와는 무관한 공통 로직을 말한다.
* hotel-domain
  * 핵심 business 로직이 들어있는 곳이다. domain model, domain service들이 존재한다.
* hotel-infra
  * 외부 의존성을 처리하는 모듈이다. database, network, OS, 외부 API, 외부 시스템 등등과의
  연동과 관련된 구현 로직은 모두 이곳에 존재한다. domain 또는 application layer에서는 dependency 
  injection으로 구현체를 interface로 참조한다.
* hotel-web
  * web application 이다. application layer와 presentation layer가 여기에 있다.

## Profile별 환경설정
hotel-infra-common.xml,hotel-infra-develop.xml
HotelAppInfraPropertiesConfig.class
```
task integrationTest(type: Test) {
    useJUnitPlatform()
    testClassesDirs = sourceSets.integrationTest.output.classesDirs
    classpath = sourceSets.integrationTest.runtimeClasspath

    if(project.hasProperty('production'))  {
        systemProperty "spring.profiles.active", "production"
    } else if(project.hasProperty('develop'))   {
        systemProperty "spring.profiles.active", "develop"
    } else  {
        systemProperty "spring.profiles.active", "local"
    }
}
```

Mybatis로 DDD 형태의 애플리케이션 구조화
* 참조 : https://dzone.com/articles/spring-mvc-and-java-based-configuration-1
* 참조 : https://herbertograca.com/2017/08/03/layered-architecture/
