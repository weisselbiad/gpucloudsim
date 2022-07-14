package Vms;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.gpu.GpuCloudlet;
import org.cloudbus.cloudsim.gpu.GpuDatacenter;
import org.cloudbus.cloudsim.gpu.GpuDatacenterBroker;
import org.cloudbus.cloudsim.gpu.GpuVm;

import java.util.List;

public class GthenCVm extends GpuVm{

    /**
     * Describes vm's type. A type can be associated with a configuration, therefore
     * it helps identifying the vm
     */
    private String type;

    /**
     * Denotes the time in which the VM enters the system.
     */
    private double arrivalTime;
    private List<Cloudlet> cloudletList;
    private List<Cloudlet> simplecloudletList;
    private List<GpuCloudlet> gpucloudletList;

    public GthenCVm(int id, int userId, double mips, int numberOfPes, int ram, long bw, long size, String vmm, String type,
                    CloudletScheduler cloudletScheduler, List<Cloudlet> cloudletList) {
        super(id, userId, mips, numberOfPes, ram, bw, size, vmm,type, cloudletScheduler);

        setArrivalTime(0.0);
        setCloudletList(cloudletList);
    }

    protected void setType(String type) {
        this.type = type;
    }
    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setCloudletList(List<Cloudlet> cloudletList){
        this.cloudletList = cloudletList;
    }

    private void sortcloudlets(){
        for (int i=0; i< cloudletList.size(); i++){
            if(cloudletList.get(i).getClass().equals(GpuCloudlet.class)){
                gpucloudletList.add((GpuCloudlet) cloudletList.get(i));
            }else simplecloudletList.add(cloudletList.get(i));
        }
    }

    private void bindgpucloudletsToVm(GpuDatacenterBroker gpubroker){
        if(isSorted()){
            // Cloudlet-VM assignment
            for (GpuCloudlet cl : gpucloudletList) {
                cl.setVmId(this.getId());
            }

        }else System.out.println("CloudletList of the CPU then GPU Vm is not sorted");
    }

    private void sendVmToSimpleDatacenter(DatacenterBroker broker) throws InterruptedException {
        while (gpucloudletList.stream().allMatch(x -> x.isFinished() == false)){
            wait();
        }
        for(Cloudlet cl : simplecloudletList){
            broker.bindCloudletToVm(cl.getCloudletId(),this.getId());
        }
        Vm Vm = new Vm(this.getId(),broker.getId(),this.getMips(),this.getNumberOfPes(),this.getUserId(),this.getBw(),this.getSize(),this.getVmm(), this.getCloudletScheduler());

        // Cloudlet-VM assignment
        for (GpuCloudlet cl : gpucloudletList) {
            cl.setVmId(this.getId());
        }
        broker.submitVmList((List<? extends Vm>) Vm);
        broker.submitCloudletList(gpucloudletList);

        this.getHost().vmDestroy(this);
    }

    private boolean isSorted(){
        if(gpucloudletList.isEmpty() && simplecloudletList.isEmpty()){return false;}else return true;
    }

}
