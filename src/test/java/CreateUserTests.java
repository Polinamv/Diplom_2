import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.example.UserRequestBody;
import org.example.UserCredentials;
import org.example.UserMethods;
import org.junit.Assert;
import org.junit.Test;

@DisplayName("Создание пользователя")
public class CreateUserTests {

    final UserMethods methods = new UserMethods();

    @Test
    @DisplayName("Создание пользователя - 200")
    public void createUser200Test() {
        UserRequestBody user = new UserRequestBody("98oеuj4@new.ru", "123234", "lalal");

        ValidatableResponse createUserResponse = methods.create(user);
        String headerValue = createUserResponse.extract().path("accessToken");
        boolean isSuccessCreate = createUserResponse.extract().path("success");
        Assert.assertTrue(isSuccessCreate);

        ValidatableResponse loginUserResponse = methods.login(UserCredentials.from(user), headerValue);
        boolean isSuccessLogin = loginUserResponse.extract().path("success");
        Assert.assertTrue(isSuccessLogin);

        methods.delete(headerValue);
    }

    @Test
    @DisplayName("Создание уже существующего пользователя - 403")
    public void createUser403Test() {
        UserRequestBody user = new UserRequestBody("newaa@new.ru", "123234", "lalal");

        methods.create(user);
        ValidatableResponse createUserResponse = methods.create(user);
        boolean isSuccessCreate = createUserResponse.extract().path("success");
        Assert.assertFalse(isSuccessCreate);
        String errorMessage = createUserResponse.extract().path("message");
        Assert.assertEquals("User already exists", errorMessage);
        int statusCode = createUserResponse.extract().statusCode();
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, statusCode);
    }
}