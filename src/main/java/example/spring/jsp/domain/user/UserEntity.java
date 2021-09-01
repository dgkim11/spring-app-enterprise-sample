package example.spring.jsp.domain.user;

import java.time.LocalDate;

public class UserEntity {
    public void UserEntity()   { }

    public enum Gender  {
        FEMALE, MALE;
    }
    private String userId;
    private String userName;
    private String homeAddress;
    private String mobilePhone;
    private LocalDate birthDate;
    private Gender gender;
    private String email;

    public static UserEntityBuilder builder()   {
        return new UserEntityBuilder();
    }

    public static class UserEntityBuilder   {
        private UserEntity userEntity;

        public UserEntityBuilder() {
            this.userEntity = new UserEntity();
        }
        public UserEntityBuilder userId(String userId)  {
            userEntity.userId = userId;
            return this;
        }
        public UserEntityBuilder userName(String userName)  {
            userEntity.userName = userName;
            return this;
        }
        public UserEntityBuilder homeAddress(String homeAddress)    {
            userEntity.homeAddress = homeAddress;
            return this;
        }
        public UserEntityBuilder mobilePhone(String mobilePhone)    {
            userEntity.mobilePhone = mobilePhone;
            return this;
        }
        public UserEntityBuilder birthDate(LocalDate birthDate) {
            userEntity.birthDate = birthDate;
            return this;
        }
        public UserEntityBuilder gender(Gender gender)  {
            userEntity.gender = gender;
            return this;
        }

        public UserEntityBuilder gender(String gender)  {
            userEntity.gender = Gender.valueOf(gender);
            return this;
        }

        public UserEntityBuilder email(String email)    {
            userEntity.email = email;
            return this;
        }
        public UserEntity build() throws InvalidUserException  {
            validateUser(userEntity);
            return userEntity;
        }

        private void validateUser(UserEntity userEntity) throws InvalidUserException {
            if(userEntity.userId == null || "".equals(userEntity.userId)) throw new InvalidUserException("user id가 누락되었습니다.");
            if(userEntity.userName == null || "".equals(userEntity.userName)) throw new InvalidUserException("user 이름이 누락되었습니다.");
            if(userEntity.email == null || "".equals(userEntity.email)) throw new InvalidUserException("email이 누락되었습니다.");
        }
    }
}
