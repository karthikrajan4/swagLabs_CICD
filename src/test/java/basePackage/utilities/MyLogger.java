package basePackage.utilities;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.logging.log4j.*;
import org.openqa.selenium.WebDriver;

public class MyLogger {
	public static Logger logger = null;

	public synchronized static void startTest(String testName) {
		// logger.set(LogManager.getLogger());
		// startLog(\\logs, testName);
		// sTestCaseName = sTestCaseName.replaceAll("[^a-zA-Z0-9]",
		// "_").replaceAll("_+", "_");
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy");
		String cal1 = dateFormat.format(cal.getTime());
		Calendar cal2 = Calendar.getInstance();
		DateFormat dateFormat2 = new SimpleDateFormat("MM_dd_yyyy_HHmmss");
		String cal3 = dateFormat2.format(cal2.getTime());
		ConfigReader config = new ConfigReader();
		config.getDefaultProp().getProperty("AppName");
		String appName = config.getDefaultProp().getProperty("AppName");
		String currentDir;
		if (config.getDefaultProp().getProperty("CICD").equalsIgnoreCase("N")) {
			currentDir = config.getDefaultProp().getProperty("latestReportLocation") + "\\" + "Reports";
			System.out.println("LogFile is : " + currentDir + "\\Logs\\" + testName + "_" + cal3);
			setlogger(currentDir + "\\Logs\\" + testName + "_" + cal3);
		} else {
			currentDir = config.getDefaultProp().getProperty("CICDlatestReportLocation") + "\\" + "Reports";
			System.out.println("LogFile is : " + currentDir + "\\Logs\\" + testName);
			setlogger(currentDir + "\\Logs\\" + testName);
		}
		// if (config.getDefaultProp().getProperty("CICD").equalsIgnoreCase("N")) {
		// setlogger(currentDir+\\Logs\\+testName+"_"+cal3);
		// }else {
		// System.out.println(currentDir+\\Logs\\+testName);
		// setlogger(currentDir+\\Logs\\+testName );
		// }
		// startLog(currentDir+\\Logs, testName);
		info("\n\n************** Runner Started : " + testName + "**************\n");
	}

	public static void endTest(String sTestCaseName) {
		info("\n\n************** Runner End : " + sTestCaseName + "**************\n");
	}

	private static void startLog(String dirPath, String testName) {
		// int noOfFiles = 0;
		// File dir = new File(dirPath);
		// if (dir.exists()) {
		// int count = 0;
		// for (File file : dir.listFiles()) {
		// if (file.isFile() && file.getName().endsWith(".log") &&
		// file.getName().contains(testCaseName)) {
		// count++;
		// }
		// }
		// noOfFiles = count;
		// }
		// noOfFiles++;
		// String logFileName = testCaseName + "_" + noOfFiles;
		// ThreadContext.put("logFilename", testName);
		// System.setProperty("filename", testCaseName);
		// System.setProperty("logDir", "logs");
		// System.setProperty("logFile", "testCaseName");
		// DOMConfigurator.configure("log4j.xml");
	}

	// public static Logger logger { return logger; }

	public static void setlogger(String testName) {
		logger = LogManager.getLogger();
		ThreadContext.put("logFilename", testName);
	}

	public static String getCallInfo() {
		String callInfo;
		String className = Thread.currentThread().getStackTrace()[3].getClassName();
		String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
		int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
		callInfo = className + ":" + methodName + " " + lineNumber + "==>  ";
		return callInfo;
	}

	public static void trace(Object message) {
		logger.trace(message);
	}

	public static void trace(Object message, Throwable t) {
		logger.trace(message, t);
	}

	public static void debug(Object message) {
		logger.debug(getCallInfo() + message);
	}

	public static void debug(Object message, Throwable t) {
		logger.debug(getCallInfo() + message, t);
	}

	public static void error(Object message) {
		logger.error(getCallInfo() + message);
	}

	public static void error(Object message, Throwable t) {
		logger.error(getCallInfo() + message, t);
	}

	public static void fatal(Object message) {
		logger.fatal(getCallInfo() + message);
	}

	public static void fatal(Object message, Throwable t) {
		logger.fatal(getCallInfo() + message, t);
	}

	public static void info(Object message) {
		logger.info(getCallInfo() + message);
	}

	public static void info(Object message, Throwable t) {
		logger.info(getCallInfo() + message, t);
	}

	public static void warn(Object message) {
		logger.warn(getCallInfo() + message);
	}

	public static void warn(Object message, Throwable t) {
		logger.warn(getCallInfo() + message, t);
	}
}
