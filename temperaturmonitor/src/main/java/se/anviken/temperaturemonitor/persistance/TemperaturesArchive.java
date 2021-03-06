package se.anviken.temperaturemonitor.persistance;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The persistent class for the temperatures_archive database table.
 * 
 */
@Entity
@Table(name = "temperatures_archive")
@NamedQuery(name = "TemperaturesArchive.findAll", query = "SELECT t FROM TemperaturesArchive t")
@XmlRootElement
public class TemperaturesArchive implements Serializable
{
   private static final long serialVersionUID = 1L;

   @Id
   @Column(name = "temperature_id")
   private int temperatureId;

   @Column(name = "sensor_id")
   private int sensorId;

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "temp_timestamp")
   private Date tempTimestamp;

   private float temperature;

   public TemperaturesArchive()
   {
   }

   public int getTemperatureId()
   {
      return this.temperatureId;
   }

   public void setTemperatureId(int temperatureId)
   {
      this.temperatureId = temperatureId;
   }

   public int getSensorId()
   {
      return this.sensorId;
   }

   public void setSensorId(int sensorId)
   {
      this.sensorId = sensorId;
   }

   public Date getTempTimestamp()
   {
      return this.tempTimestamp;
   }

   public void setTempTimestamp(Date tempTimestamp)
   {
      this.tempTimestamp = tempTimestamp;
   }

   public float getTemperature()
   {
      return this.temperature;
   }

   public void setTemperature(float temperature)
   {
      this.temperature = temperature;
   }

}