package org.cloudbus.cloudsim;



import Vms.CthenGVm;
import Vms.GthenCVm;
import de.vandermeer.asciitable.AsciiTable;
import java.util.Map.Entry;
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.gpu.*;
import org.cloudbus.cloudsim.gpu.allocation.VideoCardAllocationPolicy;
import org.cloudbus.cloudsim.gpu.allocation.VideoCardAllocationPolicyNull;
import org.cloudbus.cloudsim.gpu.hardware_assisted.grid.*;
import org.cloudbus.cloudsim.gpu.performance.models.PerformanceModel;
import org.cloudbus.cloudsim.gpu.performance.models.PerformanceModelGpuConstant;
import org.cloudbus.cloudsim.gpu.power.PowerGpuDatacenter;
import org.cloudbus.cloudsim.gpu.power.PowerGpuDatacenterBroker;
import org.cloudbus.cloudsim.gpu.power.PowerGpuHost;
import org.cloudbus.cloudsim.gpu.power.PowerVideoCard;
import org.cloudbus.cloudsim.gpu.power.models.GpuHostPowerModelLinear;
import org.cloudbus.cloudsim.gpu.power.models.VideoCardPowerModel;
import org.cloudbus.cloudsim.gpu.provisioners.GpuBwProvisionerShared;
import org.cloudbus.cloudsim.gpu.provisioners.GpuGddramProvisionerSimple;
import org.cloudbus.cloudsim.gpu.provisioners.VideoCardBwProvisioner;
import org.cloudbus.cloudsim.gpu.provisioners.VideoCardBwProvisionerShared;
import org.cloudbus.cloudsim.gpu.selection.PgpuSelectionPolicy;
import org.cloudbus.cloudsim.gpu.selection.PgpuSelectionPolicyNull;
import org.cloudbus.cloudsim.lists.VmList;
import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.text.DecimalFormat;
import java.util.*;

public class FirstExperiment {

    /** The cloudlet list. */
    private static List<Cloudlet> cloudletList;
    private static List<GpuCloudlet> gpucloudletList;
    private static List<Cloudlet> randomcloudletList;
    /** The vmlist. */
    private static List<Vm> vmlist;
    private static List<GpuVm> gpuvmlist;
    private static List<CthenGVm> cpugpuvmlist;
    private static List<GthenCVm> gpucpuvmlist;


    /** The datacenter list. */

    private static List<Datacenter> datacenterList;
    private static List<PowerGpuDatacenter> gpudatacenterList;
    /** number of VMs. */
    private static int numVms = 2;
    private static int numgpuVms = 3;
    private static int numcpugpuVms = 3;
    private static int numgpucpuVms = 3;

