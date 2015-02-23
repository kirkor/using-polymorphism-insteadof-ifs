package com.mt.hybris.services;

/**
 * All services dedicated to one store need to implement this service
 * 
 * @author Grzegorz.Bernas
 */
public interface ServiceOnlyForBaseStore {
	/**
	 * return unique uuid of BaseStore
	 * 
	 * @return
	 */
	public String getBaseStoreUuid();
}
