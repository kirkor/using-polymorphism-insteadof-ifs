package com.mt.hybris.services.impl;

import org.springframework.stereotype.Component;

import com.mt.hybris.services.ServiceOnlyForBaseStore;

@Component
public class GeolocationExampleServiceForUKStoreImpl extends GeolocationExampleServiceImpl implements ServiceOnlyForBaseStore {

	@Override
	public String sayHello() {
		return "Shop specyfic service";
	}

	@Override
	public String getBaseStoreUuid() {
		return "UK_BASE_STORE";
	}

}
