import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.UserCredentials;
import org.example.UserMethods;
import org.example.UserRequestBody;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
@DisplayName("Логин пользователя")
public class LoginUserTests {

    final UserMethods methods = new UserMethods();
    final UserRequestBody user = new UserRequestBody("valid@email.ru", "validPass", "Mane");
    final String email;
    final String password;
    final boolean success;
    final int statusCode;

    String headerValue;

    public LoginUserTests(String email, String password, boolean success, int statusCode) {
        this.email = email;
        this.password = password;
        this.success = success;
        this.statusCode = statusCode;
    }

    @Parameterized.Parameters
    public static Object[][] testData() {
        return new Object[][]{
                {"valid@email.ru", "validPass", true, 200},
                {"invalid@email.ru", "validPass", false, 401},
                {"valid@email.ru", "invalidPass", false, 401},
                {"invalid@email.ru", "invalidPass", false, 401}
        };
    }

    @Before
    public void setUp() {
        ValidatableResponse createUserResponse = methods.create(user);
        headerValue = createUserResponse.extract().path("accessToken");
    }

    @After
    public void clearUp() {
        methods.delete(headerValue);
    }

    @Test
    @DisplayName("Логин пользователя")
    public void loginUser() {
        UserCredentials credentials = new UserCredentials(email, password);
        ValidatableResponse loginUserResponse = methods.login(credentials, headerValue);
        int loginUserStatusCode = loginUserResponse.extract().statusCode();
        Assert.assertEquals(statusCode, loginUserStatusCode);
        boolean isSuccess = loginUserResponse.extract().path("success");
        Assert.assertEquals(success, isSuccess);
        if (!success) {
            String errorMessage = loginUserResponse.extract().path("message");
            assertEquals("email or password are incorrect", errorMessage);
        }
    }
}