package basePackage.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
//import com.aventstack.extentreports.ResourceCDN;
import com.aventstack.extentreports.gherkin.model.Feature;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
//import com.aventstack.extentreports.reporter.ExtentSparkReporter;
//import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import basePackage.steps.CommonFunctions;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import io.cucumber.datatable.DataTable;

public class ExtentReportListener {
	public static String reportName = null;
	public static String appName = null;
	public static String directoryLocation = null;
	public static Path apiValidationSheetPath = null;

	public synchronized ExtentReports setUp(String reportAppName) {
		try {
			ExtentSparkReporter report = null;
			ExtentReports extent = null;
			String reportLocation;
			appName = reportAppName;
			reportName = reportAppName.trim();
			// String directoryLocation;
			Calendar cal = Calendar.getInstance();
			DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy");
			String cal1 = dateFormat.format(cal.getTime());
			DateFormat dateFormat2 = new SimpleDateFormat("MM_dd_yyyy HHmmss");
			String cal2 = dateFormat2.format(cal.getTime());
			String currentDir = System.getProperty("user.dir");

			if (ITestListenerImpl.defaultConfigProperty.get().getProperty("CICD").equalsIgnoreCase("N")) {
				directoryLocation = ITestListenerImpl.defaultConfigProperty.get().getProperty("latestReportLocation")
						+ File.separator + "Reports" + File.separator + "DetailedReport" + File.separator;
			} else {
				directoryLocation = ITestListenerImpl.defaultConfigProperty.get()
						.getProperty("CICDlatestReportLocation") + File.separator + "Reports" + File.separator
						+ "DetailedReport" + File.separator;
			}
			
			if (ITestListenerImpl.defaultConfigProperty.get().getProperty("SendMail")
					.equalsIgnoreCase("CustomReport")) {

				System.out.println("Not Deleting Extent Report since Sendmail Flag is CustomReport");
			}
			// else { // deleteFilesInDirectory(directoryLocation);
			// FileUtils.cleanDirectory(new File(directoryLocation)); }
			reportLocation = directoryLocation + File.separator + "Complete Report.html";
			System.out.println("Report is generated in " + reportLocation);
			report = new ExtentSparkReporter(reportLocation);
			report.config().setReportName(reportName);
			report.config().setTheme(Theme.STANDARD);
			System.out.println("Extent Report location initialized . . .");
			// report.start();
			extent = new ExtentReports();
			extent.attachReporter(report);
			extent.setSystemInfo("Application", ITestListenerImpl.defaultConfigProperty.get().getProperty("AppName"));
			extent.setSystemInfo("Operating System", System.getProperty("os.name"));
			extent.setSystemInfo("User Name", System.getProperty("user.name"));
			System.out.println("System Info. set in Extent Report");
			return extent;
		} catch (Exception e) {
			System.out.println("In catch Extent Setup");
			e.printStackTrace();
			return null;
		}
	}

	public String takeBackUp() {
		String reportLocation = "c:" + File.separator + "bdd" + File.separator + appName + File.separator + appName
				+ ".html";
		String backUpLocation = "c:" + File.separator + "bdd" + File.separator + appName + File.separator
				+ getcurrentdate() + File.separator + appName + getcurrentdateandtime() + ".html";
		try {
			copyFiles(reportLocation, backUpLocation);
		} catch (Exception e) {
			System.out.println("Failed to take up a back up of file " + reportLocation + "into backup location of"
					+ backUpLocation);
		}
		return backUpLocation;
	}

	public void logResult(String teststatus, WebDriver driver, ExtentTest extenttest, String message,
			Throwable throwable) {
		switch (teststatus) {
		case "FAIL":
			extenttest.fail(MarkupHelper.createLabel(message, ExtentColor.RED));
			MyLogger.info("FAILURE--> " + message, throwable);
			if (throwable != null) {
				// extenttest.error(throwable.fillInStackTrace());
				extenttest.fail(throwable.fillInStackTrace());
			}
			if (driver != null) {
				// driver.quit();
				if (throwable != null) {
					Assert.fail("Failed with below message as " + throwable.fillInStackTrace());
				} else {
					Assert.fail("Failed with below message as " + message);
				}
			}
			break;

		case "PASS":
			extenttest.pass(MarkupHelper.createLabel(message, ExtentColor.GREEN));
			MyLogger.info("SUCCESS--> " + message);
			break;
		case "INFO":
			extenttest.info(MarkupHelper.createCodeBlock(message, CodeLanguage.JSON));
			MyLogger.info("SUCCESS--> " + message);
			break;
		default:
			break;
		}
	}

	public void logTestData(DataTable testData, ExtentTest extenttest) {
		List<Map<String, String>> data = testData.asMaps(String.class, String.class);
		String message = "";
		for (Map<String, String> datum : data) {
			for (Map.Entry<String, String> entry : datum.entrySet()) {
				if (entry.getKey().equalsIgnoreCase("Password") || entry.getKey().equalsIgnoreCase("Pwd")
						|| entry.getKey().equalsIgnoreCase("Pass")) {
					message = entry.getKey() + " : " + "*******";
				} else {
					message = entry.getKey() + " : " + entry.getValue();
				}
				extenttest.info(MarkupHelper.createLabel(message, ExtentColor.YELLOW));
			}
		}
	}

	public void logTestData(String testData, ExtentTest extenttest) {
		extenttest.info(MarkupHelper.createLabel(testData, ExtentColor.YELLOW));
	}

