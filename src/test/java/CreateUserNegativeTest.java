import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.example.UserMethods;
import org.example.UserRequestBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@DisplayName("Создание пользователя - 403")
@RunWith(Parameterized.class)
public class CreateUserNegativeTest {

    final UserMethods methods = new UserMethods();
    final String email;
    final String password;
    final String name;

    public CreateUserNegativeTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[][] testData() {
        return new Object[][] {
                {"email@1.ru", "pass123", null},
                {"email@2.ru", null, "Name"},
                {null, "pass123", "Name"},
                {"", "pass123", "Name"},
                {"email@2.ru", "", "Name"},
                {"email@2.ru", "pass123", ""},
        };
    }

    @Test
    @DisplayName("Создание пользователя без одного из обязательных полей")
    public void createUser403() {
        UserRequestBody user = new UserRequestBody();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);

        ValidatableResponse createUserResponse = methods.create(user);
        int createdUserStatusCode = createUserResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_FORBIDDEN, createdUserStatusCode);
        String errorMessage = createUserResponse.extract().path("message");
        assertEquals("Email, password and name are required fields", errorMessage);
    }
}