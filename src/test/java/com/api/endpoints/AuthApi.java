package com.api.endpoints;

import static io.restassured.RestAssured.given;

import com.api.base.BaseTest;
import com.api.models.LoginRequest;

import io.restassured.response.Response;

public class AuthApi {

	public static Response login(LoginRequest body) {
		return given().log().all().spec(BaseTest.jsonSpec()).body(body).when().post("/api/ecom/auth/login").then()
				.log().all().extract().response();
	}
}
