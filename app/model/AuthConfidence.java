package model;

import dto.Message;
import enums.DeviceType;

import java.util.*;

import static enums.DeviceType.*;
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


    public AuthConfidence() {
    }

    public void addMessege(Message message){
        messages.add(message);
        updateConfidenceScore(message);
    }

    public int updateConfidenceScore(Message message){
        int score = calculateConfidenceScore(message);
        confidenceScore=score;
        if(getConfidenceScore() >1000 ){
            setActionRequired(true);
        }
        return confidenceScore;
    }

    public int confirmAction(){
        setActionConfirmed(true);
        confidenceScore+=500;
        success=true;
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
                    val = Math.pow((14.5 - proximity * proximity),3) + random(130, 200);
                    setScore(deviceId, val, PROX, proximity);

                }
                //adds scores for paired devices
                //bluetooth address
                if(beacon.getBluetoothAddress() == regBeacon.getBluetoothAddress()){
                    val= random(380, 480);
                    setScore(deviceId, val, BT_ADDRESS);

                }
                //devId
                if(beacon.getDeviceId().equals(regBeacon.getDeviceId())){
                    val= random(175, 275);
                    setScore(deviceId, val, ACTIVE);
                }
            }

            //Phone

             if(master!=null && regMaster!=null){
                setScore(deviceId,0,ACTIVE);
                if(master.getWifiSSID().equals(regMaster.getWifiSSID())){
                    val= random(225, 325);
                    setScore(deviceId,val,WIFI_SSID);
                }
                if(master.getIpAddress().equals(regMaster.getIpAddress())){
                    val=random(100,200);
                    setScore(deviceId,val,IP_ADDRESS);
                }
                if(master.getBluetoothAddress() == regMaster.getBluetoothAddress()) {
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

    public static List<Score> getScoreCriteria(DeviceType deviceType, String deviceId){
        List<Score> criteria=new ArrayList<>();
        if(deviceType== SMART_PHONE){
            criteria.add(new Score(deviceId,deviceType,ACTIVE));
            criteria.add(new Score(deviceId,deviceType,WIFI_SSID));
            criteria.add(new Score(deviceId,deviceType,IP_ADDRESS));
            criteria.add(new Score(deviceId,deviceType,BT_ADDRESS));
        }else if(deviceType==BEACON){
            criteria.add(new Score(deviceId,deviceType,ACTIVE));
            criteria.add(new Score(deviceId,deviceType,PROX));
            criteria.add(new Score(deviceId,deviceType,BT_ADDRESS));
        }
        return criteria;
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
        regMaster.setType(SMART_PHONE);
        regMaster.setWifiSSID("ssid");
        regMaster.setBluetoothAddress("ABC");
        regMaster.setIpAddress("1235");

        Device regBeacon = new Device();
        regBeacon.setBluetoothAddress("ABC");
        regBeacon.setDeviceId("ABC");

        Message master = new Message();
        master.setDeviceType(SMART_PHONE);
        master.setWifiSSID("ssid");
        master.setBluetoothAddress("ABC");
        master.setIpAddress("123");
        master.setOrigDevice(regMaster);

        Message beacon = new Message();
        beacon.setDeviceType(BEACON);
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
