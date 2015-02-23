package com.mt.hybris.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mt.hybris.context.Context;
import com.mt.hybris.services.GeolocationExampleService;

@RestController
public class MainController {

	@Autowired
	private GeolocationExampleService geolocationExample;

	@Autowired
	private Context ctx;

	@RequestMapping("/service/geolocation")
	public String index(@RequestParam(value = "baseStoreName", required = false) String baseStoreName,
			@RequestParam(value = "isSap", required = false, defaultValue = "") Boolean isSap) {
		ctx.baseStoreName = baseStoreName;
		ctx.isSap = isSap;

		return geolocationExample.sayHello();
	}

	@RequestMapping("/resource")
	public Map<String, Object> home() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("id", UUID.randomUUID().toString());
		model.put("content", "Hello World");
		return model;
	}
}
