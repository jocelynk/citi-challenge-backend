package model;

import dto.Message;
import model.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sasinda on 11/10/15.
 */
public class AuthConfidence {

    public int confidenceScore=0;
    List<Message> messages=new ArrayList<>();
    public boolean actionRequired=false;



    public void addMessege(Message message){
        messages.add(message);
        if(messages.size()>2){
            actionRequired=true;
        }
        updateConfidenceScore(message);
    }

    public int updateConfidenceScore(Message message){
        int score = calculateConfidenceScore(message);
        confidenceScore+=score;
        return confidenceScore;
    }

    public static int calculateConfidenceScore(Message message){

        Device regMaster=null;
        Device regBeacon = null;
        Message master = null;
        Message beacon = null;

        double beaconScore = 0;
        double phoneScore=0;
        if(message.devType!=null){
            switch (message.devType){
                case SMART_PHONE: {
                    regMaster=message.origDevice;
                    master=message;
                    break;
                }
                case BEACON: {
                    beacon = message;
                    regBeacon = message.origDevice;
                    break;
                }
            }
            //Beacon
            if(beacon!=null){
                //proximity
                if(beacon.proximity>0) {
                    double proximity=beacon.proximity;
                    beaconScore = Math.pow((10 - proximity * proximity),3) + random(130, 200);
                }
                //bluetooth address
                if(beacon.bluetoothAddress==regBeacon.bluetoothAddress){
                    beaconScore+= random(380, 480);
                }
                //devId
                if(beacon.deviceId.equals(regBeacon.deviceId)){
                    beaconScore+= random(175, 275);
                }
            }
            System.out.println(beaconScore);

            //Phone

            if(master!=null){
                if(master.wifiSSID.equals(regMaster.wifiSSID)){
                    phoneScore+= random(225, 325);
                }
                if(master.ipAddress.equals(regMaster.ipAddress)){
                    phoneScore+=random(100,200);
                }
                if(master.bluetoothAddress==regMaster.bluetoothAddress) {
                    phoneScore+=random(300,400);
                }
            }
            System.out.println(phoneScore);
        }

        int score= (int) (beaconScore+phoneScore);
        System.out.println("Score :"+score);
        return score;
    }

    private static double random(int min, int max){
        double randomNum =  (Math.random() * (max - min) + min);
        return randomNum;
    }

    public static void main(String[] args) {

        Device regMaster=new Device();
        regMaster.type="master";
        regMaster.wifiSSID="ssid";
        regMaster.bluetoothAddress="ABC";
        regMaster.ipAddress="1235";

        Device regBeacon = new Device();
        regBeacon.bluetoothAddress="ABC";
        regBeacon.deviceId="ABC";

        Message master = new Message();
        master.devType= Message.DevTypes.SMART_PHONE;
        master.wifiSSID="ssid";
        master.bluetoothAddress="ABC";
        master.ipAddress="123";
        master.origDevice=regMaster;

        Message beacon = new Message();
        beacon.devType= Message.DevTypes.BEACON;
        beacon.proximity=1;
        beacon.bluetoothAddress="ABC";
        beacon.deviceId="ABC";

        List<Message> messages=new ArrayList<>();
        messages.add(master);
        messages.add(beacon);

        List<Device> devices=new ArrayList<>();
        devices.add(regMaster);
        devices.add(regBeacon);
        calculateConfidenceScore(master);

    }
}
