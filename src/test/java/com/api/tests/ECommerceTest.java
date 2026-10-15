package com.api.tests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.api.base.BaseTest;
import com.api.endpoints.AuthApi;
import com.api.endpoints.OrderApi;
import com.api.endpoints.ProductApi;
import com.api.models.LoginRequest;
import com.api.models.LoginResponse;
import com.api.models.Order;
import com.api.models.OrderDetail;
import com.api.utils.ConfigReader;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@Epic("ECommerce API")
@Feature("Order Flow")
public class ECommerceTest extends BaseTest {

	private String token;
	private String userId;
	private String productId;

	@Test
	@Story("Login")
	@Severity(SeverityLevel.BLOCKER)
	@Description("Login with valid credentials and capture token and userId")
	public void login() {
		LoginRequest request = new LoginRequest();
		request.setUserEmail(ConfigReader.get("user.email"));
		request.setUserPassword(ConfigReader.get("user.password"));

		Response response = AuthApi.login(request);
		Assert.assertEquals(response.statusCode(), 200);

		LoginResponse login = response.as(LoginResponse.class);
		token = login.getToken();
		userId = login.getUserId();
		Assert.assertNotNull(token, "Token should not be null");
	}

	@Test(dependsOnMethods = "login")
	@Story("Product")
	@Severity(SeverityLevel.CRITICAL)
	@Description("Add a product with an image and capture productId")
	public void addProduct() {
		File image = new File(ConfigReader.get("product.image.path"));
		Response response = ProductApi.addProduct(token, userId, image);
		Assert.assertEquals(response.statusCode(), 201);

		productId = new JsonPath(response.asString()).getString("productId");
		Assert.assertNotNull(productId, "productId should not be null");
	}

	@Test(dependsOnMethods = "addProduct")
	@Story("Order")
	@Severity(SeverityLevel.CRITICAL)
	@Description("Create an order for the product added in the previous step")
	public void createOrder() {
		OrderDetail detail = new OrderDetail();
		detail.setCountry("India");
		detail.setProductOrderedId(productId);

		List<OrderDetail> details = new ArrayList<>();
		details.add(detail);

		Order order = new Order();
		order.setOrders(details);

		Response response = OrderApi.createOrder(token, order);
		Assert.assertEquals(response.statusCode(), 201);
		Assert.assertEquals(new JsonPath(response.asString()).getString("message"), "Order Placed Successfully");
	}

	@Test(dependsOnMethods = "createOrder")
	@Story("Product")
	@Severity(SeverityLevel.NORMAL)
	@Description("Delete the product created earlier and verify the message")
	public void deleteProduct() {
		Response response = ProductApi.deleteProduct(token, productId);
		Assert.assertEquals(response.statusCode(), 200);
		Assert.assertEquals(new JsonPath(response.asString()).getString("message"), "Product Deleted Successfully");
	}
}