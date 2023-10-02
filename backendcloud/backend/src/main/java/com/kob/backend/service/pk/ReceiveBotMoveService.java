package com.kob.backend.service.pk;

/**
 * receive the result from bot running system
 */
public interface ReceiveBotMoveService {
  String receiveBotMove(Integer userId, Integer direction);
}
