package com.tinawu.springSecuritybase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin
@RestController
    @RequestMapping("googleOauth2")
public class GoogleOAuthController {


    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @RequestMapping("/userinfo")
    public String userinfo(OAuth2AuthenticationToken authentication) {
        // authentication.getAuthorizedClientRegistrationId() returns the
        // registrationId of the Client that was authorized during the Login flow
        OAuth2AuthorizedClient authorizedClient =
                this.authorizedClientService.loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        System.out.println(accessToken);

        return "userinfo";
    }

    @GetMapping("/user/me")
    public Map<String, Object> userDetails(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        System.out.println("進來了");
        return oAuth2AuthenticationToken.getPrincipal().getAttributes();
    }
}
