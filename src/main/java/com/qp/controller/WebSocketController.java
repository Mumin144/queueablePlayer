package com.qp.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
	
	@MessageMapping("/reciever")
    @SendTo("/remote/player")
    public String greeting(String msg) throws Exception {
		//Message message = new Message(msg); 
		System.out.println(msg);
        return msg;
    }
}
