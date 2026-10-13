package com.api.endpoints;

import static io.restassured.RestAssured.given;

import com.api.base.BaseTest;
import com.api.models.Order;

import io.restassured.response.Response;

public class OrderApi {

	public static Response createOrder(String token, Order order) {
		return given().log().all().spec(BaseTest.jsonSpec(token)).body(order).when()
				.post("/api/ecom/order/create-order").then().log().all().extract().response();
	}
}
