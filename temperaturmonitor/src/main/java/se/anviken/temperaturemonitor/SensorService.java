package se.anviken.temperaturemonitor;

import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import se.anviken.temperaturemonitor.persistance.Sensor;
 
@ManagedBean(name = "SensorService")
@ApplicationScoped
public class SensorService {
     
    public List<Sensor> createSensors(List<Sensor> sensors) {
		return sensors;

    }
}