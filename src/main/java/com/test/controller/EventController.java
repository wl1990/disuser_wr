package com.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.springevent.EventService;

@RestController
public class EventController {
	@Autowired
	private EventService eventService;
	
	@RequestMapping(value="/eventtest",method=RequestMethod.GET)
	public void eventTest(@RequestParam String name){
		eventService.register(name);
	}
}
