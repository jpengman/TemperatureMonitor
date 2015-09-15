package se.anviken.temperaturemonitor;

import javax.annotation.PostConstruct;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartSeries;

import se.anviken.temperaturemonitor.persistance.Temperature;

@ManagedBean
public class DynamicChartView implements Serializable {

	private static final long serialVersionUID = -9182840560176521158L;
	private LineChartModel dateModel;

	@Inject
	private EntityManager em;
	
    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyy-MM-dd HH:mm");
	private Date startDate;
	private Date endDate;
    
	@PostConstruct
	public void init() {
		createDateModel();
	}

	public LineChartModel getDateModel() {
		return dateModel;
	}

	private void createDateModel() {
		dateModel = new LineChartModel();
		addSerie(14);
		
		dateModel.setAnimate(true);
		dateModel.setBreakOnNull(false);
		dateModel.setDatatipFormat("%s %.1f");
		//dateModel.setExtender(extender);
		dateModel.setLegendCols(0);
		dateModel.setLegendPlacement(null);
		dateModel.setLegendRows(0);
		dateModel.setMouseoverHighlight(true);
		dateModel.setShowPointLabels(false);
		dateModel.setShowDatatip(true);
		
		dateModel.setTitle("Zoom for Details");
		dateModel.setZoom(true);
		dateModel.getAxis(AxisType.Y).setLabel("Values");
		DateAxis axis = new DateAxis("Dates");
		axis.setTickAngle(-50);
		axis.setMin(DATE_FORMAT.format(startDate));
		axis.setMax(DATE_FORMAT.format(endDate));
		axis.setTickFormat("%Y-%m-%d %H:%M");

		dateModel.getAxes().put(AxisType.X, axis);
	}

	private void addSerie(int sensor) {
		LineChartSeries series1 = new LineChartSeries();
		series1.setLabel("Series 1");
		TypedQuery<Temperature> tempQuery = em.createNamedQuery("Temperature.ForSensor",Temperature.class);
		tempQuery.setParameter("sensorId", sensor);
		Calendar start = Calendar.getInstance();
		start.add(Calendar.DATE,-1);
		startDate = start.getTime();
		endDate = Calendar.getInstance().getTime();
		tempQuery.setParameter("startDate", startDate );
		tempQuery.setParameter("endDate", endDate);
		List<Temperature> list = tempQuery.getResultList();
		
		for(Temperature temperature:list){
			series1.set(DATE_FORMAT.format(temperature.getTempTimestamp()),temperature.getTemperature());
		}
		series1.setShowMarker(false);
		dateModel.addSeries(series1);
		
	}
}
