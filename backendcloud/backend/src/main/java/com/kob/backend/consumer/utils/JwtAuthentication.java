package com.kob.backend.consumer.utils;

import com.kob.backend.utils.JwtUtil;

import io.jsonwebtoken.Claims;

/**
 * whether we can parse the userId from given token
 * if so, we return the userId
 * else we return -1 - meaning failure
 */
public class JwtAuthentication {
  public static Integer getUserId(String token) {
    Integer userId = -1;
    try {
      Claims claims = JwtUtil.parseJWT(token);
      userId = Integer.parseInt(claims.getSubject());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return userId;
  }
}
