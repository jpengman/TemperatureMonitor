package se.anviken.temperaturemonitor;

import javax.annotation.PostConstruct;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartSeries;

import com.cgi.osd.transactiondemo.persistence.Booking;

import se.anviken.temperaturemonitor.persistance.Temperature;

@ManagedBean
public class DynamicChartView implements Serializable {

	private static final long serialVersionUID = -9182840560176521158L;
	private LineChartModel dateModel;

	@Inject
	private EntityManager em;

	@PostConstruct
	public void init() {
		createDateModel();
	}

	public LineChartModel getDateModel() {
		return dateModel;
	}

	private void createDateModel() {
		dateModel = new LineChartModel();
		LineChartSeries series1 = new LineChartSeries();
		series1.setLabel("Series 1");

		TypedQuery<Temperature> tempQuery = em.createNamedQuery("Temperature.ForSensor",Temperature.class);
		tempQuery.setParameter("sensorId", 14);
		Calendar start = Calendar.getInstance();
		start.add(Calendar.DATE,-1);
		Date startDate = start.getTime();
		Date endDate = Calendar.getInstance().getTime();
		tempQuery.setParameter("startDate", startDate );
		tempQuery.setParameter("endDate", endDate);
		List<Temperature> list = tempQuery.getResultList();
		
		for(Temperature temperature:list){
			series1.set(temperature.getTempTimestamp(),temperature.getTemperature());
		}
		series1.set("2014-01-06", 22);
		series1.set("2014-01-12", 65);
		series1.set("2014-01-18", 74);
		series1.set("2014-01-24", 24);
		series1.set("2014-01-30", 51);

		LineChartSeries series2 = new LineChartSeries();
		series2.setLabel("Series 2");

		series2.set("2014-01-01", 32);
		series2.set("2014-01-06", 73);
		series2.set("2014-01-12", 24);
		series2.set("2014-01-18", 12);
		series2.set("2014-01-24", 74);
		series2.set("2014-01-30", 62);

		dateModel.addSeries(series1);
		dateModel.addSeries(series2);

		dateModel.setTitle("Zoom for Details");
		dateModel.setZoom(true);
		dateModel.getAxis(AxisType.Y).setLabel("Values");
		DateAxis axis = new DateAxis("Dates");
		axis.setTickAngle(-50);
		axis.setMax("2014-02-01");
		axis.setTickFormat("%b %#d, %y");

		dateModel.getAxes().put(AxisType.X, axis);
	}
}
