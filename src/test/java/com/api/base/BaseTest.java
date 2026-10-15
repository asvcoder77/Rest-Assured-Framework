package com.api.base;

import com.api.utils.ConfigReader;

import io.qameta.allure.restassured.AllureRestAssured;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class BaseTest {

	protected static String baseUrl;

	@BeforeClass(alwaysRun = true)
	public void setUp() {
		baseUrl = ConfigReader.get("base.url");
		RestAssured.baseURI = baseUrl;
		// Attach every request/response to the Allure report
		RestAssured.replaceFiltersWith(new AllureRestAssured());
	}

	/** JSON request with the auth token header. */
	public static RequestSpecification jsonSpec(String token) {
		return new RequestSpecBuilder().setBaseUri(baseUrl).addHeader("authorization", token)
				.setContentType(ContentType.JSON).build();
	}

	/** JSON request without auth (used for login). */
	public static RequestSpecification jsonSpec() {
		return new RequestSpecBuilder().setBaseUri(baseUrl).setContentType(ContentType.JSON).build();
	}

	/** Auth header only, no content type (multipart uploads set their own). */
	public static RequestSpecification authSpec(String token) {
		return new RequestSpecBuilder().setBaseUri(baseUrl).addHeader("authorization", token).build();
	}
}
