package common;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Created by SourabhM on 21-01-2016.
 */
public class validator {

    public static void check(xlsReader dataSource, jsonParser jsonParser, int i, String Response, Reporting reporter, File f,long time) throws IOException, JSONException {
//        LinkedHashMap jsonResponse = jsonParser.getJsonReturnHashMap(Response);
//        LinkedHashMap expectedResponse = jsonParser.getJsonReturnHashMap(dataSource.getCellData("Tests", "Parameters_to_Validate", i));
        LinkedHashMap expectedResponse =null;
        LinkedHashMap jsonResponse=null;
        //jsonParser.newmap.clear();
        expectedResponse = jsonParser.getHashMapFromString(dataSource.getCellData("Tests", "Parameters_to_Validate", i));
        jsonParser.newmap.clear();
        jsonResponse = jsonParser.getHashMapFromString(Response);
        jsonParser.newmap.clear();

        StringBuffer resultsForExcel = new StringBuffer();
        String[] passOrFail = new String[1];
        passOrFail[0] = "PASS";
        System.out.println("The scenario is ----"+dataSource.getCellData("Tests", "Scenarios", i));
        System.out.println("The actual response is "+jsonResponse.toString());
        System.out.println("The expected response is "+expectedResponse.toString());
        dataSource.setCellData("Tests", "Actual_Response", i, Response.toString());
        float timeF = time;
        float timeFlinSec = timeF/1000;
        System.out.println("time req is ---------------------"+String.format("%.1f",timeFlinSec));
//        String timeReq = (timeFlinSec>5.0) ? "<b>"+String.valueOf(timeFlinSec)+"</b>" : String.valueOf(timeFlinSec);
        String timeReq = (timeFlinSec>5.0) ? "<b>"+String.format("%.1f",timeFlinSec)+"</b>" : String.format("%.1f",timeFlinSec);

        for (Object key : expectedResponse.keySet()) {

            System.out.println("Key: " + key);
            System.out.println(expectedResponse.get(key));
            System.out.println(jsonResponse.get(key));
//           if (expectedResponse.get(key).toString().equals(jsonResponse.get(key)))
            //if (expectedResponse.get(key).toString().equals(jsonResponse.get(key).toString()))
            // if (expectedResponse.get(key).equals(jsonResponse.get(key)))
            String expRep = (expectedResponse.get(key) == null) ? "null" : expectedResponse.get(key).toString();
            String jsonResp = (jsonResponse.get(key) == null) ? "null" : jsonResponse.get(key).toString();
//
//            if (ObjectUtils.equals(expectedResponse.get(key), jsonResponse.get(key)))
            if (ObjectUtils.equals(expRep, jsonResp))
            {
                System.out.println("match for -----------" + key);
                System.out.println("Expected was--" + expectedResponse.get(key));
                System.out.println("Actual is--" + jsonResponse.get(key));
                // println("MATCH for Key: " + key + " and its Value: " + expectedResponse.get(key) + "\n")
                resultsForExcel.append("Key: " + key + " and Value: " + expectedResponse.get(key) + ", MATCHES<br>     \n");

            } else {
                System.out.println("Mismatch for -----------" + key);
                String result = "\n <b>Key</b>: " + key + ", <b>Expected</b>: " + expectedResponse.get(key) + ", <b>Found</b>:  " + jsonResponse.get(key) + "<br> \n";
                System.out.println("Expected was--" + expectedResponse.get(key));
                System.out.println("Actual is--" + jsonResponse.get(key));
                passOrFail[0] = "<font color=\"red\">FAIL</font>";
                resultsForExcel.append(result + "\n");
            }


        }
        dataSource.setCellData("Tests", "Status", i, passOrFail[0]);
        dataSource.setCellData("Tests", "Result", i, resultsForExcel.toString());
        reporter.generateReport(passOrFail[0].toString(), "Service: "+dataSource.getCellData("Tests", "SERVICE", i).toString()+"<br>Method: "+dataSource.getCellData("Tests", "Method", i).toString()+"<br>Scenario: "+dataSource.getCellData("Tests", "Scenarios", i).toString(), resultsForExcel.toString(), i, f,dataSource.getCellData("Tests", "SL No.", i).toString(),timeReq);
        jsonParser.newmap.clear();
    }


}
