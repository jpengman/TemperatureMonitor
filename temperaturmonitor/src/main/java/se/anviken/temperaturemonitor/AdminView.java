package se.anviken.temperaturemonitor;

import java.io.Serializable;
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

@ManagedBean
public class AdminView  implements Serializable{
	
	@Inject
	private EntityManager em;
	@Inject
    private PersistenceFacade persistenceFacade;
	
	private static final long serialVersionUID = -7888372649028471348L;
	
	@ManagedProperty("#{sensorService}")
    private SensorService service;
	
	private String username;

	private String password;
	
	private boolean loggedIn= false;
	
	private List<Sensor> sensors = null;
	
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
		sensors = sensorQuery.getResultList();
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

	public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensorList) {
		this.sensors = sensorList;
	}

	public Sensor getSelectedSensor() {
		return selectedSensor;
	}

	public void setSelectedSensor(Sensor selectedSensor) {
		this.selectedSensor = selectedSensor;
	}	
	
//	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void onRowEdit(RowEditEvent event) {
		persistenceFacade.updateSensor((Sensor) event.getObject());
		FacesMessage msg = new FacesMessage("Car Edited", ((Sensor) event.getObject()).getAddress());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Sensor) event.getObject()).getName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}