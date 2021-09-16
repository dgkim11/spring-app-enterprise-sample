package example.spring.hotel.domain.model.customer;

import example.spring.hotel.domain.model.DomainEntity;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements DomainEntity {
    private Long customerId;
    private @NotNull String name;
    private String userId;
    private String password;
    private @Email String emailAddr;
}
