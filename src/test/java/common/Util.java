package common;

import com.jayway.restassured.response.Response;

import java.io.IOException;
import java.util.LinkedHashMap;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.*;

/**
 * Created by SourabhM on 21-01-2016.
 */
public class Util {
    public static String postWithHeaders(String endPoint, String path, LinkedHashMap headers, String query) {

        //System.out.println("in post--------------------------");
        System.out.println("in post--------------------------" + endPoint + path);
        long start = System.currentTimeMillis();
        Response response = given().headers(headers).contentType("application/json").body(query).when().post(endPoint + path).andReturn();
        long end = System.currentTimeMillis();
        long timeReq = end-start;

        String r = response.body().asString();
        System.out.println(r);

        return r;
    }

    public static String getWithHeaders(String endPoint, String path, LinkedHashMap headers) {
        long start = System.currentTimeMillis();
        Response response = given().headers(headers).contentType("application/json").when().get(endPoint + path).thenReturn();
        long end = System.currentTimeMillis();
        long timeReq = end-start;
        String r = response.body().asString();
        System.out.println(r);
        return r;
    }

    public static String putWithHeaders(String endPoint, String path, LinkedHashMap headers, String query) {
        long start = System.currentTimeMillis();
        Response response = given().headers(headers).and().contentType("application/json").and().body(query).when().put(endPoint + path).thenReturn();
        long end = System.currentTimeMillis();
        long timeReq = end-start;
        String r = response.body().asString();
        System.out.println("putWithHeaders-----------" + r);
        return r;
    }

    public static String putWithHeadersNobody(String endPoint, String path, LinkedHashMap headers) {
        long start = System.currentTimeMillis();
        Response response = given().headers(headers).contentType("application/json").when().put(endPoint + path).thenReturn();
        long end = System.currentTimeMillis();
        long timeReq = end-start;
        String r = response.body().asString();
        System.out.println(r);
        return r;
    }

    public static String deleteWithHeaders(String endPoint, String path, LinkedHashMap headers, String query) {
        long start = System.currentTimeMillis();
        Response response = given().headers(headers).contentType("application/json").and().body(query).when().delete(endPoint + path).thenReturn();
        long end = System.currentTimeMillis();
        long timeReq = end-start;
        String r = response.body().asString();
        System.out.println(r);
        return r;
    }

}
