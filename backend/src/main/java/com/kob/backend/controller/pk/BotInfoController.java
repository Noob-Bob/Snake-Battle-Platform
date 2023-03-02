package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/pk/")
public class BotInfoController {
  @RequestMapping("getbotinfo/")
  public Map<String, Integer> getBotInfo() {
    Map<String, Integer> map = new HashMap<>();
    map.put("sword", 3800);
    map.put("axe", 10);
    map.put("shield", 15);
    return map;
  }
}
