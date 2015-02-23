package com.mt.hybris.services.impl;

import org.springframework.stereotype.Component;

import com.mt.hybris.services.GeolocationExampleService;

@Component(value = "defaultGeolocationService")
public class GeolocationExampleServiceImpl implements GeolocationExampleService {

	@Override
	public String sayHello() {
		return "Default";
	}

}
