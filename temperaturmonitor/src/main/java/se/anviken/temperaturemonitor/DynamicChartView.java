package se.anviken.temperaturemonitor;

import javax.annotation.PostConstruct;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartSeries;

import se.anviken.temperaturemonitor.persistance.Sensor;
import se.anviken.temperaturemonitor.persistance.Temperature;

@ManagedBean
public class DynamicChartView implements Serializable {

	private static final long serialVersionUID = -9182840560176521158L;
	private LineChartModel dateModel;

	@Inject
	private EntityManager em;
	
    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyy-MM-dd HH:mm");
	private DateTime startDate;
	private DateTime endDate;
    
	@PostConstruct
	public void init() {
		createDateModel();
	}

	public LineChartModel getDateModel() {
		return dateModel;
	}

	private void createDateModel() {
		dateModel = new LineChartModel();
			
		setInterval(Period.hours(1));
		addSerie(14);
		
		dateModel.setAnimate(true);
		dateModel.setDatatipFormat("%s %.1f");
		dateModel.setLegendPosition("e");
		dateModel.setShowDatatip(true);
		
		dateModel.setTitle("Temperaturgraf");
		dateModel.getAxis(AxisType.Y).setLabel("Temperatur");
		DateAxis axis = new DateAxis("Tidpunkt");
		axis.setTickAngle(-50);
		axis.setMin(DATE_FORMAT.format(startDate.toDate()));
		axis.setMax(DATE_FORMAT.format(endDate.toDate()));
		axis.setTickFormat("%Y-%m-%d %H:%M");

		dateModel.getAxes().put(AxisType.X, axis);
	}

	private void setInterval(Period period) {
		endDate = new DateTime();
		startDate = endDate.minus(period);
	}

	private void addSerie(int sensorId) {
		LineChartSeries series1 = new LineChartSeries();
		TypedQuery<Sensor> sensorQuery = em.createNamedQuery("Sensor.findWithId",Sensor.class);
		sensorQuery.setParameter("sensorId", sensorId);
		Sensor sensor = sensorQuery.getSingleResult();
				
		series1.setLabel(sensor.getName());
		TypedQuery<Temperature> tempQuery = em.createNamedQuery("Temperature.ForSensor",Temperature.class);
		tempQuery.setParameter("sensorId", sensorId);
		tempQuery.setParameter("startDate", startDate.toDate() );
		tempQuery.setParameter("endDate", endDate.toDate());
		List<Temperature> list = tempQuery.getResultList();
		
		for(Temperature temperature:list){
			series1.set(DATE_FORMAT.format(temperature.getTempTimestamp()),temperature.getTemperature());
		}
		series1.setShowMarker(false);
		dateModel.addSeries(series1);
	}
	
	private List<String> selectedOptions;
	private List<Sensor> sensorList;
    
    public List<String> getSelectedOptions() {
        return selectedOptions;
    }
    public List<String> getSensorNames() {
    	if(sensorList==null){
    		TypedQuery<Sensor> sensorQuery = em.createNamedQuery("Sensor.findAll",Sensor.class);
    		sensorList = sensorQuery.getResultList();
    	}
    	List<String> stringList = new ArrayList<String>();
    	
    	for(Sensor sensor:sensorList){
    		stringList.add(sensor.getName());
    	}
		return stringList;
    }
    public void setSelectedOptions(List<String> selectedOptions) {
        this.selectedOptions = selectedOptions;
        dateModel.clear();
        for(String name:selectedOptions){
        	for(Sensor sensor:sensorList){
        			if (name.equals(sensor.getName())){
        				addSerie(sensor.getSensorId());
        			}
        	}
        }
    }
	
}
