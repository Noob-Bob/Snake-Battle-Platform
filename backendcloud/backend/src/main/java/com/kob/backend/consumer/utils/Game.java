package com.kob.backend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;


import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * this Game class is used to manage the whole process of the game
 * multithreading`
 */
public class Game extends Thread {
  // game map related information
  final private Integer rows;
  final private Integer cols;
  final private Integer inner_wall_count;
  final private int[][] g; // game map
  private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
  // players related info
  private final Player playerA, playerB;

  // next operation of user A/B
  private Integer nextStepA = null;
  private Integer nextStepB = null;
  private final ReentrantLock lock = new ReentrantLock();
  private String status = "playing"; // play -> finish
  private String loser = ""; // "all": tie, "A": a lose, "B": b lose
  private final static String addBotUrl = "http://127.0.0.1:3002/bot/add/";
  public Game(
          Integer rows,
          Integer cols,
          Integer inner_wall_count,
          Integer idA,
          Bot botA,
          Integer idB,
          Bot botB
  ) {
    this.rows = rows;
    this.cols = cols;
    this.inner_wall_count = inner_wall_count;
    this.g = new int[rows][cols];

    Integer botIdA = -1, botIdB = -1;
    String botCodeA = "", botCodeB = "";
    if (botA != null) {
      botIdA = botA.getId();
      botCodeA = botA.getContent();
    }

    if (botB != null) {
      botIdB = botB.getId();
      botCodeB = botB.getContent();
    }
    playerA = new Player(idA, botIdA, botCodeA, rows - 2, 1, new ArrayList<>());
    playerB = new Player(idB, botIdB, botCodeB,1, cols - 2, new ArrayList<>());
  }

  public Player getPlayerA() {
    return playerA;
  }

  public Player getPlayerB() {
    return playerB;
  }

  /**
   * set the next operation of user A
   * @param nextStepA
   */
  public void setNextStepA(Integer nextStepA) {
    lock.lock();
    try {
      this.nextStepA = nextStepA;
    } finally {
      lock.unlock();
    }
  }
  /**
   * set the next operation of user B
   * @param nextStepB
   */
  public void setNextStepB(Integer nextStepB) {
    lock.lock();
    try {
      this.nextStepB = nextStepB;
    } finally {
      lock.unlock();
    }
  }

  /**
   * return the generated game map
   * @return a 2D int array, representing the game map for the two players
   */
  public int[][] getG() {
    return this.g;
  }

  /**
   * generate the game map
   * @return boolean variable, representing whether the map is valid
   */
  private boolean draw() {
    // step1: initialization
    for (int i = 0; i < this.rows; i ++) {
      for (int j = 0; j < this.cols; j ++) {
        g[i][j] = 0;
      }
    }

    // step2: draw the four edges
    for (int i = 0; i < this.rows; i ++) {
      g[i][0] = g[i][this.cols - 1] = 1;
    }
    for (int j = 0; j < this.cols; j ++) {
      g[0][j] = g[this.rows - 1][j] = 1;
    }

    // step3: draw the inner part of the map
    Random random = new Random();
    for (int i = 0; i < this.inner_wall_count / 2; i ++) {
      for (int j = 0; j < 1000; j ++) { // try drawing 1000 times
        int r = random.nextInt(this.rows); // generate a random number in range [0, this.rows)
        int c = random.nextInt(this.cols);
        if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1) continue; // no repeat
        if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2) continue; // valid start point
        g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1; // central symmetry
        break;
      }
    }
    return this.checkConnectivity(this.rows - 2, 1, 1, this.cols - 2);
  }

  /**
   * check whether the game map is connected
   * @param sx start position x coordinate
   * @param sy start position y coordinate
   * @param tx end position x coordinate
   * @param ty end position x coordinate
   * @return boolean, whether it is connected
   */
  private boolean checkConnectivity(int sx, int sy, int tx, int ty) {
    if (sx == tx && sy == ty) return true;
    g[sx][sy] = 1;
    for (int i = 0; i < 4; i ++) {
      int a = sx + dx[i], b = sy + dy[i];
      if (a < 0 || a >= this.rows || b < 0 || b >= this.cols) continue;
      if (g[a][b] == 0 && checkConnectivity(a, b, tx, ty)) {
        g[sx][sy] = 0;
        return true;
      }
    }
    g[sx][sy] = 0;
    return false;
  }

  /**
   * generate the game map
   * try 1000 times of draw function to get a valid map
   */
  public void createMap() {
    for (int i = 0; i < 1000; i ++) { // try drawing the map for 1000 times until we get a valid map
      if (draw()) {
        break;
      }
    }
  }

  private String getInput(Player player) { // encode the game into a string
    // _______#________#________#(__________)#_________#_______#(_________)
    // map       me.sx    me.sy    me.op     you.sx    you.sy   you.op
    Player me, you;
    if (playerA.getId().equals(player.getId())) {
      me = playerA;
      you = playerB;
    } else {
      me = playerB;
      you = playerA;
    }

    return getMapString() + "#" +
            me.getSx() + "#" +
            me.getSy() + "#(" +
            me.getStepsString() + ")#" +
            you.getSx() + "#" +
            you.getSy() + "#(" +
            you.getStepsString() + ")";
  }
  private void sendBotCode(Player player) {
    if (player.getBotId().equals(-1)) return; // human player
    MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
    data.add("user_id", player.getId().toString());
    data.add("bot_code", player.getBotCode());
    data.add("input", getInput(player));
    WebSocketServer.restTemplate.postForObject(addBotUrl, data, String.class);
  }

  /**
   * Get the next operations for the current two users
   *
   * TimeLimit = 10s
   * If we get operations from both of the users within the time limit, then update their steps array
   * else we return false
   * @return
   */
  private boolean nextStep() {
    try {
      Thread.sleep(200); // make sure frontend can render properly
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    sendBotCode(playerA);
    sendBotCode(playerB);

    for (int i = 0; i < 100; i ++) {
      try {
        Thread.sleep(100);
        lock.lock();
        try {
          if (nextStepA != null && nextStepB != null) {
            playerA.getSteps().add(nextStepA);
            playerB.getSteps().add(nextStepB);
            return true;
          }
        } finally {
          lock.unlock();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  /**
   * check whether the current state is valid or not
   * @param cellsA body of snake A
   * @param cellsB body of snake B
   * @return boolean
   */
  private boolean check_valid(List<Cell> cellsA, List<Cell> cellsB) {
    int n = cellsA.size();
    Cell cell = cellsA.get(n - 1); // head of the snake
    if (g[cell.x][cell.y] == 1) return false;

    for (int i = 0; i < n - 1; i ++) { // check self collision
      if (cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y) {
        return false;
      }
    }

    for (int i = 0; i < n - 1; i ++) { // check mutual collision
      if (cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y) {
        return false;
      }
    }

    return true;
  }

  /**
   * check whether the next operation of the two users are valid or not
   */
  private void judge() {
    List<Cell> cellsA = playerA.getCells();
    List<Cell> cellsB = playerB.getCells();

    boolean validA = check_valid(cellsA, cellsB);
    boolean validB = check_valid(cellsB, cellsA);

    if (!validA || !validB) {
      status = "finished";
      if (!validA && !validB) {
        loser = "all";
      } else if (!validA) {
        loser = "A";
      } else {
        loser = "B";
      }
    }
  }

  /**
   * send given message to both users
   * @param message the given message
   */
  private void sendAllMessage(String message) {
    if (WebSocketServer.users.get(playerA.getId()) != null)
      WebSocketServer.users.get(playerA.getId()).sendMessage(message);
    if (WebSocketServer.users.get(playerB.getId()) != null)
      WebSocketServer.users.get(playerB.getId()).sendMessage(message);
  }

  /**
   * send users' operation to the frontend, synchronizing
   */
  private void sendMove() { // broadcast movement info
    lock.lock();
    try {
      JSONObject resp = new JSONObject();
      resp.put("event", "move");
      resp.put("a_direction", nextStepA);
      resp.put("b_direction", nextStepB);
      sendAllMessage(resp.toJSONString());
      nextStepA = nextStepB = null;
    } finally {
      lock.unlock();
    }
  }

  private String getMapString() {
    StringBuilder res = new StringBuilder();
    for (int i = 0; i < rows; i ++) {
      for (int j = 0; j < cols; j ++) {
        res.append(g[i][j]);
      }
    }
    return res.toString();
  }
  // update rating
  private void updateUserRating(Player player, Integer rating) {
    User user = WebSocketServer.userMapper.selectById(player.getId());
    user.setRating(rating);
    WebSocketServer.userMapper.updateById(user);
  }
  private void saveToDatabase() {
    Integer ratingA = WebSocketServer.userMapper.selectById(playerA.getId()).getRating();
    Integer ratingB = WebSocketServer.userMapper.selectById(playerB.getId()).getRating();

    if ("A".equals(loser)) {
      ratingA -= 2;
      ratingB += 5;
    } else if ("B".equals(loser)) {
      ratingA += 5;
      ratingB -= 2;
    }
    updateUserRating(playerA, ratingA);
    updateUserRating(playerB, ratingB);
    Record record = new Record(
            null,
            playerA.getId(),
            playerA.getSx(),
            playerA.getSy(),
            playerB.getId(),
            playerB.getSx(),
            playerB.getSy(),
            playerA.getStepsString(),
            playerB.getStepsString(),
            getMapString(),
            loser,
            new Date()
    );
    WebSocketServer.recordMapper.insert(record);
  }

  /**
   * broadcast the result to the users
   */
  private void sendResult() {
    JSONObject resp = new JSONObject();
    resp.put("event", "result");
    resp.put("loser", loser);
    saveToDatabase();
    sendAllMessage(resp.toJSONString());
  }

  /**
   * entrance for the thread class
   */
  @Override
  public void run() {
    for (int i = 0; i < 1000; i ++) {
      if (nextStep()) {
        judge();
        if (status.equals("playing")) {
          sendMove();
        } else {
          sendResult();
          break;
        }
      } else {
        status = "finished";
        lock.lock();
        try {
          if (nextStepA == null && nextStepB == null) {
            loser = "all";
          } else if (nextStepA == null) {
            loser = "A";
          } else {
            loser = "B";
          }
        } finally {
          lock.unlock();
        }
        sendResult();
        break;
      }
    }
  }
}
