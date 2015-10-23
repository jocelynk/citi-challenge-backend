package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sasinda on 10/14/15.
 */
public class Util {

    /**
     * Deparameterize to original json form the query params coming from the jquery.param() method.
     * @param query
     * @return
     */
    public static Map<String, Object> deParam(Map<String, String[]> query) {

        Map<String, Object> map = new HashMap<>();
        List<Map<String,Object>> toArray=new ArrayList<>();

        for (String key : query.keySet()) {
            //ex key $and[0][price][$lt]
            String value=query.get(key)[0];
            key = key.replace("]", "");

            String[] split = key.split("\\[");

            Map<String, Object> grandParent =null;
            Map<String, Object> parent =map;
            String parentName="root";
            Map<String, Object> child = null;
            String lastSkey=null;

            for (String skey : split) {
                Object me = parent.get(skey);
                if(skey.matches("[0-9]+")){
                    //if array key
                    toArray.add(parent);
                    parent.put("parent",grandParent);
                    parent.put("myName", parentName );
                }
                if (me == null) {
                    child=new HashMap<>();
                    parent.put(skey, child);
                    grandParent=parent;
                    parent=child;
                    parentName=skey;
                } else {
                    grandParent=parent;
                    parent = (Map<String, Object>) me;
                    parentName=skey;
                }
                lastSkey=skey;
            }
            if(value.matches("[0-9]+\\.?[0-9]+")){
                Double numVal=new Double(value);
                grandParent.put(lastSkey,numVal);
            }else {
                grandParent.put(lastSkey,value);
            }

        }

        for (Map<String, Object> arrayMap : toArray) {
            Map<String, Object> myParent = (Map<String, Object>) arrayMap.remove("parent");
            String myName = (String) arrayMap.remove("myName");
            if(myParent!=null&&myName!=null){
                myParent.put(myName, arrayMap.values());
            }else if(myName!=null && myName.equals("root")){
                map.put("list", arrayMap.values());
            }
        }

        return map;
    }
}
