/**
 *
 */
package se.anviken.temperaturemonitor.persistance;

public interface PersistenceFacade {


	public void updateSensor(Sensor sensor); 
	public void deleteSensor(Sensor sensor); 


}
