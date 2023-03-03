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
  public Map<String, String> getBotInfo() {
    Map<String, String> bot1 = new HashMap<>();
    bot1.put("name", "axe");
    bot1.put("score", "1500");
    return bot1;
  }
}
