package se.anviken.temperaturemonitor.persistance;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class PersistenceManagerBean implements PersistenceManager {

	@Inject
	private EntityManager em;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void doUpdateSensor(Sensor sensor) {
		em.merge(sensor);
		em.flush();
		
	}	
}
