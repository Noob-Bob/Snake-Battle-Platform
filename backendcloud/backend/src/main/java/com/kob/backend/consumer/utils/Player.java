package com.kob.backend.consumer.utils;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Play class is used to maintain the information of players
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
  private Integer id;
  private Integer botId; // -1: manually; else using bot
  private String botCode;
  private Integer sx;
  private Integer sy;
  private List<Integer> steps; // operation history

  /**
   * check whether the length of the snake is increasing or not for the current round
   * @param steps number of rounds
   * @return boolean
   */
  private boolean check_tail_increasing(int steps) { //
    if (steps <= 10) return true;
    return steps % 3 == 1;
  }

  /**
   * get the body cells of a snake
   * @return a list containing the cells
   */
  public List<Cell> getCells() {
    List<Cell> res = new ArrayList<>();

    int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    int x = sx, y = sy;
    res.add(new Cell(x, y));
    int step = 0;
    for (int d : steps) {
      x += dx[d];
      y += dy[d];
      res.add(new Cell(x, y));
      if (!check_tail_increasing(++ step)) {
        res.remove(0); // remove the tail
      }
    }
    return res;
  }

  public String getStepsString() {
    StringBuilder res = new StringBuilder();
    for (int d : steps) {
      res.append(d);
    }
    return res.toString();
  }
}
