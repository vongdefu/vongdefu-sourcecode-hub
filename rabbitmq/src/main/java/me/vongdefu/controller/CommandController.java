package me.vongdefu.controller;


import me.vongdefu.service.CommandService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/11
 */
@RestController
@RequestMapping("/command")
public class CommandController {

    @Resource
    private CommandService commandService;

    @GetMapping("/produceOneMsg")
    public void produceOneMsg() {
        commandService.produceOneMsg();
    }

    @GetMapping("/produceNMsg")
    public void produceNMsg() {
        commandService.produceNMsg();
    }


}
