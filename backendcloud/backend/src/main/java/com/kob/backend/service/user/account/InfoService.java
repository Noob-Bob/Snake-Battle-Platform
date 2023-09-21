package com.kob.backend.service.user.account;

import java.util.Map;

/**
 * fetch user information after user login
 */
public interface InfoService {
  public Map<String, String> getInfo();
}
