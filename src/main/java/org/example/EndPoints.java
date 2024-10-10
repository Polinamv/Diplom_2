package org.example;

public interface EndPoints {

    String POST_CREATE_USER = "api/auth/register";
    String POST_LOGIN = "api/auth/login";
    String DELETE_USER = "api/auth/user";
    String PATCH_USER_DATA = "api/auth/user";

    String GET_INGREDIENTS_DATA = "api/ingredients";
    String GET_USERS_ORDERS = "api/orders";
    String POST_CREATE_ORDER = "api/orders";
}