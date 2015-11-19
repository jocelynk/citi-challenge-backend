package model;

import dto.Message;

import java.util.*;

import static model.Score.Type.*;

/**
 * Created by sasinda on 11/10/15.
 */
public class AuthConfidence {

    private int confidenceScore=0;
    private boolean success;
    List<Message> messages=new ArrayList<>();
    Map<String,Score> scoreMap=new HashMap<>();
    private boolean actionRequired=false;
    private boolean actionRequested=false;
    private boolean actionConfirmed=false;

    public void addMessege(Message message){
        messages.add(message);
        updateConfidenceScore(message);
    }

    public int updateConfidenceScore(Message message){
        int score = calculateConfidenceScore(message);
        confidenceScore=score;
        if(getConfidenceScore() >300 ){
            setActionRequired(true);
        }
        return confidenceScore;
    }

    public int confirmAction(){
        setActionConfirmed(true);
        confidenceScore+=1000;
        return getConfidenceScore();
    }

    public  int calculateConfidenceScore(Message message){

        Device regMaster=null;
        Device regBeacon = null;
        Message master = null;
        Message beacon = null;

        //temp var to be set in score map
        String deviceId= message.getDeviceId();
        double val;
        if(message.getDeviceType() !=null && deviceId!=null){
            switch (message.getDeviceType()){
                case SMART_PHONE: {
                    regMaster= message.getOrigDevice();
                    master=message;
                    break;
                }
                case BEACON: {
                    beacon = message;
                    regBeacon = message.getOrigDevice();
                    break;
                }
            }
            //Beacon
            if(beacon!=null&& regBeacon!=null){

                //proximity
                if(beacon.getProximity() >0) {
                    double proximity= beacon.getProximity();
                    val = Math.pow((10 - proximity * proximity),3) + random(130, 200);
                    setScore(deviceId, val, PROX, proximity);

                }
                //adds scores for paired devices
                //bluetooth address
                if(beacon.getBluetoothAddress() ==regBeacon.bluetoothAddress){
                    val= random(380, 480);
                    setScore(deviceId, val, BT_ADDRESS);

                }
                //devId
                if(beacon.getDeviceId().equals(regBeacon.deviceId)){
                    val= random(175, 275);
                    setScore(deviceId, val, ACTIVE);
                }
            }

            //Phone

             if(master!=null && regMaster!=null){
                if(master.getWifiSSID().equals(regMaster.wifiSSID)){
                    val= random(225, 325);
                    setScore(deviceId,val,WIFI_SSID);
                }
                if(master.getIpAddress().equals(regMaster.ipAddress)){
                    val=random(100,200);
                    setScore(deviceId,val,IP_ADDRESS);
                }
                if(master.getBluetoothAddress() ==regMaster.bluetoothAddress) {
                    val=random(300,400);
                    setScore(deviceId,val,BT_ADDRESS);
                }
            }
            System.out.println(scoreMap);

        }

        int score= (int) getCurrentScore();
        System.out.println("Score :"+score);
        return score;
    }

    private void setScore(String deviceId, double scoreVal, Score.Type scoreType, double proximity)  {
        Score score = new Score(deviceId, scoreVal, PROX, proximity);
        scoreMap.put(score.getKey(), score );
    }
    private void setScore(String deviceId, double scoreVal, Score.Type scoreType) {
        Score score=new Score(deviceId,scoreVal,scoreType);
        scoreMap.put(score.getKey(), score );
    }

    private  double getCurrentScore(){
        double current=0;
        for (Score score : scoreMap.values()) {
            current+= score.getValue();
        }
        return current;
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
        master.setDeviceType(Message.DevTypes.SMART_PHONE);
        master.setWifiSSID("ssid");
        master.setBluetoothAddress("ABC");
        master.setIpAddress("123");
        master.setOrigDevice(regMaster);

        Message beacon = new Message();
        beacon.setDeviceType(Message.DevTypes.BEACON);
        beacon.setProximity(1);
        beacon.setBluetoothAddress("ABC");
        beacon.setDeviceId("ABC");

        List<Message> messages=new ArrayList<>();
        messages.add(master);
        messages.add(beacon);

        List<Device> devices=new ArrayList<>();
        devices.add(regMaster);
        devices.add(regBeacon);

//        calculateConfidenceScore(master);

    }

    public int getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(int confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public boolean isActionRequired() {
        return actionRequired;
    }

    public void setActionRequired(boolean actionRequired) {
        this.actionRequired = actionRequired;
    }

    public boolean isActionRequested() {
        return actionRequested;
    }

    public void setActionRequested(boolean actionRequested) {
        this.actionRequested = actionRequested;
    }

    public boolean isActionConfirmed() {
        return actionConfirmed;
    }

    public void setActionConfirmed(boolean actionConfirmed) {
        this.actionConfirmed = actionConfirmed;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Score> getSubScores() {
        return new ArrayList<>(scoreMap.values());
    }
}
