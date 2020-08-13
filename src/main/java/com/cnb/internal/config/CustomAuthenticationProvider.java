package com.cnb.internal.config;

import com.cnb.internal.dtos.UserDto;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.cnb.internal.config.Constants.BASE_URL;
import static com.cnb.internal.config.Constants.LOGIN_URL;

@Configuration
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private String url = BASE_URL+LOGIN_URL;

    public CustomAuthenticationProvider(){}

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        //System.out.println("Email received:"+email);
        //System.out.println("password received:"+ password);
        RestTemplate restTemplate = new RestTemplate();
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setPassword(password);
        userDto.setRole("internal");
        ResponseEntity<UserDto> response;
        try {
            response = restTemplate.postForEntity(url, userDto, UserDto.class);
        }catch (HttpClientErrorException e){
                throw new BadCredentialsException("User name or password invalid"+e.getMessage());
        }
        //assert responseUser != null;
        UserDto responseUser = response.getBody();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(responseUser,authentication.getCredentials(), authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
