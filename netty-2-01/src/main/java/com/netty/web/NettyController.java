package com.netty.web;

import com.netty.server.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping(value = "nettyServer")
@RestController

public class NettyController {
    @Autowired
    private NettyServer nettyServer;

    @RequestMapping("nettyAddress")
    public String localAddress() {
        return "netty localAddress" + nettyServer.getChannel().localAddress();
    }

    @RequestMapping("isOpen")
    public String isOpen() {
        return "nettyServer is open" + nettyServer.getChannel().isOpen();
    }
}
