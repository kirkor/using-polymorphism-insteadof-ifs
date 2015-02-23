package com.mt.hybris.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mt.hybris.context.Context;
import com.mt.hybris.services.ServiceOnlyForBaseStore;
import com.mt.hybris.services.ServiceOnlyForNonSapMarkets;
import com.mt.hybris.services.ServiceOnlyForSapMarkets;

/**
 * This intercepter is used to discover custom service for current session.
 * Selection is made based on context variables.
 * 
 * IMPORTANT! Every proxy has own instance of
 * {@link PolymorphicInterceptorForServices}, this is the reason of having
 * SCOPE_PROTOTYPE
 * 
 * @author Grzegorz.Bernas
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PolymorphicInterceptorForServices implements MethodInterceptor {

	// temp variable to faster lookup
	Object sapService = null;
	Object nonSapService = null;
	Object defaultService = null;
	Map<String, Object> baseStoreServices = Collections.synchronizedMap(new HashMap<String, Object>());

	// Already selected services
	private Map<StoreUuidAndServiceKey, Object> serviceMap = Collections.synchronizedMap(new HashMap<StoreUuidAndServiceKey, Object>());

	@Autowired
	private Context ctx; // TODO: prototype/session move to lookup-method?

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Object targetService = null;
		Class<? extends Object> invocatedType = methodInvocation.getMethod().getDeclaringClass();

		// unique key based on store uuid and service interface
		StoreUuidAndServiceKey proxyServiceKey = new StoreUuidAndServiceKey(ctx.baseStoreName, invocatedType);

		// check the already existing map of service implementations
		if (serviceMap.containsKey(proxyServiceKey)) {
			Object service = serviceMap.get(proxyServiceKey);

			return executeMethod(methodInvocation, service);
		}

		// check if we are talking about eco2 or hybris services
		String invocatedSimpleName = invocatedType.getCanonicalName();
		if (!invocatedSimpleName.startsWith("com.mt") && !invocatedSimpleName.startsWith("de.hybris")) {
			return methodInvocation.proceed();
		}

		targetService = lookupForImplementation(methodInvocation, invocatedType);

		// put service to map for faster lookup for next invocation
		serviceMap.put(proxyServiceKey, targetService);

		return executeMethod(methodInvocation, targetService);
	}

	private Object lookupForImplementation(MethodInvocation methodInvocation, Class<? extends Object> invocatedType) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		Map<String, ? extends Object> beansOfType = applicationContext.getBeansOfType(invocatedType);

		if (contextHasBeenSearched()) {
			return decideWhichServiceIsMostRelevant();
		} else if (beansOfType != null && beansOfType.size() > 1) {
			// one implementation, just proceed
			// run only once per interceptor instance
			for (Entry<String, ? extends Object> entry : beansOfType.entrySet()) {
				Object service = entry.getValue();

				if (isServiceOnlyForBaseStore(service)) {
					this.baseStoreServices.put(((ServiceOnlyForBaseStore) entry.getValue()).getBaseStoreUuid(), entry.getValue());

					continue;
				}

				if (isServiceOnlyForSapMarkets(service)) {
					this.sapService = entry.getValue();

					continue;
				}

				if (isServiceOnlyForNonSapMarkets(service)) {
					this.nonSapService = entry.getValue();

					continue;
				}
			}

		}

		this.defaultService = methodInvocation.getThis();

		return decideWhichServiceIsMostRelevant();
	}

	private Object decideWhichServiceIsMostRelevant() {
		if (this.baseStoreServices.containsKey(ctx.baseStoreName)) {
			return this.baseStoreServices.get(ctx.baseStoreName);
		}

		if (this.sapService != null && ctx.isSap != null && ctx.isSap) {
			return this.sapService;
		}

		if (this.nonSapService != null && ctx.isSap != null && !ctx.isSap) {
			return this.sapService;
		}

		return this.defaultService;
	}

	private boolean isServiceOnlyForNonSapMarkets(Object service) {
		return service instanceof ServiceOnlyForNonSapMarkets;
	}

	private boolean isServiceOnlyForSapMarkets(Object service) {
		return service instanceof ServiceOnlyForSapMarkets;
	}

	private boolean isServiceOnlyForBaseStore(Object service) {
		return service instanceof ServiceOnlyForBaseStore;
	}

	private boolean contextHasBeenSearched() {
		return this.defaultService != null || this.sapService != null || this.nonSapService != null || this.baseStoreServices.size() > 0;
	}

	private Object executeMethod(MethodInvocation methodInvocation, Object service) throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		Object result;
		Object[] arguments = methodInvocation.getArguments();
		Class[] argumentTypes = new Class[arguments.length];

		Method declaredMethod = service.getClass().getDeclaredMethod(methodInvocation.getMethod().getName(), argumentTypes);

		result = declaredMethod.invoke(service, arguments);
		return result;
	}

}
