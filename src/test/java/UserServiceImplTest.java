import org.example.data.dto.UserDto;
import org.example.service.ServiceFactory;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTest {
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = ServiceFactory.getInstance().getUserService();
    }

    @ParameterizedTest
    @CsvSource({"2, Maria Petrova", "3, Dmitry Sidorov", "4, Anna Kuznetsova", "5, Sergey Smirnov"})
    void getUserById(Long id, String name) {
        UserDto userDto = userService.getUserById(id);
        assertNotNull(userDto);
        assertEquals(id, userDto.getUserId());
        assertEquals(name, userDto.getUserName());
    }

    @Test
    void getAllUsers() {
        List<UserDto> userDtos = userService.getAllUsers();
        assertNotNull(userDtos);
        assertEquals(30, userDtos.size());
        assertEquals("Alexey Ivanov", userDtos.get(0).getUserName());
        assertEquals("Maria Petrova", userDtos.get(1).getUserName());
        assertEquals("Dmitry Sidorov", userDtos.get(2).getUserName());
        assertEquals("Anna Kuznetsova", userDtos.get(3).getUserName());
        assertEquals("Sergey Smirnov", userDtos.get(4).getUserName());
    }

    @Test
    void createUser() {
        UserDto userDto = new UserDto();
        userDto.setUserName("Ivan Petrov");
        userDto.setUserEmail("ivan.petrov@gmail.com");
        userDto.setUserPhone("+375296789777");
        UserDto createdUserDto = userService.createUser(userDto);
        assertNotNull(createdUserDto);
        assertNotNull(createdUserDto.getUserId());
        assertEquals(userDto.getUserName(), createdUserDto.getUserName());
        assertEquals(userDto.getUserEmail(), createdUserDto.getUserEmail());
        assertEquals(userDto.getUserPhone(), createdUserDto.getUserPhone());
    }

    @Test
    void updateUser() {
        UserDto userDto = userService.getUserById(1L);
        assertNotNull(userDto);
        userDto.setUserName("Alexey Petrov");
        userDto.setUserEmail("alexey.petrov@gmail.com");
        UserDto updatedUserDto = userService.updateUser(userDto);
        assertNotNull(updatedUserDto);
        assertEquals(userDto.getUserId(), updatedUserDto.getUserId());
        assertEquals(userDto.getUserName(), updatedUserDto.getUserName());
        assertEquals(userDto.getUserEmail(), updatedUserDto.getUserEmail());
        assertEquals(userDto.getUserPhone(), updatedUserDto.getUserPhone());
    }

    @Test
    void deleteUser() {
        UserDto userDto = userService.getUserById(1L);
        assertNotNull(userDto);
        userService.deleteUser(userDto);
        assertNull(userService.getUserById(1L));
    }
}
