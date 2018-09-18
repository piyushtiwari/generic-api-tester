package common;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.Semaphore;

/**
 * Created by SourabhM on 21-01-2016.
 */
public class runnerClass {
    public static void main(String[] args) {
        xlsReader dataSource = new xlsReader("ApiTestCases-bw.xlsx");
        jsonParser jsonParser = new jsonParser();
        String endPoint = dataSource.getCellData("Constants", "EndPoint", 2);
        // validator validator = new validator();
        System.out.println(endPoint);
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        File f = new File(dateFormat.format(d) + ".html");
        Reporting reporter = new Reporting(f);
        long startAll = System.currentTimeMillis();
        for(int i=2;i<=dataSource.getRowCount("Tests"); i++)
        //for (int i = 8; i <= 10; i++)
        {

            if (dataSource.getCellData("Tests", "Function", i).equals("YES")) {
                try {
                    URL url = new URL(dataSource.getCellData("Tests", "URL", i));
                    String path = url.getPath();
                    String query = dataSource.getCellData("Tests", "Request", i);
                    //String url = dataSource.getCellData("Tests", "URL", i);

                    LinkedHashMap headers;
                    try{
                        headers = jsonParser.getJsonReturnHashMap(dataSource.getCellData("Tests", "Headers", i));
                    } catch (IOException e1) {
                        headers = new LinkedHashMap<String, String>();
                    }

                    String method = dataSource.getCellData("Tests", "Method", i).trim();

                    switch (method) {
                        case "POST": {
                            long start = System.currentTimeMillis();
                            String response = Util.postWithHeaders(endPoint, path, headers, query);
                            long end = System.currentTimeMillis();
                            long timeReq = end - start;

                            validator.check(dataSource, jsonParser, i, response, reporter, f, timeReq);
                            break;
                        }
                        case "GET": {
                            System.out.println("inside get");
                            long start = System.currentTimeMillis();
                            String response = Util.getWithHeaders(endPoint, path, headers);
                            long end = System.currentTimeMillis();
                            long timeReq = end - start;
                            validator.check(dataSource, jsonParser, i, response, reporter, f, timeReq);
                            break;
                        }
                        case "PUT": {

                            System.out.println("inside put--------------------");
                            System.out.println("the query is --" + query);
                            long start = System.currentTimeMillis();
                            String response = Util.putWithHeaders(endPoint, path, headers, query);
                            long end = System.currentTimeMillis();
                            long timeReq = end - start;
                            validator.check(dataSource, jsonParser, i, response, reporter, f, timeReq);

                            break;
                        }
                        case "DELETE": {
                            System.out.println("inside delete-------------");
                            long start = System.currentTimeMillis();
                            String response = Util.deleteWithHeaders(endPoint, path, headers, query);
                            long end = System.currentTimeMillis();
                            long timeReq = end - start;
                            validator.check(dataSource, jsonParser, i, response, reporter, f, timeReq);
                            //all.toDelete(path, headers,i,dataSource,reporter,f,jsonParser)
                            break;
                        }
                        default:
                            System.out.println("Found no method");
                    }

                } catch (NullPointerException e) {

                    dataSource.setCellData("Tests", "Result", i, e.toString());
                    dataSource.setCellData("Tests", "Status", i, "FAIL");

                    //  reporter.generateReport("failed", dataSource.getCellData("Tests", "Scenarios", i).toString(), e.toString(), i, f)
                    reporter.generateReport("<font color=\"red\">FAIL</font>", "Service: " + (dataSource.getCellData("Tests", "SERVICE", i).toString()) + "-- Method " + (dataSource.getCellData("Tests", "Method", i).toString()) + "<br> \n" + "Scenario: " + (dataSource.getCellData("Tests", "Scenarios", i).toString()), "Found null in the input from excel", i, f, dataSource.getCellData("Tests", "SL No.", i).toString(),"NA");
                    // System.out.println(e.message)

                } catch (IOException e1) {

                    dataSource.setCellData("Tests", "Result", i, e1.toString());
                    dataSource.setCellData("Tests", "Status", i, "FAIL");

                    //  reporter.generateReport("failed", dataSource.getCellData("Tests", "Scenarios", i).toString(), e.toString(), i, f)
                    reporter.generateReport("<font color=\"red\">FAIL</font>", "Service: " + (dataSource.getCellData("Tests", "SERVICE", i).toString()) + "-- Method " + (dataSource.getCellData("Tests", "Method", i).toString()) + "<br> \n" + "Scenario: " + (dataSource.getCellData("Tests", "Scenarios", i).toString()), "Found IO exception", i, f, dataSource.getCellData("Tests", "SL No.", i).toString(),"NA");

                }
                catch (JSONException e2) {

                    dataSource.setCellData("Tests", "Result", i, e2.toString());
                    dataSource.setCellData("Tests", "Status", i, "FAIL");

                    //  reporter.generateReport("failed", dataSource.getCellData("Tests", "Scenarios", i).toString(), e.toString(), i, f)
                    reporter.generateReport("<font color=\"red\">FAIL</font>", "Service: " + (dataSource.getCellData("Tests", "SERVICE", i).toString()) + "-- Method " + (dataSource.getCellData("Tests", "Method", i).toString()) + "<br> \n" + "Scenario: " + (dataSource.getCellData("Tests", "Scenarios", i).toString()), "Found Json exception", i, f, dataSource.getCellData("Tests", "SL No.", i).toString(),"NA");

                }
            } else {
                System.out.println("test not to be run");
            }


            System.out.println("------------------------------------------------------EndOfTest" + i + "----------------------------------------------------");

        }

        long endAll = System.currentTimeMillis();
        System.out.println("The time taken is  - " + (endAll - startAll));

        if(dataSource.getCellData("Constants", "SendMail", 2).equals("YES"))
        {reporter.sendMail(f,dataSource.getCellData("Constants", "MailId", 2));}

    }
}