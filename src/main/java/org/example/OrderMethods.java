package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.example.EndPoints.*;
import static org.example.RequestData.getRequestSpec;

public class OrderMethods {

    @Step("Получение данных об ингредиентах")
    public List<String> getIngredientsData() {
        return given()
                .spec(getRequestSpec())
                .get(GET_INGREDIENTS_DATA)
                .then()
                .extract()
                .as(IngredientsResponse.class)
                .getData()
                .stream()
                .map(Data::get_id)
                .collect(Collectors.toList());
    }

    @Step("Получение заказов пользователя")
    public ValidatableResponse getUsersOrders(String headerValue) {
        return given()
                .spec(getRequestSpec())
                .header("Authorization", headerValue)
                .get(GET_USERS_ORDERS)
                .then();
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrder(OrderRequestBody body, String headerValue) {
        return given()
                .spec(getRequestSpec())
                .header("Authorization", headerValue)
                .body(body)
                .post(POST_CREATE_ORDER)
                .then();
    }
}