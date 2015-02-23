package com.mt.hybris.config;

final class StoreUuidAndServiceKey {

	private final String baseStoreUuid;
	private final Object serviceInterface;

	public StoreUuidAndServiceKey(String baseStoreUuid, Object serviceInterface) {
		this.baseStoreUuid = baseStoreUuid;
		this.serviceInterface = serviceInterface;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseStoreUuid == null) ? 0 : baseStoreUuid.hashCode());
		result = prime * result + ((serviceInterface == null) ? 0 : serviceInterface.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StoreUuidAndServiceKey other = (StoreUuidAndServiceKey) obj;
		if (baseStoreUuid == null) {
			if (other.baseStoreUuid != null)
				return false;
		} else if (!baseStoreUuid.equals(other.baseStoreUuid))
			return false;
		if (serviceInterface == null) {
			if (other.serviceInterface != null)
				return false;
		} else if (!serviceInterface.getClass().getSimpleName().equals(other.serviceInterface.getClass().getSimpleName()))
			return false;
		return true;
	}

}