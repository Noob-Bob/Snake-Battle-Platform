package com.kob.backend.service.user.account;

import java.util.Map;

/**
 * new user registration
 */
public interface RegisterService {
  public Map<String, String> register(String username, String password, String confirmPassword);
}
