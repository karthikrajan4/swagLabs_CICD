package basePackage.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.model.Device;
import com.aventstack.extentreports.model.ExceptionInfo;
import com.aventstack.extentreports.model.Test;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
//import utils.EmailUtils;

public class ITestListenerImpl extends ExtentReportListener implements ITestListener, ISuiteListener {
	// private static ExtentReports extent;
	// private String suiteName;
	public static ExtentReports extent;
	public static String suiteName;
	public static String testName;
	public static String browserName;
	public static String envURL;
	// public static ExtentTest scenario;
	public ExtentTest scenario;
	// public ExtentTest extentStep ;
	public static ThreadLocal<ReportGenerator> rg = new ThreadLocal<ReportGenerator>();
	public static ThreadLocal<Properties> defaultConfigProperty = new ThreadLocal<Properties>();
	public static ThreadLocal<Properties> objectRepoProperty = new ThreadLocal<Properties>();
	// public static ThreadLocal<MyLogger> logging = new ThreadLocal<MyLogger>();

	public void onStart(ISuite suite) {
		try {
			System.out.println(
					"=========================================On Execution Start=========================================");
			defaultConfigProperty.set(new ConfigReader().getDefaultProp());
			objectRepoProperty.set(new ConfigReader().getObjectRepoProp());
			System.out.println("AppName is : " + defaultConfigProperty.get().getProperty("AppName"));
			System.out.println("TestNg Suite Name is: " + suite.getName());
			System.out.println("CICD Run is: " + defaultConfigProperty.get().getProperty("CICD"));
			if (defaultConfigProperty.get().getProperty("SendMail").equalsIgnoreCase("CustomReport")) {
				String html = rg.get().excelMailReport();
				SendingMail.sendMail(html);
				System.exit(0);
			}
			// Delete Results Folder
			if (defaultConfigProperty.get().getProperty("reportAppend").equalsIgnoreCase("N")) {
				rg.get().reportDel();
			}
			extent = setUp(defaultConfigProperty.get().getProperty("AppName"));
			rg.set(new ReportGenerator());
			// //Delete Results Folder
			// if
			// (defaultConfigProperty.get().getProperty("reportDelete").equalsIgnoreCase("Y"))
			// {
			// rg.get().reportDel();
			// }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onFinish(ISuite suite) {
		try {
			extentReporting();
			extent.flush();
			// System.out.println("Generated Report. . .");
			rg.get().overallStatus();
			String html = rg.get().excelMailReport();
			if (defaultConfigProperty.get().getProperty("SendMail").equalsIgnoreCase("Y")) {
				SendingMail.sendMail(html);
			}
			System.out.println(
					"=========================================On Execution End=========================================");
		} catch (Exception e) {
			MyLogger.info("============Exception in OnFinish Suite=================");
			System.out.println("Exception:" + e.getMessage());
		} finally {
			if (defaultConfigProperty.get().getProperty("CICD").equalsIgnoreCase("N")) {
				File_Transfer.takeBackUp();
			}
		}
	}

	private void extentReporting() throws RowsExceededException, WriteException, BiffException, IOException {
		// TODO Auto-generated method stub
		List<Test> testList = new ArrayList();
		List<ExceptionInfo> exceptions = new ArrayList();
		Set<Device> devSet = new HashSet();
		String testCaseName = "";
		String comments = "";
		String testCaseStatus = "";
		boolean status;
		String browserName = "";
		int size = extent.getReport().getTestList().size();
		testList = extent.getReport().getTestList();
		for (Test testCase : testList) {
			testCaseName = testCase.getName();
			testCaseStatus = testCase.getStatus().toString();
			if (testCaseStatus.equalsIgnoreCase("Pass")) {
				status = true;
			} else {
				status = false;
			}
			devSet = testCase.getDeviceSet();
			for (Device d : devSet) {
				browserName = d.getName();
			}
			String testCaseException = "";
			if (status == false) {
				testCaseException += "Failed at Step: ";
				List<Test> children = testCase.getChildren();
				for (Test child : children) {
					if (child.getStatus().toString().equalsIgnoreCase("Fail")) {
						testCaseException += child.getName() + ". ";
					}
				}
			}
			ReportGenerator.reportExtent(status, comments, testCaseName, browserName, testCaseException);
		}
		System.out.println("==========>Created Custom ExcelReport from ExtentReport<==========");
	}

	public void onTestStart(ITestResult result) {

	}

	public void onTestSuccess(ITestResult result) {

	}

	public void onTestFailure(ITestResult result) {

	}

	public void onTestSkipped(ITestResult result) {

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	public void onStart(ITestContext context) {
		testName = context.getName();
		browserName = context.getCurrentXmlTest().getParameter("browser");
		System.out.println("Test Name is: " + testName + " Browser Name is: " + browserName);
	}

	public static String getTestName() {
		return testName;
	}
	// public void onFinish(ITestContext context) {
	// extent.flush(); System.out.println("Generated Report. . ."); if
	// (!isDebugFlow.toLowerCase().contains("yes")) { // Taking backup of the files
	// for next run. String reportLocation = takeBackUp();
	// }
}
