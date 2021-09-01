package example.spring.jsp.service;

import example.spring.jsp.domain.user.InvalidUserException;
import example.spring.jsp.domain.user.UserEntity;
import example.spring.jsp.domain.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserManagementService {
    private UserRepository userRepository;

    public UserManagementService(UserRepository repository) {
        this.userRepository = repository;
    }

    public void addUser(UserVO userVo) throws InvalidUserException {
        Optional<UserEntity> userOptional = userRepository.findById(userVo.getUserId());
        if(userOptional.isPresent())    {
            throw new InvalidUserException("이미 존재하는 사용자입니다. userId:" + userVo.getUserId());
        }
        UserEntity userEntity = UserEntity.builder()
                .userId(userVo.getUserId())
                .userName(userVo.getUserName())
                .birthDate(userVo.getBirthDate())
                .email(userVo.getEmail())
                .gender(userVo.getGender())
                .homeAddress(userVo.getHomeAddress())
                .mobilePhone(userVo.getMobilePhone())
                .build();

        userRepository.addUser(userEntity);
    }
}
