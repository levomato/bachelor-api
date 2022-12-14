package com.ntlevinkerschberger;


import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import com.ntlevinkerschberger.models.Post;
import com.ntlevinkerschberger.repositories.PostRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;


import java.text.ParseException;
import java.util.List;

import static io.micronaut.http.HttpStatus.OK;
import static io.micronaut.http.HttpStatus.UNAUTHORIZED;
import static io.micronaut.http.MediaType.APPLICATION_JSON;
import static io.micronaut.http.MediaType.TEXT_PLAIN;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class JwtAuthenticationTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    PostRepository postRepository;

    @Test
    void accessingASecuredUrlWithoutAuthenticatingReturnsUnauthorized() {
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/posts").accept(APPLICATION_JSON));
        });

        assertEquals(UNAUTHORIZED, e.getStatus());
    }

    @Test
    void uponSuccessfulAuthenticationAJsonWebTokenIsIssuedToTheUser() throws ParseException {
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("sherlock", "password");
        HttpRequest<?> request = HttpRequest.POST("/login", creds);
        HttpResponse<BearerAccessRefreshToken> rsp = client.toBlocking().exchange(request, BearerAccessRefreshToken.class);
        assertEquals(OK, rsp.getStatus());
        BearerAccessRefreshToken bearerAccessRefreshToken = rsp.body();
        assertEquals("sherlock", bearerAccessRefreshToken.getUsername());
        assertNotNull(bearerAccessRefreshToken.getAccessToken());
        assertTrue(JWTParser.parse(bearerAccessRefreshToken.getAccessToken()) instanceof SignedJWT);

        String accessToken = bearerAccessRefreshToken.getAccessToken();
        HttpRequest<?> requestWithAuthorization = HttpRequest.GET("/posts")
                .accept(APPLICATION_JSON)
                .bearerAuth(accessToken);
        HttpResponse<String> response = client.toBlocking().exchange(requestWithAuthorization, String.class);
        assertEquals(OK, rsp.getStatus());

    }
}
