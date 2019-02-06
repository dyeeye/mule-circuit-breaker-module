package org.mule.extension.circuit.breaker.internal;

import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.param.Parameter;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */
@Operations(CircuitBreakerOperations.class)
public class CircuitBreakerConfiguration {
	
  /**
   * Number of failures that cause Circuit Breaker to open
   */
  @Parameter
  private int threshold;
  
  /**
   * Period of time when the Circuit Breaker stays open before
   * it comes back to closed state.
   */
  @Parameter
  private int timeout;
  
  /**
   * Unique name of the Circuit Breaker
   */
  @Parameter
  private String breakerName;
  
  public void setThreashold(int threshold) {
	  this.threshold = threshold;
  }
  
  public void setTimeout(int timeout) {
	  this.timeout = timeout;
  }
  
  public void setBreakerName(String name) {
	  this.breakerName = name;
  }
  
  public int getThreshold() {
	  return this.threshold;
  }
  
  public int getTimeout() {
	  return this.timeout;
  }
  
  public String getBreakerName() {
	  return this.breakerName;
  }
}
