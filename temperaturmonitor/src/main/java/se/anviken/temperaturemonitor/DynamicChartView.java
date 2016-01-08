package se.anviken.temperaturemonitor;

import javax.annotation.PostConstruct;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartSeries;

import se.anviken.temperaturemonitor.persistance.Sensor;
import se.anviken.temperaturemonitor.persistance.SensorType;
import se.anviken.temperaturemonitor.persistance.Temperature;

@ManagedBean
public class DynamicChartView extends ButtonGroupsHandler implements Serializable {

	private static final long serialVersionUID = -9182840560176521158L;
	private LineChartModel dateModel;

	@Inject
	private EntityManager em;

	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyy-MM-dd HH:mm");
	private DateTime startDate;
	private DateTime endDate;
	private int noOfHours=6;

	public int getNoOfHours() {
		return noOfHours;
	}

	public void setNoOfHours(int noOfHours) {
		this.noOfHours = noOfHours;
		updateSeries();
	}

	@PostConstruct
	public void init() {
		TypedQuery<Sensor> sensorQuery = em.createNamedQuery("Sensor.findAll",
				Sensor.class);
		setSensorList(sensorQuery.getResultList());
		TypedQuery<SensorType> sensorTypeQuery = em.createNamedQuery("SensorType.findAll",
				SensorType.class);
		sensorTypeList = sensorTypeQuery.getResultList();
		createDateModel();
		
	}

	public LineChartModel getDateModel() {
		return dateModel;
	}

	private void createDateModel() {
		dateModel = new LineChartModel();
		setInterval(Period.hours(noOfHours));
		addSerie(getSensorList().get(0).getSensorId());

		dateModel.setAnimate(true);
		dateModel.setDatatipFormat("%s %.1f");
		dateModel.setLegendPosition("w");
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
		TypedQuery<Sensor> sensorQuery = em.createNamedQuery(
				"Sensor.findWithId", Sensor.class);
		sensorQuery.setParameter("sensorId", sensorId);
		Sensor sensor = sensorQuery.getSingleResult();

		series1.setLabel(sensor.getName());
		TypedQuery<Temperature> tempQuery = em.createNamedQuery(
				"Temperature.ForSensor", Temperature.class);
		tempQuery.setParameter("sensorId", sensorId);
		tempQuery.setParameter("startDate", startDate.toDate());
		tempQuery.setParameter("endDate", endDate.toDate());
		List<Temperature> list = tempQuery.getResultList();

		for (Temperature temperature : list) {
			series1.set(DATE_FORMAT.format(temperature.getTempTimestamp()),
					temperature.getTemperature());
		}
		series1.setShowMarker(false);
		dateModel.addSeries(series1);
	}
	@Override
	public void updateSeries() {
		dateModel.clear();
		setInterval(Period.hours(noOfHours));
		DateAxis axis = new DateAxis("Tidpunkt");
		axis.setTickAngle(-50);
		axis.setMin(DATE_FORMAT.format(startDate.toDate()));
		axis.setMax(DATE_FORMAT.format(endDate.toDate()));
		axis.setTickFormat("%Y-%m-%d %H:%M");

		dateModel.getAxes().put(AxisType.X, axis);
		loopSelected(selectedOptions);
		loopSelected(selectedRoomSensors);
		loopSelected(selectedHeatPumpSensors);

	}

	private void loopSelected(List<String> selected) {
		if (selected != null) {
			for (String name : selected) {

				addSerie(getIdFromName(name));
			}
		}
	}

	public List<String> getSensorDescriptions() {

		List<String> stringList = new ArrayList<String>();

		for (Sensor sensor : getSensorList()) {
			stringList.add(sensor.getDescription());
		}
		return stringList;
	}
	
	public void onSlideEnd(SlideEndEvent event) {
        FacesMessage message = new FacesMessage("Slide Ended", "Value: " + event.getValue());
        FacesContext.getCurrentInstance().addMessage(null, message);
    } 
}
