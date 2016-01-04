package se.anviken.temperaturemonitor;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import se.anviken.temperaturemonitor.persistance.PersistenceFacade;
import se.anviken.temperaturemonitor.persistance.Sensor;
import se.anviken.temperaturemonitor.persistance.SensorType;

@ManagedBean
public class AdminView extends SensorHandler implements Serializable {

	@Inject
	private EntityManager em;
	@Inject
	private PersistenceFacade persistenceFacade;

	private static final long serialVersionUID = -7888372649028471348L;

	@ManagedProperty("#{sensorService}")
	private SensorService service;

	private String username;

	private String password;

	private boolean loggedIn = false;

	private List<Sensor> sensorsNotInDb;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void login(ActionEvent event) {
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage message = null;
		boolean loggedIn = false;

		if (username != null && username.equals("admin") && password != null
				&& password.equals("admin")) {
			loggedIn = true;
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome",
					username);
		} else {
			loggedIn = false;
			message = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Loggin Error", "Invalid credentials");
		}

		FacesContext.getCurrentInstance().addMessage(null, message);
		context.addCallbackParam("loggedIn", loggedIn);
		setLoggedIn(true);
	}

	private Sensor selectedSensor;

	@PostConstruct
	public void init() {
		TypedQuery<Sensor> sensorQuery = em.createNamedQuery("Sensor.findAll",
				Sensor.class);
		sensorList = sensorQuery.getResultList();
		TypedQuery<SensorType> sensorTypeQuery = em.createNamedQuery("SensorType.findAll",
				SensorType.class);
		sensorTypeList = sensorTypeQuery.getResultList();
	}

	public void setService(SensorService service) {
		this.service = service;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}


	public Sensor getSelectedSensor() {
		return selectedSensor;
	}

	public void setSelectedSensor(Sensor selectedSensor) {
		this.selectedSensor = selectedSensor;
	}

	public void onRowEdit(RowEditEvent event) {
		persistenceFacade.updateSensor((Sensor) event.getObject());
		reloadSensorsNotInDb();
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edit Cancelled",
				((Sensor) event.getObject()).getName());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public List<String> getConnectedSensors() {

		File file = new File("/mnt/1wire");
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				if (name.startsWith("28.")) {
					return new File(current, name).isDirectory();
				} else {
					return false;
				}
			}
		});
		List<String> dirList = new ArrayList<String>();
		for (int i = 0; i < directories.length; i++) {
			dirList.add(directories[i]);
		}
		return dirList;
	}

	public List<Sensor> getSensorsNotInDb() {
		if (this.sensorsNotInDb == null) {
			reloadSensorsNotInDb();
		}
		return this.sensorsNotInDb;
	}

	private void reloadSensorsNotInDb() {
		List<String> connectedSensors = getConnectedSensors();
		List<Sensor> inDb = getSensorList();
		List<Sensor> notInDb = new ArrayList<Sensor>();
		for (String address : connectedSensors) {
			boolean isInDb = false;
			for (Sensor sensor : inDb) {
				if (sensor.getAddress().equals(address)) {
					isInDb = true;
				}
			}
			if (!isInDb) {
				Sensor newSensor = new Sensor();
				newSensor.setAddress(address);
				notInDb.add(newSensor);
			}
		}
		this.sensorsNotInDb = notInDb;
	}

	public void setSensorsNotInDb(List<Sensor> sensorList) {
		this.sensorsNotInDb = sensorList;
	}
}