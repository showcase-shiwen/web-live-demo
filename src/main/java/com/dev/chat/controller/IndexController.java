package com.dev.chat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class IndexController {
    @Autowired
    private RedisTemplate redisTemplate;

    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = {"index", "/"})
    public String index() {
        redisTemplate.opsForValue().set("test", "test");
        redisTemplate.delete("test");
        logger.info("index view");
        return "index";
    }

    /**
     * 直播页面
     *
     * @param modelMap
     * @return
     */
    @GetMapping("webBroadcast")
    public ModelAndView webBroadcast(ModelMap modelMap) {
        modelMap.put("roomId", UUID.randomUUID().toString().replaceAll("-", ""));
        return new ModelAndView("/broadcast", modelMap);
    }

    /**
     * 观看页面
     *
     * @param modelMap
     * @return
     */
    @GetMapping("webWatch")
    public ModelAndView webWatch(String roomId, ModelMap modelMap) {
        modelMap.put("roomId", roomId);
        return new ModelAndView("/watch", modelMap);
    }

}