    /** number of gpu-cloudlets */
    private static int numGpuCloudlets = 5;
    private static int numCloudlets = 25;
    private static int randomCloudlets = 2 + (int)(Math.random() * 10);
    private static int lastVmIndex;
    private static int lastCloudletIndex;
    private static int lastvgpuIndex;
    /**
     * The resolution in which progress in evaluated.
     */
    private static double schedulingInterval = 20;    /**
     * Creates main() to run this example
     */
    public static void main(String[] args) {

        Log.printLine("Starting CloudSimExample3...");

        try {
            // First step: Initialize the CloudSim package. It should be called
            // before creating any entities.
            int num_user = 1;   // number of cloud users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            // Initialize the CloudSim library
            CloudSim.init(num_user, calendar, trace_flag);
            datacenterList = new ArrayList<Datacenter>();
            gpudatacenterList = new ArrayList<PowerGpuDatacenter>();
            // Create a list to hold created VMs
            gpuvmlist = new ArrayList<GpuVm>();
            // Create a list to hold issued Cloudlets
            gpucloudletList = new ArrayList<GpuCloudlet>();
            // Second step: Create Datacenters
            //Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
            @SuppressWarnings("unused")
            Datacenter datacenter = createDatacenter("DatacenterSimple");
            datacenterList.add(datacenter);
            PowerGpuDatacenter Gpudatacenter = createGpuDatacenter("GpuDatacenter");
            gpudatacenterList.add(Gpudatacenter);
            //Third step: Create Broker
            DatacenterBroker broker = createBroker();
            int brokerId = broker.getId();
            PowerGpuDatacenterBroker gpubroker = createGpuBroker("GpuBroker");
            int gpubrokerId = gpubroker.getId();

            //Fourth step: Create virtual machine
            vmlist =  createVmList(brokerId);
            gpuvmlist = createGpuVmList(gpubrokerId);
            cpugpuvmlist= createCthenGVmList(brokerId,createRandomCloudlets(brokerId));
            gpucpuvmlist= createGthenCVmList(gpubrokerId, createRandomCloudlets(gpubrokerId));
            //Fifth step: Create Cloudlets
            cloudletList = creatCloudletList(brokerId,numCloudlets);
            gpucloudletList= createGpuCloudletList(gpubrokerId,numGpuCloudlets);

            // Cloudlet-VM assignment
            for (int i = 0; i < numGpuCloudlets; i++) {
                GpuCloudlet cloudlet = gpucloudletList.get(i);
                cloudlet.setVmId(i % numgpuVms);
            }

            //submit vm list to the broker
            broker.submitVmList(vmlist);
            //submit cloudlet list to the broker
            broker.submitCloudletList(cloudletList);


            // submit GpuVm list to the broker
            gpubroker.submitVmList(gpuvmlist);
            // submit Gpucloudlet list to the broker
            gpubroker.submitCloudletList(gpucloudletList);


            // Sixth step: Starts the simulation
            CloudSim.startSimulation();
            // Final step: Print results when simulation is over
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            List<Cloudlet> gpunewList = gpubroker.getCloudletReceivedList();

            CloudSim.stopSimulation();

            printCloudletList(newList);
            printGpuCloudletList(gpunewList);

            Log.printLine("First Experiment finished!");
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
    }
    private static List<Cloudlet> creatCloudletList(int brokerId, int NumofCl){
        cloudletList = new ArrayList<Cloudlet>();

        //Cloudlet properties

        int pesNumber=1;
        long length = 40000;
        long fileSize = 300;
        long outputSize = 300;
        UtilizationModel utilizationModel = new UtilizationModelFull();
        for (int i = 0; i < NumofCl ; i++) {
            Cloudlet cloudlet1 = new Cloudlet(lastCloudletIndex, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
            cloudlet1.setUserId(brokerId);
            lastCloudletIndex= ++lastCloudletIndex;

            //add the cloudlets to the list
            cloudletList.add(cloudlet1);
        }
        return cloudletList;
    }

    /**
     * Create a GpuCloudlet.
     */
    private static List<GpuCloudlet> createGpuCloudletList( int brokerId, int NumofCl) {
        // Cloudlet properties
        long length = (long) (400 * GpuHostTags.DUAL_INTEL_XEON_E5_2620_V3_PE_MIPS);
        long fileSize = 300;
        long outputSize = 300;
        int pesNumber = 1;
        UtilizationModel cpuUtilizationModel = new UtilizationModelFull();
        UtilizationModel ramUtilizationModel = new UtilizationModelFull();
        UtilizationModel bwUtilizationModel = new UtilizationModelFull();

        // GpuTask properties
        long taskLength = (long) (GridVideoCardTags.NVIDIA_K1_CARD_PE_MIPS * 150);
        long taskInputSize = 128;
        long taskOutputSize = 128;
        long requestedGddramSize = 4 * 1024;
        int numberOfBlocks = 2;
        UtilizationModel gpuUtilizationModel = new UtilizationModelFull();
        UtilizationModel gddramUtilizationModel = new UtilizationModelFull();
        UtilizationModel gddramBwUtilizationModel = new UtilizationModelFull();

        // Create GpuCloudlets
        for (int i = 0; i < NumofCl; i++) {
            GpuTask gpuTask = new GpuTask(lastCloudletIndex, taskLength, numberOfBlocks, taskInputSize, taskOutputSize,
                    requestedGddramSize, gpuUtilizationModel, gddramUtilizationModel, gddramBwUtilizationModel);
            lastCloudletIndex= ++lastCloudletIndex;
            GpuCloudlet gpuCloudlet = new GpuCloudlet(lastCloudletIndex, length, pesNumber, fileSize, outputSize,
                    cpuUtilizationModel, ramUtilizationModel, bwUtilizationModel, gpuTask, false);
            gpuCloudlet.setUserId(brokerId);
            lastCloudletIndex= ++lastCloudletIndex;
            // add the cloudlet to the list
            gpucloudletList.add(gpuCloudlet);
        }

        return gpucloudletList;
    }

    private static List<Cloudlet> createRandomCloudlets(int brokerId){
        for (int i=0; i<randomCloudlets; i++){
            randomcloudletList.addAll(creatCloudletList(brokerId,1));
            randomcloudletList.addAll(createGpuCloudletList(brokerId,1));

        }return randomcloudletList;
    }

    private static List<Vm> createVmList(int brokerId){
        vmlist = new ArrayList<Vm>();

        //VM description

        int mips = 250;
        long size = 10000; //image size (MB)
        int ram = 2048; //vm memory (MB)
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        for (int i = 0; i < numVms ; i++) {
            //create two VMs
            Vm vm1 = new Vm(lastVmIndex, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());

            lastVmIndex = ++lastVmIndex ;
            //add the VMs to the vmList
            vmlist.add(vm1);
        }
        return vmlist;

    }
    /**
     * Create a VM.
     */
    private static List<GpuVm> createGpuVmList( int brokerId) {
        // VM description
        double mips = GpuHostTags.DUAL_INTEL_XEON_E5_2620_V3_PE_MIPS;
        // image size (GB)
        int size = 10;
        // vm memory (GB)
        int ram = 2;
        long bw = 100;
        // number of cpus
        int pesNumber = 4;
        // VMM name
        String vmm = "Xen";
        // Create GpuTask Scheduler
        GpuTaskSchedulerLeftover gpuTaskScheduler = new GpuTaskSchedulerLeftover();
        // Create VMs
        for (int i = 0; i < numgpuVms; i++) {

            // Create VM
            GpuVm vm = new GpuVm(lastVmIndex, brokerId, mips, pesNumber, ram, bw, size, vmm, "Custom",
                    new GpuCloudletSchedulerTimeShared());

            // Create a Vgpu
            Vgpu vgpu = GridVgpuTags.getK180Q(lastvgpuIndex, gpuTaskScheduler);
            vm.setVgpu(vgpu);
            lastVmIndex = ++lastVmIndex ;
            lastvgpuIndex = ++lastvgpuIndex;
            // add the VM to the vmList
            gpuvmlist.add(vm);
        }

        return gpuvmlist;
    }

    private static List<CthenGVm> createCthenGVmList(int brokerId, List<Cloudlet> CthenGCloudlets){
        //VM description

        int mips = 250;
        long size = 10000; //image size (MB)
        int ram = 2048; //vm memory (MB)
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        for (int i = 0; i < numcpugpuVms ; i++) {
            //create two VMs
            CthenGVm vm1 = new CthenGVm(lastVmIndex, brokerId, mips, pesNumber, ram, bw, size, vmm,"Custom", new CloudletSchedulerTimeShared(), CthenGCloudlets);

            lastVmIndex = ++lastVmIndex ;
            //add the VMs to the vmList
            cpugpuvmlist.add(vm1);
        }
        return cpugpuvmlist;

    }


    private static List<GthenCVm> createGthenCVmList(int brokerId, List<Cloudlet> GthenCCloudlets){
        //VM description

        int mips = 250;
        long size = 10000; //image size (MB)
        int ram = 2048; //vm memory (MB)
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        for (int i = 0; i < numgpucpuVms ; i++) {
            //create two VMs
            GthenCVm vm1 = new GthenCVm(lastVmIndex, brokerId, mips, pesNumber, ram, bw, size, vmm,"Custom", new CloudletSchedulerTimeShared(), GthenCCloudlets);

            lastVmIndex = ++lastVmIndex ;
            //add the VMs to the vmList
            gpucpuvmlist.add(vm1);
        }
        return gpucpuvmlist;

    }

    private static Datacenter createDatacenter(String name){

        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store
        //    our machine
        List<Host> hostList = new ArrayList<Host>();

        // 2. A Machine contains one or more PEs or CPUs/Cores.
        // In this example, it will have only one core.
        List<Pe> peList = new ArrayList<Pe>();

        int mips = 1000;

        // 3. Create PEs and add these into a list.
        peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

        //4. Create Hosts with its id and list of PEs and add them to the list of machines
        int hostId=0;
        int ram = 2048; //host memory (MB)
        long storage = 1000000; //host storage
        int bw = 10000;

        hostList.add(
                new Host(
                        hostId,
                        new RamProvisionerSimple(ram),
                        new BwProvisionerSimple(bw),
                        storage,
                        peList,
                        new VmSchedulerTimeShared(peList)
                )
        ); // This is our first machine

        //create another machine in the Data center
        List<Pe> peList2 = new ArrayList<Pe>();

        peList2.add(new Pe(0, new PeProvisionerSimple(mips)));

        hostId++;

        hostList.add(
                new Host(
                        hostId,
                        new RamProvisionerSimple(ram),
                        new BwProvisionerSimple(bw),
                        storage,
                        peList2,
                        new VmSchedulerTimeShared(peList2)
                )
        ); // This is our second machine



        // 5. Create a DatacenterCharacteristics object that stores the
        //    properties of a data center: architecture, OS, list of
        //    Machines, allocation policy: time- or space-shared, time zone
        //    and its price (G$/Pe time unit).
        String arch = "x86";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
        double cost = 3.0;              // the cost of using processing in this resource
        double costPerMem = 0.05;     // the cost of using memory in this resource
        double costPerStorage = 0.001; // the cost of using storage in this resource
        double costPerBw = 0.0;          // the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>();   //we are not adding SAN devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

        // 6. Finally, we need to create a PowerDatacenter object.
        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }


    private static PowerGpuDatacenter createGpuDatacenter(String name) {
        // We need to create a list to store our machine
        List<GpuHost> hostList = new ArrayList<GpuHost>();

        /* Create 2 hosts, one is GPU-equipped */

        // Number of host's video cards
        int numVideoCards = GpuHostTags.DUAL_INTEL_XEON_E5_2620_V3_NUM_VIDEO_CARDS;
        // To hold video cards
        List<VideoCard> videoCards = new ArrayList<VideoCard>(numVideoCards);
        for (int videoCardId = 0; videoCardId < numVideoCards; videoCardId++) {
            List<Pgpu> pgpus = new ArrayList<Pgpu>();
            // Adding an NVIDIA K1 Card
            double mips = GridVideoCardTags.NVIDIA_K1_CARD_PE_MIPS;
            int gddram = GridVideoCardTags.NVIDIA_K1_CARD_GPU_MEM;
            long bw = GridVideoCardTags.NVIDIA_K1_CARD_BW_PER_BUS;
            for (int pgpuId = 0; pgpuId < GridVideoCardTags.NVIDIA_K1_CARD_GPUS; pgpuId++) {
                List<Pe> pes = new ArrayList<Pe>();
                for (int peId = 0; peId < GridVideoCardTags.NVIDIA_K1_CARD_GPU_PES; peId++) {
                    pes.add(new Pe(peId, new PeProvisionerSimple(mips)));
                }
                pgpus.add(new Pgpu(pgpuId, GridVideoCardTags.NVIDIA_K1_GPU_TYPE, pes,
                        new GpuGddramProvisionerSimple(gddram), new GpuBwProvisionerShared(bw)));
            }
            // Pgpu selection policy
            PgpuSelectionPolicy pgpuSelectionPolicy = new PgpuSelectionPolicyNull();
            // Performance Model
            double performanceLoss = 0.1;
            PerformanceModel<VgpuScheduler, Vgpu> performanceModel = new PerformanceModelGpuConstant(performanceLoss);
            // Scheduler
            GridPerformanceVgpuSchedulerFairShare vgpuScheduler = new GridPerformanceVgpuSchedulerFairShare(
                    GridVideoCardTags.NVIDIA_K1_CARD, pgpus, pgpuSelectionPolicy, performanceModel);
            // PCI Express Bus Bw Provisioner
            VideoCardBwProvisioner videoCardBwProvisioner = new VideoCardBwProvisionerShared(BusTags.PCI_E_3_X16_BW);
            // Video Card Power Model
            VideoCardPowerModel videoCardPowerModel = new GridVideoCardPowerModelK1(false);
            // Create a video card
            PowerVideoCard videoCard = new PowerVideoCard(videoCardId, GridVideoCardTags.NVIDIA_K1_CARD, vgpuScheduler,
                    videoCardBwProvisioner, videoCardPowerModel);
            videoCards.add(videoCard);
        }

        // Create a host
        int hostId = 0;

        // A Machine contains one or more PEs or CPUs/Cores.
        List<Pe> peList = new ArrayList<Pe>();

        // PE's MIPS power
        double mips = GpuHostTags.DUAL_INTEL_XEON_E5_2620_V3_PE_MIPS;

        for (int peId = 0; peId < GpuHostTags.DUAL_INTEL_XEON_E5_2620_V3_NUM_PES; peId++) {
            // Create PEs and add these into a list.
            peList.add(new Pe(0, new PeProvisionerSimple(mips)));
        }

        // Create Host with its id and list of PEs and add them to the list of machines
        // host memory (MB)
        int ram = GpuHostTags.DUAL_INTEL_XEON_E5_2620_V3_RAM;
        // host storage
        long storage = GpuHostTags.DUAL_INTEL_XEON_E5_2620_V3_STORAGE;
        // host BW
        int bw = GpuHostTags.DUAL_INTEL_XEON_E5_2620_V3_BW;
        // Set VM Scheduler
        VmScheduler vmScheduler = new VmSchedulerTimeShared(peList);
        // Host Power Model
        double hostMaxPower = 200;
        double hostStaticPowerPercent = 0.70;
        PowerModel powerModel = new GpuHostPowerModelLinear(hostMaxPower, hostStaticPowerPercent);
        // Video Card Selection Policy
        VideoCardAllocationPolicy videoCardAllocationPolicy = new VideoCardAllocationPolicyNull(videoCards);

        PowerGpuHost newHost = new PowerGpuHost(hostId, GpuHostTags.DUAL_INTEL_XEON_E5_2620_V3,
                new RamProvisionerSimple(ram), new BwProvisionerSimple(bw), storage, peList, vmScheduler,
                videoCardAllocationPolicy, powerModel);

        hostList.add(newHost);

        // A host without GPU

        // A Machine contains one or more PEs or CPUs/Cores.
        peList = new ArrayList<Pe>();

        for (int peId = 0; peId < GpuHostTags.DUAL_INTEL_XEON_E5_2690_V4_NUM_PES; peId++) {
            // Create PEs and add these into a list.
            peList.add(new Pe(0, new PeProvisionerSimple(GpuHostTags.DUAL_INTEL_XEON_E5_2690_V4_PE_MIPS)));
        }
        powerModel = new GpuHostPowerModelLinear(hostMaxPower, hostStaticPowerPercent);
        newHost = new PowerGpuHost(hostId, GpuHostTags.DUAL_INTEL_XEON_E5_2690_V4,
                new RamProvisionerSimple(GpuHostTags.DUAL_INTEL_XEON_E5_2690_V4_RAM),
                new BwProvisionerSimple(GpuHostTags.DUAL_INTEL_XEON_E5_2690_V4_BW),
                GpuHostTags.DUAL_INTEL_XEON_E5_2690_V4_STORAGE, peList, new VmSchedulerTimeShared(peList), null,
                powerModel);

        hostList.add(newHost);

        // Create a DatacenterCharacteristics object that stores the
        // properties of a data center: architecture, OS, list of
        // Machines, allocation policy: time- or space-shared, time zone
        // and its price (G$/Pe time unit).
        // system architecture
        String arch = "x86";
        // operating system
        String os = "Linux";
        // VM Manager
        String vmm = "Horizen";
        // time zone this resource located (Tehran)
        double time_zone = +3.5;
        // the cost of using processing in this resource
        double cost = 0.0;
        // the cost of using memory in this resource
        double costPerMem = 0.00;
        // the cost of using storage in this resource
        double costPerStorage = 0.000;
        // the cost of using bw in this resource
        double costPerBw = 0.0;
        // we are not adding SAN devices by now
        LinkedList<Storage> storageList = new LinkedList<Storage>();

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(arch, os, vmm, hostList, time_zone,
                cost, costPerMem, costPerStorage, costPerBw);

        // We need to create a Datacenter object.
        PowerGpuDatacenter datacenter = null;
        try {
            datacenter = new PowerGpuDatacenter(name, characteristics,
                    new GridGpuVmAllocationPolicyBreadthFirst(hostList), storageList, schedulingInterval);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;}

    //We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
    //to the specific rules of the simulated scenario
    private static DatacenterBroker createBroker(){

        DatacenterBroker broker = null;
        try {
            broker = new DatacenterBroker("Broker");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }

    /**
     * Creates the broker.
     *
     * * @param name the name
     *
     * @return the datacenter broker
     */
    private static PowerGpuDatacenterBroker createGpuBroker(String name) {
        PowerGpuDatacenterBroker broker = null;
        try {
            broker = new PowerGpuDatacenterBroker(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }

    /**
     * Prints the Cloudlet objects
     * @param list  list of Cloudlets
     */

    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
                "Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
                Log.print("SUCCESS");

                Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
                        indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime())+
                        indent + indent + dft.format(cloudlet.getFinishTime()));
            }
        }

    }

    private static void printGpuCloudletList(List<Cloudlet> gpuCloudlets) {
        Log.printLine(String.join("", Collections.nCopies(100, "-")));
        DecimalFormat dft = new DecimalFormat("###.##");
        for (GpuCloudlet gpuCloudlet : (List<GpuCloudlet>) (List<?>) gpuCloudlets) {
            // Cloudlet
            AsciiTable at = new AsciiTable();
            at.addRule();
            at.addRow("Cloudlet ID", "Status", "Datacenter ID", "VM ID", "Time", "Start Time", "Finish Time");
            at.addRule();
            if (gpuCloudlet.getStatus() == Cloudlet.SUCCESS) {
                at.addRow(gpuCloudlet.getCloudletId(), "SUCCESS", gpuCloudlet.getResourceId(), gpuCloudlet.getVmId(),
                        dft.format(gpuCloudlet.getActualCPUTime()).toString(),
                        dft.format(gpuCloudlet.getExecStartTime()).toString(),
                        dft.format(gpuCloudlet.getFinishTime()).toString());
                at.addRule();
            }
            GpuTask gpuTask = gpuCloudlet.getGpuTask();
            // Gpu Task
            at.addRow("Task ID", "Cloudlet ID", "Status", "vGPU Profile", "Time", "Start Time", "Finish Time");
            at.addRule();
            if (gpuTask.getTaskStatus() == GpuTask.SUCCESS) {
                at.addRow(gpuTask.getTaskId(), gpuTask.getCloudlet().getCloudletId(), "SUCCESS",
                        ((GpuVm) VmList.getById(gpuvmlist, gpuTask.getCloudlet().getVmId())).getVgpu().getType(),
                        dft.format(gpuTask.getActualGPUTime()).toString(),
                        dft.format(gpuTask.getExecStartTime()).toString(),
                        dft.format(gpuTask.getFinishTime()).toString());
                at.addRule();
            }
            at.getContext().setWidth(100);
            Log.printLine(at.render());
            Log.printLine(String.join("", Collections.nCopies(100, "-")));

        }

        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Entity", "Energy Consumed (Joules)");
        at.addRule();
        for (PowerGpuDatacenter datacenter : gpudatacenterList) {
            String depth = "#" + datacenter.getId();
            at.addRow("Datacenter " + depth, dft.format(datacenter.getConsumedEnergy()).toString());
            at.addRule();
            for (Entry<PowerGpuHost, Double> entry : datacenter.getHostEnergyMap().entrySet()) {
                PowerGpuHost host = entry.getKey();
                depth = "#" + host.getId() + " / " + depth;
                at.addRow("Host " + depth, dft.format(datacenter.getHostCpuEnergyMap().get(host)).toString() + " / "
                        + dft.format(datacenter.getHostEnergyMap().get(host)).toString());
                at.addRule();
                if (host.isGpuEquipped()) {
                    for (PowerVideoCard videoCard : (List<PowerVideoCard>) host.getVideoCardAllocationPolicy()
                            .getVideoCards()) {
                        depth = "#" + videoCard.getId() + " / " + depth;
                        at.addRow("Video Card " + depth,
                                dft.format(datacenter.getHostVideoCardEnergyMap().get(host).get(videoCard)).toString()
                                        + " / " + dft.format(datacenter.getHostEnergyMap().get(host)).toString());
                        at.addRule();
                    }
                }
                depth = "#" + datacenter.getId();
            }
        }
        at.getContext().setWidth(100);
        Log.printLine(at.render());
    }

}

