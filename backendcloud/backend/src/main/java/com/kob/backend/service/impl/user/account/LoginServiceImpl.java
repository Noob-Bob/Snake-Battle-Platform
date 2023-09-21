package com.kob.backend.service.impl.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
  @Autowired
  private AuthenticationManager authenticationManager;
  @Override
  public Map<String, String> getToken(String username, String password) {
    // STEP 1
    // encapsulate username and password
    // note that this will get encoded password
    UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(username, password);
    // STEP 2
    // then use authenticationManager to do the authentication
    // if error occur, this will throw an exception
    Authentication authenticate = authenticationManager.authenticate(authenticationToken);

    // STEP 3
    // obtain the specific user
    UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
    User user = loginUser.getUser();


    // Create a jwt-token
    String jwt = JwtUtil.createJWT(user.getId().toString());
    // Note that this should correspond to the frontend
    Map<String, String> map = new HashMap<>();
    map.put("error_message", "success");
    map.put("token", jwt);

    return map;
  }
}
