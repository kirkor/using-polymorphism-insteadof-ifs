package com.mt.hybris.services.impl;

import org.springframework.stereotype.Component;

import com.mt.hybris.services.ServiceOnlyForSapMarkets;

@Component
public class GeolocationExampleServiceForSapMarketsImpl extends GeolocationExampleServiceImpl implements ServiceOnlyForSapMarkets {

	@Override
	public String sayHello() {
		return "Sap service";
	}

}
