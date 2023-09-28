package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Cell is the unit of the snake body
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cell {
  int x, y;
}
