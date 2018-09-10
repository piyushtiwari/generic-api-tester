package common;

//import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * Created by SourabhM on 31-12-2015.
 */
public class jsonParser
{
    HashMap newmap = new HashMap();
    public  LinkedHashMap getJsonReturnHashMap(String jsonString ) throws IOException
    {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(jsonString, Map.class);

        LinkedHashMap hashMapOfJson = new  LinkedHashMap(map);

        return hashMapOfJson;

    }



    public LinkedHashMap getHashMapFromString(String jsonString)
            throws JSONException, JsonParseException, JsonMappingException,
            IOException {

        JSONObject input = new JSONObject(jsonString);
        Iterator itrKeys = input.keys();

        Object temp;
        JSONArray tempJsonArray;
        //JSONObject tempJsonObject;
        ObjectMapper mapper = new ObjectMapper();
      //  ObjectMapper mapper1 = new ObjectMapper();

        System.out.println("the json string is "+jsonString);
        System.out.println(input.toString());

        while (itrKeys.hasNext()) {

            // JSONArray values = input.getJSONArray(itrKeys.next().toString());
            String keyValueIs= itrKeys.next().toString();
            temp = input.get(keyValueIs);
            System.out.println("temp inside "+temp);


            if (temp instanceof JSONArray) {
                tempJsonArray = (JSONArray) temp;
                System.out.println("if condition - jsonArray as string-------------------"
                        + tempJsonArray.toString());
                for (int j = 0; j < tempJsonArray.length(); j++) {
                    System.out.println("if condition"+tempJsonArray.get(j));
                    Map<String, Object> map = mapper.readValue(tempJsonArray.get(j).toString(), Map.class);
                    System.out.println("if condition map-------------"+map.toString());
                    //getHashMapFromString(map.toString());
                    getHashMapFromString(tempJsonArray.get(j).toString());
                }
            }
            else  {
                System.out.println("inside else -----"+keyValueIs);
                System.out.println("inside else----"+temp);
                System.out.println("inside else----"+input);
                System.out.println("Inside the else condition-----------"+temp.getClass());
                newmap.put(keyValueIs, temp);
                System.out.println("else it is not array");
                //break;
            }

        }

        for (Object key : newmap.keySet()) {
            System.out.println("---------------------Key = " + key);
            System.out.println("----------------------value = " + newmap.get(key));
        }

        LinkedHashMap hashMapOfJson = new LinkedHashMap(newmap);
        return hashMapOfJson;

    }






}
