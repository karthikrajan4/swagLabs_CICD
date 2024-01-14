package basePackage.utilities;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class File_Transfer {
	public static Calendar cal = Calendar.getInstance();
	public static DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy HHmmss");
	public static String cal1 = dateFormat.format(cal.getTime());
	public static String backUpReportName = "Report " + cal1;
	public static String reportAppname = ITestListenerImpl.defaultConfigProperty.get().getProperty("AppName");
	public static String latestReportLocation;
	public static String mailReportName;
	public static String backUpLocation;
	public static File reportsFolder;
	
	public static void takeBackUp() {
		try {
			if (ITestListenerImpl.defaultConfigProperty.get().getProperty("CICD")
					.equalsIgnoreCase("N")) {
				latestReportLocation = ITestListenerImpl.defaultConfigProperty.get()
						.getProperty("latestReportLocation");
				mailReportName = ReportGenerator.currentDirExcel;
				backUpLocation = ITestListenerImpl.defaultConfigProperty.get().getProperty("backUpReportLocation")
						+ File.separator + "Report " + cal1;
				reportsFolder = new File(latestReportLocation);
			} else {
				latestReportLocation = ITestListenerImpl.defaultConfigProperty.get()
						.getProperty("CICDlatestReportLocation");
				mailReportName = ReportGenerator.currentDirExcel;
				backUpLocation = ITestListenerImpl.defaultConfigProperty.get()
						.getProperty("CICDbackUpReportLocation")
						+ File.separator + "Report " + cal1;
				reportsFolder = new File(latestReportLocation);
			}
			if (reportsFolder.isDirectory() && reportsFolder.list().length == 0) {
				System.out.println("======Cannot Take backUp==========");
				System.out.println("No files are present in: " + mailReportName);
			} else {
				// copyFiles(reportLocation, backUpLocation);
				copyDirectory(mailReportName, backUpLocation);
			}
		} catch (Exception e) {
			System.out.println("Failed to take up a back up of file " + mailReportName + "into backup location of"
					+ backUpLocation);
			// e.printStackTrace();
		}
	}

	public static void copyFiles(String src, String dest) {
		try {
			if ((src != null) && (dest != null)) {
				List<String> allFiles = getFileNames(src);
				for (String eachFile : allFiles) {
					String srcFile = src + File.separator + eachFile;
					FileUtils.copyFileToDirectory(new File(srcFile), new File(dest));
				}
				FileUtils.cleanDirectory(reportsFolder);
			} else
				System.out.println("source and destination paths are null");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void copyDirectory(String src, String dest) {
		try {
			if ((src != null) && (dest != null)) {
				FileUtils.copyDirectory(new File(src), new File(dest));
				// FileUtils.cleanDirectory(reportsFolder);
				System.out.println("======BackUp Taken==========");
			} else
				System.out.println("source and destination paths are null");
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("======BackUp Not Taken==========");
		}
	}

	public static List<String> getFileNames(String dirPath) {
		List<String> fileNames = new ArrayList<String>();
		File[] files = new File(dirPath).listFiles();
		for (File file : files) {
			if (file.isFile()) {
				fileNames.add(file.getName());
			}
		}
		return fileNames;
	}
}