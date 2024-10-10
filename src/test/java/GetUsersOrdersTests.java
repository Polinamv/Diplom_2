import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.example.OrderMethods;
import org.example.OrderRequestBody;
import org.example.UserMethods;
import org.example.UserRequestBody;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@DisplayName("Получение заказов пользователя")
public class GetUsersOrdersTests {

    final UserRequestBody user = new UserRequestBody("new@email.dot", "123123", "Polly");
    final UserMethods userMethods = new UserMethods();
    final OrderMethods orderMethods = new OrderMethods();

    String headerValue;

    @Before
    public void setUp() {
        ValidatableResponse createUserResponse = userMethods.create(user);
        headerValue = createUserResponse.extract().path("accessToken");

        OrderRequestBody body = new OrderRequestBody(orderMethods.getIngredientsData());
        orderMethods.createOrder(body, headerValue);
    }

    @After
    public void clearUp() {
        userMethods.delete(headerValue);
    }

    @Test
    @DisplayName("Получение заказов пользователя - 200")
    public void getUsersOrders200() {
        ValidatableResponse getUsersOrdersResponse = orderMethods.getUsersOrders(headerValue);
        int statusCode = getUsersOrdersResponse.extract().statusCode();
        Assert.assertEquals(HttpStatus.SC_OK, statusCode);
        boolean isSuccess = getUsersOrdersResponse.extract().path("success");
        Assert.assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Получение заказов пользователя - 401")
    public void getUsersOrders401() {
        ValidatableResponse getUsersOrdersResponse = orderMethods.getUsersOrders("");
        int statusCode = getUsersOrdersResponse.extract().statusCode();
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);
        boolean isSuccess = getUsersOrdersResponse.extract().path("success");
        Assert.assertFalse(isSuccess);
        String errorMessage = getUsersOrdersResponse.extract().path("message");
        Assert.assertEquals("You should be authorised", errorMessage);
    }
}