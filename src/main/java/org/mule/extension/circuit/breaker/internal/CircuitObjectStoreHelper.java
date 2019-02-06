package org.mule.extension.circuit.breaker.internal;

import org.mule.runtime.api.store.ObjectStore;
import org.mule.runtime.api.store.ObjectStoreException;
import org.mule.runtime.api.store.ObjectStoreManager;

/**
 * 
 * @author Patryk <patryk.bandurski@gmail.com>
 * Class unifying a way to get or set value within object store
 *
 */
public class CircuitObjectStoreHelper {
	
	private ObjectStore<String> os;
	
	public CircuitObjectStoreHelper(ObjectStoreManager osm) {
		os = osm.getDefaultPartition();
	}
	
	/**
	 * Get synchronously property from the Object Store for given circuit
	 * @param propertyName Name of the property to extract
	 * @param defaultValue Default value to return in case no value found
	 * @param circuitName Circuit Breaker's name
	 * @return
	 */
	synchronized public String get(String propertyName, String defaultValue, String circuitName) {
		
		String value = defaultValue;
		try {
			value = os.retrieve(String.format("%s.%s", circuitName, propertyName));
		} catch (ObjectStoreException e) {
		}
		
		return value;
	}
	
	/**
	 * Synchronously set new property into the Object Store for given Circuit
	 * @param propertyName Name of the property to insert
	 * @param value Value related to the property
	 * @param circuitName Circuit Breaker's name
	 */
	synchronized public void set(String propertyName, String value, String circuitName) {
		String key = String.format("%s.%s", circuitName, propertyName);
		
		try {
			if(os.contains(key)) os.remove(key);
			
			os.store(key, value);
		} catch (ObjectStoreException e) {
		}
	}
}
