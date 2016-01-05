package se.anviken.temperaturemonitor.persistance;

public interface PersistenceManager {

    
    public void doUpdateSensor(Sensor sensor);

	public void deleteSensor(Sensor sensor);

}
