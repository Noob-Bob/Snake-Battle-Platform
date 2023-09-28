package com.kob.backend.service.pk;

/**
 * Receive the matching result from matching system and then start a new game
 */
public interface StartGameService {
  String startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId);
}
