package org.mule.extension.circuit.breaker.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import javax.inject.Inject;

import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.extension.circuit.breaker.internal.exceptions.OpenedCicuitException;
import org.mule.runtime.api.store.ObjectStoreManager;
import org.mule.runtime.extension.api.annotation.param.Config;


/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class CircuitBreakerOperations {	

	@Inject ObjectStoreManager runtimeObjectStoreManager;
  
	/**
	 * Record one more failure by saving it in Object Store
	 * @param configuration Circuit Breaker configuration
	 */
	@MediaType(value = ANY, strict = false)
	public void recordFailure(@Config CircuitBreakerConfiguration configuration) {
		CircuitObjectStoreHelper objectStore = new CircuitObjectStoreHelper(runtimeObjectStoreManager);
		Integer failureCount = 0;
		synchronized (failureCount) {
			failureCount = Integer.parseInt(objectStore.get("failureCount", "0", configuration.getBreakerName()));	
			objectStore.set("failureCount", String.valueOf(failureCount+1), configuration.getBreakerName());
		}
			
		if((failureCount + 1) == configuration.getThreshold()) {
			objectStore.set("openedAt", String.valueOf(System.currentTimeMillis()), configuration.getBreakerName());
		}
	}

	/**
	 * Filter the message if the failures' threshold has been met. In other words
	 * Circuit Breaker has been opened and no new messages are allowed to pass further.
	 * 
	 * @param configuration Circuit Breaker configuration
	 * @throws OpenedCicuitException The exception is thrown when the Circuit Breaker is opened
	 */
	@MediaType(value = ANY, strict = false)
	public void filter(@Config CircuitBreakerConfiguration configuration) throws OpenedCicuitException {
		CircuitObjectStoreHelper objectStore = new CircuitObjectStoreHelper(runtimeObjectStoreManager);
		long openedAt = Long.parseLong(objectStore.get("openedAt", "0", configuration.getBreakerName()));
		
		if(openedAt != 0 && (System.currentTimeMillis() - openedAt) > configuration.getTimeout()) {
			objectStore.set("openedAt", "0", configuration.getBreakerName());
			objectStore.set("failureCount", "0", configuration.getBreakerName());
		} else if (openedAt == 0) {
			// do nothing
		} else {
			throw new OpenedCicuitException();
		}
	}
}
