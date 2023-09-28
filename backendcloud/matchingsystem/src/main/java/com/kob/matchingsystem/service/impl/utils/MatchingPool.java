package com.kob.matchingsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MatchingPool extends Thread {
  private static List<Player> players = new ArrayList<>(); // players in the waiting list
  private final ReentrantLock lock = new ReentrantLock();
  private static RestTemplate restTemplate;
  private static final String startGameUrl = "http://127.0.0.1:3000/pk/start/game/";

  @Autowired
  public void setRestTemplate(RestTemplate restTemplate) {
    MatchingPool.restTemplate = restTemplate;
  }

  /**
   * add a user to the matching pool
   * @param userId id of the user
   * @param rating rating of the user
   * @param botId botId of the user
   */
  public void addPlayer(Integer userId, Integer rating, Integer botId) {
    lock.lock(); // deal with write-write conflict for the players array
    try {
      players.add(new Player(userId, rating, botId, 0));
    } finally {
      lock.unlock();
    }
  }

  /**
   * remove a user from the matching pool
   * @param userId id of the user
   */
  public void removePlayer(Integer userId) {
    lock.lock();
    try {
      List<Player> newPlayers = new ArrayList<>();
      for (Player player : players) {
        if (!player.getUserId().equals(userId)) {
          newPlayers.add(player);
        }
      }
      players = newPlayers;
    } finally {
      lock.unlock();
    }
  }

  /**
   * add the waiting time for all users in the matching pool
   */
  private void increaseWaitingTime() {
    for (Player player : players) {
      player.setWaitingTime(player.getWaitingTime() + 1);
    }
  }

  /**
   * check whether player a and player b can be matched
   * @param a player a
   * @param b player b
   * @return boolean
   */
  private boolean checkMatched(Player a, Player b) {
    int ratingDelta = Math.abs(a.getRating() - b.getRating());
    int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
    return ratingDelta <= 10 * waitingTime;
  }

  /**
   * send the result of matching a and b
   * @param a player A
   * @param b player B
   */
  private void sendResult(Player a, Player b) {
    MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
    data.add("a_id", a.getUserId().toString());
    data.add("a_bot_id", a.getBotId().toString());
    data.add("b_id", b.getUserId().toString());
    data.add("b_bot_id", b.getBotId().toString());
    restTemplate.postForObject(startGameUrl, data, String.class);
  }

  /**
   * try to match players
   */
  private void matchPlayers() {
    boolean[] used = new boolean[players.size()];
    for (int i = 0; i < players.size(); i ++) {
      if (used[i]) continue;
      for (int j = i + 1; j < players.size(); j ++) {
        if (used[j]) continue;
        Player a = players.get(i), b = players.get(j);
        if (checkMatched(a, b)) {
          used[i] = used[j] = true;
          sendResult(a, b);
          break;
        }
      }
    }
    List<Player> newPlayers = new ArrayList<>();
    for (int i = 0; i < players.size(); i ++) {
      if (!used[i]) {
        newPlayers.add(players.get(i));
      }
    }
    players = newPlayers;
  }
  @Override
  public void run() {
    while (true) {
      try {
        Thread.sleep(1000);
        lock.lock(); // increasing and match all modify players list, so we need a lock
        try {
          increaseWaitingTime();
          matchPlayers();
        } finally {
          lock.unlock();
        }
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
