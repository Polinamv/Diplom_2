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

import java.util.ArrayList;
import java.util.List;

@DisplayName("Создание заказа без авторизации")
public class CreateOrderNoAuthTests {

    final OrderMethods orderMethods = new OrderMethods();
    final UserMethods userMethods = new UserMethods();
    final UserRequestBody user = new UserRequestBody("abcd@molly.com", "123123", "Polina");

    String headerValue;

    @Before
    public void setUp() {
        ValidatableResponse createUserResponse = userMethods.create(user);
        headerValue = createUserResponse.extract().path("accessToken");
    }

    @After
    public void clearUp() {
        userMethods.delete(headerValue);
    }

    @Test
    @DisplayName("Создание заказа с ингридиентами - 401")
    public void createOrderWithIngredients401() {
        OrderRequestBody body = new OrderRequestBody(orderMethods.getIngredientsData());
        ValidatableResponse createOrderResponse = orderMethods.createOrder(body, "");
        int createOrderStatusCode = createOrderResponse.extract().statusCode();
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, createOrderStatusCode);
    }

    @Test
    @DisplayName("Создание заказа без ингридиентов - 401")
    public void createOrderWithoutIngredients401() {
        OrderRequestBody body = new OrderRequestBody();
        ValidatableResponse createOrderResponse = orderMethods.createOrder(body, "");
        int createOrderStatusCode = createOrderResponse.extract().statusCode();
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, createOrderStatusCode);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингридиентов - 401")
    public void createOrderWithInvalidIngredients401() {
        List<String> invalidIngredients = new ArrayList<>();
        invalidIngredients.add("867ahj4466kfl");
        invalidIngredients.add("920abc2677kad");
        OrderRequestBody body = new OrderRequestBody();
        body.setIngredients(invalidIngredients);
        ValidatableResponse createOrderResponse = orderMethods.createOrder(body, "");
        int createOrderStatusCode = createOrderResponse.extract().statusCode();
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, createOrderStatusCode);
    }
}