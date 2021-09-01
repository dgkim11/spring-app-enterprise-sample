package example.spring.jsp.domain.user;

import java.util.Optional;

public class UserRepository {
    // Null을 리턴하는 경우가 있는 메서드는 null을 리턴하지 말고 Optional을 리턴하여 메소드 호출하는 코드가 명시적으로 null 처리를 할 수 있게 해준다.
    public Optional<UserEntity> findById(String userId)   {

    }

    public void addUser(UserEntity userEntity) {
    }
}
