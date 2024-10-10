import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.example.UserRequestBody;
import org.example.UserMethods;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@DisplayName("Изменение данных пользователя")
public class ChangeUserDataTests {

    final UserMethods methods = new UserMethods();
    final String email = "polly@poll.ru";
    final String password = "123234";
    final String name = "polya";
    final UserRequestBody user = new UserRequestBody(email, password, name);

    String headerValue;

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
    @DisplayName("Изменение данных пользователя - 200")
    public void changeUserData200Test() {
        ValidatableResponse changedUserData = methods
                .patch(headerValue, getUpdatedBody("email@new.ru"));
        boolean changeUserDataSuccess = changedUserData.extract().path("success");
        Assert.assertTrue(changeUserDataSuccess);
        String newEmail = changedUserData.extract().path("email");
        String newName = changedUserData.extract().path("name");
        Assert.assertNotEquals(email, newEmail);
        Assert.assertNotEquals(name, newName);
    }

    @Test
    @DisplayName("Изменение данных пользователя - 401")
    public void changeUserData401Test() {
        ValidatableResponse changedUserData = methods
                .patch("", getUpdatedBody("email@new.ru"));
        int statusCode = changedUserData.extract().statusCode();
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        boolean success = changedUserData.extract().path("success");
        Assert.assertFalse(success);
        String errorMessage = changedUserData.extract().path("message");
        Assert.assertEquals("You should be authorised", errorMessage);
    }

    @Test
    @DisplayName("Изменение данных пользователя - 403")
    public void changeUserData403Test() {
        ValidatableResponse changedUserData = methods
                .patch(headerValue, getUpdatedBody(email));
        int statusCode = changedUserData.extract().statusCode();
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, statusCode);
        boolean success = changedUserData.extract().path("success");
        Assert.assertFalse(success);
        String errorMessage = changedUserData.extract().path("message");
        Assert.assertEquals("User with such email already exists", errorMessage);
    }

    private String getUpdatedBody(String email) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode user = mapper.createObjectNode();
            user.put("email", email);
            user.put("password", "098765");
            user.put("name", "Noname");
            return user.toString();
        } catch (Exception ex) {
            return "";
        }
    }
}