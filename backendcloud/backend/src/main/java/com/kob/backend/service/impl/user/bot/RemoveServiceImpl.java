package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.RemoveService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class RemoveServiceImpl implements RemoveService {
  @Autowired
  private BotMapper botMapper;
  @Override
  public Map<String, String> remove(Map<String, String> data) {
    // only the bot owner can remove the bot
    // so we need to get the current user
    UsernamePasswordAuthenticationToken authenticationToken =
            (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
    User user = loginUser.getUser();

    int bot_id = Integer.parseInt(data.get("bot_id"));
    Bot bot = botMapper.selectById(bot_id);
    Map<String, String> map = new HashMap<>();

    if (bot == null) {
      map.put("error_message", "Bot does not exist");
      return map;
    }

    if (!bot.getUserId().equals(user.getId())) {
      map.put("error_message", "Not authorized operation");
      return map;
    }

    botMapper.deleteById(bot_id);
    map.put("error_message", "success");
    return map;
  }
}