	public String captureScreenShot(WebDriver driver) throws IOException {
		try {
			TakesScreenshot screen = (TakesScreenshot) driver;
			File src = screen.getScreenshotAs(OutputType.FILE);
			String reportImgtDest = "." + File.separator + "Screenshots" + File.separator + getcurrentdateandtime()
					+ CommonFunctions.browserName.get().toUpperCase() + +Thread.currentThread().getId() + ".png";
			String screenshotDest = directoryLocation + reportImgtDest;
			// MyLogger.info("Screenshots are saved in " + screenshotDest);
			FileUtils.copyFile(src, new File(screenshotDest));
			return reportImgtDest;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String captureScreenShotBase64(WebDriver driver) throws IOException {
		try {
			String dest;
			TakesScreenshot screen = (TakesScreenshot) driver;
			File src = screen.getScreenshotAs(OutputType.FILE);
			dest = directoryLocation + File.separator + "Screenshots" + File.separator + getcurrentdateandtime()
					+ CommonFunctions.browserName.get().toUpperCase() + +Thread.currentThread().getId() + ".png";
			File target = new File(dest);
			FileUtils.copyFile(src, target);
			byte[] imageBytes = IOUtils.toByteArray(new FileInputStream(dest));
			// byte[] imageBytes = driver.getFullPageScreenshotAs(OutputType.BYTES);
			return Base64.getEncoder().encodeToString(imageBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String captureScreenShotFullImg(WebDriver driver) throws IOException {
		try {
			String reportImgtDest = "." + File.separator + "Screenshots" + File.separator + getcurrentdateandtime()
					+ CommonFunctions.browserName.get().toUpperCase() + +Thread.currentThread().getId();
			String screenshotDest = directoryLocation + reportImgtDest;
			Shutterbug.shootPage(driver, Capture.FULL, 500, true).save(screenshotDest);
			List<String> results = new ArrayList<String>();
			File[] files = new File(screenshotDest).listFiles();
			for (File file : files) {
				if (file.isFile()) {
					results.add(file.getName());
				}
			}
			MyLogger.info("Screenshots are saved in " + screenshotDest + File.separator + results.get(0));
			return reportImgtDest + File.separator + results.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getFeatureFileNameFromScenarioId(Feature feature) {
		String featureName = feature.toString();
		// String rawFeatureName = scenario.getId().split(";")[0].replace("-", " ");
		// featureName = featureName + rawFeatureName.substring(0, 1).toUpperCase() +
		// rawFeatureName.substring(1);
		return featureName;
	}

	private static String getcurrentdate() {
		String str = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date date = new Date();
			str = dateFormat.format(date);
			str = str.replace("/", "-");
			System.out.println(str);

		} catch (Exception e) {
		}
		return str;
	}

	private static String getcurrenttime() {
		String str = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			str = dateFormat.format(date);
			// str = str.replace(" ", "").replaceAll("/", "").replaceAll(":", "");
		} catch (Exception e) {

		}
		return str;
	}

	private static String getcurrentdateandtime() {
		String str = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss:SSS");
			Date date = new Date();
			str = dateFormat.format(date);
			str = str.replace(" ", "").replaceAll("/", "").replaceAll(":", "");
		} catch (Exception e) {

		}
		return str;
	}

	private static void moveFiles(String src, String dest) {
		try {
			FileUtils.moveFile(new File(src), new File(dest));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void copyFiles(String src, String dest) {
		try {
			if ((src != null) && (dest != null)) {
				FileUtils.copyFile(new File(src), new File(dest));
			} else
				System.out.println("source and destination paths are null");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Unable to copy the files to dest folder " + dest);
			System.out.println("Check your src path properly " + src);
		}
	}

	private static void deleteFilesInDirectory(String directoryLocation) {
		try {
			if (directoryLocation != null) {
				File src = new File(directoryLocation);
				File[] files = src.listFiles();
				System.out.println("Number of files in the directory" + directoryLocation + "are " + files.length);
				if (files.length >= 1) {
					for (File file : files) {
						if (file.isFile()) {
							file.delete();
							System.out.println(
									"Successfully deleted the files in the diredtory Location " + directoryLocation);
						}
					}
				} else
					System.out.println("No files are present in " + directoryLocation + " to be deleted");
			} else
				System.out.println("Directory location is null");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(
					"Check whether the directory is right and try again with correct directory path not file path or this is the first time we are running the feauture file so the folder has not been created ");
		}
	}

	public void copyApiTemplate() throws Throwable {
		// Path apiValidationSheetPath = null;
		try {
			FileSystem system = FileSystems.getDefault();
			String originalPath = System.getProperty("user.dir");
			Calendar cal = Calendar.getInstance();
			DateFormat dateFormat2 = new SimpleDateFormat("MMddyyyy_HHmmss");
			String cal2 = dateFormat2.format(cal.getTime());
			// System.out.println(originalPath+File.separator+"ResponseValidation.xlsx");
			Path original = system.getPath(originalPath + File.separator + "ResponseValidation.xlsx");
			apiValidationSheetPath = system
					.getPath(directoryLocation + File.separator + "ResponseValidation" + cal2 + ".xlsx");
			System.out.println(apiValidationSheetPath);
			// Throws an exception if the original file is not found.
			Files.copy(original, apiValidationSheetPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			System.out.println("ERROR");
			MyLogger.info("Exception Occured : " + e.getMessage());
			throw e;
		}
	}

	public String getApiValidationReportPath() {
		return apiValidationSheetPath.toString();
	}

	public String getDetailedReportLocation() {
		return directoryLocation;
	}

	public String getOperatingSystem() {
		String os = System.getProperty("os.name");
		// System.out.println("Using System Property: " + os);
		return os;
	}
}
