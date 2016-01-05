package se.anviken.temperaturemonitor.persistance;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

@Default
@ApplicationScoped
public class PersistenceFacadeBean implements PersistenceFacade {

    @Inject
    PersistenceManager persistenceManager;
	@Override
	public void updateSensor(Sensor sensor) {
		persistenceManager.doUpdateSensor(sensor);
		
	}
	@Override
	public void deleteSensor(Sensor sensor) {
		persistenceManager.deleteSensor(sensor);
		
	}
	@Override
	public void addSensor(Sensor sensor) {
		persistenceManager.doAddSensor(sensor);
		
	}
}
