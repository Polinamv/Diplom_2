package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserMethods extends RequestData implements EndPoints {

    @Step("Создание нового пользователя")
    public ValidatableResponse create(UserRequestBody user) {
        return given()
                .spec(getRequestSpec())
                .body(user)
                .post(POST_CREATE_USER)
                .then();
    }

    @Step("Логин пользователя в системе")
    public ValidatableResponse login(UserCredentials credentials, String headerValue) {
        return given()
                .spec(getRequestSpec())
                .header("Authorization", headerValue)
                .body(credentials)
                .post(POST_LOGIN)
                .then();
    }

    @Step("Удаление пользователя")
    public void delete(String headerValue) {
        given()
                .spec(getRequestSpec())
                .header("Authorization", headerValue)
                .when()
                .delete(DELETE_USER);
    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse patch(String headerValue, String body) {
        return given()
                .spec(getRequestSpec())
                .header("Authorization", headerValue)
                .body(body)
                .when()
                .patch(PATCH_USER_DATA)
                .then();
    }
}