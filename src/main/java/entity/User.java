package entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private Long userId;
    private String userName;
    private String userEmail;
    private String userPhone;

    public User(String userName, String userEmail, String userPhone) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
    }
}
