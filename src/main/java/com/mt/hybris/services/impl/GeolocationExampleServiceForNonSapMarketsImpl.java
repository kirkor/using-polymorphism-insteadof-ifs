package com.mt.hybris.services.impl;

import org.springframework.stereotype.Component;

import com.mt.hybris.services.ServiceOnlyForNonSapMarkets;
import com.mt.hybris.services.GeolocationExampleService;

@Component
public class GeolocationExampleServiceForNonSapMarketsImpl implements ServiceOnlyForNonSapMarkets, GeolocationExampleService {
	@Override
	public String sayHello() {
		return "Non sap service";
	}
}
