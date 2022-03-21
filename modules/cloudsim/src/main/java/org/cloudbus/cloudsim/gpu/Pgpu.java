/**
 * 
 */
package org.cloudbus.cloudsim.gpu;

import java.util.List;

import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.gpu.provisioners.GpuBwProvisioner;
import org.cloudbus.cloudsim.gpu.provisioners.GpuGddramProvisioner;

/**
 * 
 * Represents a physical GPU inside a video card.
 * 
 * @author Ahmad Siavashi
 *
 */
public class Pgpu {

	/**
	 * Pgpu Id
	 */
	private int id;
	
	/**
	 * Type of the Pgpu
	 */
	private String type;

	/**
	 * List of Pgpu's PEs
	 */
	private List<Pe> peList;
	/**
	 * GPU's GDDRAM provisioner
	 */
	private GpuGddramProvisioner gddramProvisioner;
	/**
	 * GPU's GDDRAM bandwidth provisioner
	 */
	private GpuBwProvisioner bwProvisioner;

	/**
	 * @param id
	 *            Pgpu id
	 * @param pes
	 *            list of Pgpu's processing elements
	 */
	public Pgpu(int id, String type, List<Pe> pes, GpuGddramProvisioner gddramProvisioner, GpuBwProvisioner bwProvisioner) {
		super();
		setId(id);
		setType(type);
		setPeList(pes);
		setGddramProvisioner(gddramProvisioner);
		setBwProvisioner(bwProvisioner);
	}

	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}

	public List<Pe> getPeList() {
		return peList;
	}

	protected void setPeList(List<Pe> peList) {
		this.peList = peList;
	}

	/**
	 * @return the gddramProvisioner
	 */
	public GpuGddramProvisioner getGddramProvisioner() {
		return gddramProvisioner;
	}

	/**
	 * @param gddramProvisioner
	 *            the gddramProvisioner to set
	 */
	public void setGddramProvisioner(GpuGddramProvisioner gddramProvisioner) {
		this.gddramProvisioner = gddramProvisioner;
	}

	/**
	 * @return the bwProvisioner
	 */
	public GpuBwProvisioner getBwProvisioner() {
		return bwProvisioner;
	}

	/**
	 * @param bwProvisioner
	 *            the bwProvisioner to set
	 */
	public void setBwProvisioner(GpuBwProvisioner bwProvisioner) {
		this.bwProvisioner = bwProvisioner;
	}

	public String getType() {
		return type;
	}

	protected void setType(String type) {
		this.type = type;
	}

}
