package com.testBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.Duration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.utility.PropertiesOperations;

public class TestBase {
	BrowserFactory bf = new BrowserFactory();

	@BeforeMethod
	public void LaunchApplication() throws Exception {
		String browserName = System.getProperty("browser")!=null ? System.getProperty("browser") :PropertiesOperations.getPropertyValueByKey("browser");
	//	String browser = PropertiesOperations.getPropertyValueByKey("browser");
	//	String url = 	PropertiesOperations.getPropertyValueByKey("url");

		DriverFactory.getInstance().setDriver(bf.createBrowserInstance(browserName));
		DriverFactory.getInstance().getDriver().manage().window().maximize();
		DriverFactory.getInstance().getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	//	DriverFactory.getInstance().getDriver().navigate().to(url);
		DriverFactory.getInstance().getDriver().navigate().to("https://parabank.parasoft.com/parabank/index.htm");
	}

	@AfterMethod
	public void tearDown() {
		DriverFactory.getInstance().closeDriver();
	}
	protected JSONObject dat;
	String	tcId ;
	
	protected void setTestCaseId(String testId) {
	tcId = testId;
		
	}
	
	

    
/**
 * It is used to get test case id from testCase.json 
 * @param testcaseid
 * @return
 * @throws Exception
 */
public JSONObject getDetails(String testcaseid) throws Exception {
	JSONObject finalObj = null;
	try {
		JSONParser parser = new JSONParser();
		JSONArray jsonarray = (JSONArray) parser
				.parse(new InputStreamReader(new FileInputStream(new File("./ty/testf.json"))));
		for (Object jsonobj : jsonarray) {
			JSONObject myObj = (JSONObject) jsonobj;
			String id = myObj.get("id").toString();
			if (id.equalsIgnoreCase(testcaseid)) {
				finalObj = (JSONObject) myObj.get("dat");
			}
		}
	} catch (Exception ex) {
		ex.printStackTrace();
		throw new Exception("Unable to Find TestCase " + testcaseid);
	}
	return finalObj;
}


/**
 * reusable method to get the JSON value using key from testCase.json
 * @param object
 * @param keyName
 * @return
 * @throws Exception
 */
public String getData(JSONObject object, String keyName) throws Exception {
	try {
		return object.get(keyName.toLowerCase()).toString();
	} catch (Exception e) {
		e.printStackTrace();
		
		throw new Exception(keyName + " not found");
	}
}

}
