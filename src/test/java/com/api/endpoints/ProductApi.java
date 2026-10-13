package com.api.endpoints;

import static io.restassured.RestAssured.given;

import java.io.File;

import com.api.base.BaseTest;

import io.restassured.response.Response;

public class ProductApi {

	public static Response addProduct(String token, String userId, File image) {
		return given().log().all().spec(BaseTest.authSpec(token)).param("productName", "Laptop")
				.param("productAddedBy", userId).param("productCategory", "fashion")
				.param("productSubCategory", "shirts").param("productPrice", "11500")
				.param("productDescription", "Lenova").param("productFor", "men").multiPart("productImage", image)
				.when().post("/api/ecom/product/add-product").then().log().all().extract().response();
	}

	public static Response deleteProduct(String token, String productId) {
		return given().log().all().spec(BaseTest.jsonSpec(token)).pathParam("productId", productId).when()
				.delete("/api/ecom/product/delete-product/{productId}").then().log().all().extract().response();
	}
}
