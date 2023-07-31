package com.kob.backend.service.record;

import com.alibaba.fastjson.JSONObject;

public interface GetRecordListService {
  /**
   *
   * @param page index of the page
   * @return Json object
   */
  JSONObject getList(Integer page);
}
