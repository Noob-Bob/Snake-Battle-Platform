package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Override
  public Map<String, String> register(String username, String password, String confirmPassword) {
    Map<String, String> map = new HashMap<>();
    if (username == null) {
      map.put("error message", "Username cannot be empty");
      return map;
    }
    if (password == null || confirmPassword == null) {
      map.put("error message", "Password cannot be empty");
      return map;
    }
    username = username.trim();
    if (username.length() == 0) {
      map.put("error message", "Username cannot be empty");
      return map;
    }
    if (username.length() > 100) {
      map.put("error message", "The length of username should not be greater than 100");
      return map;
    }
    if (password.length() == 0 || confirmPassword.length() == 0) {
      map.put("error message", "Password should not be empty");
      return map;
    }
    if (password.length() > 100 || confirmPassword.length() > 100) {
      map.put("error message", "The length of password should not be greater than 100");
      return map;
    }
    if (!password.equals(confirmPassword)) {
      map.put("error message", "Passwords are different");
      return map;
    }
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("username", username);
    List<User> users = userMapper.selectList(queryWrapper);
    if (!users.isEmpty()) {
      map.put("error message", "Username existed");
      return map;
    }

    String encodedPassword = passwordEncoder.encode(password);
    String photo = "https://cdn.acwing.com/media/user/profile/photo/46915_lg_2f2af038e1.jpg";
    User user = new User(null, username, encodedPassword, photo);
    userMapper.insert(user);
    map.put("error message", "success");
    return map;
  }
}
