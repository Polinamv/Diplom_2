import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.example.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@DisplayName("Создание заказа с авторизацией")
public class CreateOrderAuthTests {

    final OrderMethods orderMethods = new OrderMethods();
    final UserMethods userMethods = new UserMethods();
    final UserRequestBody user = new UserRequestBody("abcd@moll.com", "123123", "Polina");

    String headerValue;

    @Before
    public void setUp() {
        ValidatableResponse createUserResponse = userMethods.create(user);
        headerValue = createUserResponse.extract().path("accessToken");
        userMethods.login(UserCredentials.from(user), headerValue);
    }

    @After
    public void clearUp() {
        userMethods.delete(headerValue);
    }

    @Test
    @DisplayName("Создание заказа с ингридиентами - 200")
    public void createOrder200() {
        OrderRequestBody body = new OrderRequestBody(orderMethods.getIngredientsData());
        ValidatableResponse createOrderResponse = orderMethods.createOrder(body, headerValue);
        int createOrderStatusCode = createOrderResponse.extract().statusCode();
        Assert.assertEquals(HttpStatus.SC_OK, createOrderStatusCode);
        boolean isSuccess = createOrderResponse.extract().path("success");
        Assert.assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Создание заказа без ингридиентов - 400")
    public void createOrder400() {
        OrderRequestBody body = new OrderRequestBody();
        ValidatableResponse createOrderResponse = orderMethods.createOrder(body, headerValue);
        int createOrderStatusCode = createOrderResponse.extract().statusCode();
        Assert.assertEquals(HttpStatus.SC_BAD_REQUEST, createOrderStatusCode);
        boolean isSuccess = createOrderResponse.extract().path("success");
        Assert.assertFalse(isSuccess);
        String errorMessage = createOrderResponse.extract().path("message");
        Assert.assertEquals("Ingredient ids must be provided", errorMessage);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингридиентов - 500")
    public void createOrder500() {
        List<String> invalidIngredients = new ArrayList<>();
        invalidIngredients.add("867ahj4466kfl");
        invalidIngredients.add("920abc2677kad");
        OrderRequestBody body = new OrderRequestBody();
        body.setIngredients(invalidIngredients);
        ValidatableResponse createOrderResponse = orderMethods.createOrder(body, headerValue);
        int createOrderStatusCode = createOrderResponse.extract().statusCode();
        Assert.assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, createOrderStatusCode);
    }
}