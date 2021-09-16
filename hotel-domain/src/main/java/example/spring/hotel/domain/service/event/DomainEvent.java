package example.spring.hotel.domain.service.event;

/**
 * Domain들 간에 상태 변화나 행동의 결과에 대해서 다른 도메인에서 알아야할 때 Observer 패턴처럼 동작하도록 한다.
 * 상태 변화나 행동에 결과에 대해서 event를 발생시키고 그 event에 대해서 관심있는 다른 도메인은 subscription을 한다.
 */
public interface DomainEvent {
}
