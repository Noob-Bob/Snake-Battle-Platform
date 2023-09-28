package com.kob.matchingsystem.service;

public interface MatchingService {
  /**
   * add a user to the matching pool
   * @param userId
   * @param rating
   * @param botId
   * @return
   */
  String addPlayer(Integer userId, Integer rating, Integer botId);

  /**
   * remove a user from the matching pool
   * @param userId
   * @return
   */
  String removePlayer(Integer userId);
}
