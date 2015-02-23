package com.mt.hybris.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mt.hybris.context.Context;
import com.mt.hybris.services.GeolocationExampleService;

@RestController
public class MainController {

	@Autowired
	private GeolocationExampleService someService;

	@Autowired
	private Context ctx;

	@RequestMapping("/service/{baseStoreName}/{isSap}")
	public String index(@PathVariable("baseStoreName") String baseStoreName, @PathVariable("isSap") boolean isSap) {
		ctx.baseStoreName = baseStoreName;
		ctx.isSap = isSap;

		return someService.sayHello();
	}

	@RequestMapping("/resource")
	public Map<String, Object> home() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("id", UUID.randomUUID().toString());
		model.put("content", "Hello World");
		return model;
	}
}
