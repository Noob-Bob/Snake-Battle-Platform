package com.kob.backend.consumer.utils;

import java.util.Random;

public class Game {
  final private Integer rows;
  final private Integer cols;
  final private Integer inner_wall_count;

  final private int[][] g;
  private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

  public Game(Integer rows, Integer cols, Integer inner_wall_count) {
    this.rows = rows;
    this.cols = cols;
    this.inner_wall_count = inner_wall_count;
    this.g = new int[rows][cols];
  }

  public int[][] getG() {
    return this.g;
  }

  private boolean draw() {
    for (int i = 0; i < this.rows; i ++) {
      for (int j = 0; j < this.cols; j ++) {
        g[i][j] = 0;
      }
    }

    for (int i = 0; i < this.rows; i ++) {
      g[i][0] = g[i][this.cols - 1] = 1;
    }

    for (int j = 0; j < this.cols; j ++) {
      g[0][j] = g[this.rows - 1][j] = 1;
    }
    Random random = new Random();
    for (int i = 0; i < this.inner_wall_count / 2; i ++) {
      for (int j = 0; j < 1000; j ++) {
        int r = random.nextInt(this.rows);
        int c = random.nextInt(this.cols);
        if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1) continue; // no repeat
        if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2) continue; // valid start point
        g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
        break;
      }
    }

    return this.checkConnectivity(this.rows - 2, 1, 1, this.cols - 2);
  }

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

  public void createMap() {
    for (int i = 0; i < 1000; i ++) {
      if (draw()) {
        break;
      }
    }
  }
}
