package basePackage.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import basePackage.steps.CommonFunctions;

import org.openqa.selenium.remote.DesiredCapabilities;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ReportGenerator extends CommonFunctions {
	// static String selectedApp = "";
	static String launchURL = "";
	public static String appName = ITestListenerImpl.defaultConfigProperty.get().getProperty("AppName");
	public static String scenarioName = "";
	// static String featureName = "";
	public static String elementName = "";
	public static String ErrType = "";
	public static String comments = "";
	public static String filepath = "";
	public static String currentDirExcel = "";
	public static ThreadLocal<Integer> stepFailCnt = new ThreadLocal<Integer>();
	public static ThreadLocal<Integer> stepPassCnt = new ThreadLocal<Integer>();
	// public static ThreadLocal<String> comments = new ThreadLocal<String>();
	static int Critical_Error_Counter = 0;
	static String User_Name = ITestListenerImpl.defaultConfigProperty.get().getProperty("MailFromTeam");
	static String teamEmailID = ITestListenerImpl.defaultConfigProperty.get().getProperty("MailFromTeamID");
	static int Critical_Counter = 0;
	static int Failed_Case_Counter = 0;
	static int Passed_Case_Counter = 0;

	// public static synchronized Reportgenerator getReport() {
	// try {
	// Reportgenerator report = new Reportgenerator();
	// return report;
	// } catch(Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// }

	public static synchronized void reportExtent(Boolean status, String Cmnt, String scenarioName, String browserName,

			String testCaseException) throws IOException, RowsExceededException, WriteException, BiffException {

		/*
		 * 
		 * System.out.println("After Report - Status - " + status + " Browser: " +
		 * 
		 * browserName + " scenarioName - " + scenarioName + " Thread: " +
		 * 
		 * Thread.currentThread().getId());
		 * 
		 */

		if (status == false) {

			// comments = Cmnt;

			comments = "";

			// System.out.println("comments " + comments);

		} else {

			comments = "";

		}

		Calendar cal = Calendar.getInstance();

		DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy");

		String cal1 = dateFormat.format(cal.getTime());
		if (ITestListenerImpl.defaultConfigProperty.get().getProperty("CICD").equalsIgnoreCase("N")) {

			currentDirExcel = ITestListenerImpl.defaultConfigProperty.get().getProperty("latestReportLocation")
					+ File.separator + File.separator + "Reports";

		} else {

			currentDirExcel = ITestListenerImpl.defaultConfigProperty.get().getProperty("CICDlatestReportLocation")

					+ File.separator + File.separator + "Reports";

		}

		String filepath = currentDirExcel + File.separator + "Final Report.xls";

		File ifilepath = new File(currentDirExcel + File.separator + "Final Report.xls");

		String ofilepath = currentDirExcel + File.separator + "Final Report_temp.xls";

		File logfile = new File(filepath);// Created object of java File

		if (!logfile.exists()) { // if1

			WritableWorkbook workbook = Workbook.createWorkbook(new File(filepath));

			WritableSheet sheet = workbook.createSheet("Report", 0);

			sheet.setName("Report");

			WritableFont arialfont = new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD);

			WritableCellFormat cellFormat = new WritableCellFormat(arialfont);

			cellFormat.setBackground(Colour.ICE_BLUE);

			cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

			sheet.addCell(new Label(0, 0, "S.No", cellFormat));

			sheet.addCell(new Label(1, 0, "Test Case", cellFormat));

			sheet.addCell(new Label(2, 0, "Overall Status", cellFormat));

			sheet.addCell(new Label(3, 0, "Chrome", cellFormat));

			sheet.addCell(new Label(4, 0, "IE", cellFormat));

			sheet.addCell(new Label(5, 0, "Edge", cellFormat));

			sheet.addCell(new Label(6, 0, "FireFox", cellFormat));

			// sheet.addCell(new Label(6, 0, "Fail Reason", cellFormat));

			sheet.addCell(new Label(7, 0, "Comments", cellFormat));

			workbook.write();

			workbook.close();

		} // if1 ends

		Workbook wb1 = Workbook.getWorkbook(ifilepath);

		WritableWorkbook wbcopy = Workbook.createWorkbook(new File(ofilepath), wb1);

		WritableSheet sheet1 = wbcopy.getSheet(0);

		Sheet sheet = wb1.getSheet(0);

		int newrow = sheet.getRows();

		// System.out.println("newrow: " + newrow);

		sheet1.setName("Report");

		WritableFont arialfont1 = new WritableFont(WritableFont.ARIAL, 10);

		WritableCellFormat cellFormat1 = new WritableCellFormat(arialfont1);

		cellFormat1.setBorder(Border.ALL, BorderLineStyle.THIN);

		WritableCellFormat passcellFormat = new WritableCellFormat(arialfont1);

		passcellFormat.setBackground(Colour.LIGHT_GREEN);

		passcellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

		WritableCellFormat failcellFormat = new WritableCellFormat(arialfont1);

		failcellFormat.setBackground(Colour.RED);

		failcellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

		int col = 0;

		int widthInChars = 12;

		/*
		 * 
		 * // =================COLUMN 0 S.NO========================
		 * 
		 * sheet1.setColumnView(col, widthInChars); sheet1.addCell(new Label(col,
		 * 
		 * newrow, Integer.toString(newrow), cellFormat1)); // =================COLUMN 1
		 * 
		 * SCENARIO========================= System.out.println("In Column1 Scenario");
		 * 
		 * col = 1; widthInChars = 70; sheet1.setColumnView(col, widthInChars);
		 * 
		 */

		// System.out.println("get content: "+ sheet.getCell(1,

		// 1).getContents().equalsIgnoreCase(scenarioName));

		if (newrow == 1) {

			// =================COLUMN 0 S.NO========================

			col = 0;

			widthInChars = 12;

			sheet1.setColumnView(col, widthInChars);

			sheet1.addCell(new Label(col, newrow, Integer.toString(newrow), cellFormat1));

			// =================COLUMN 1 SCENARIO=========================

			// System.out.println("In Column1 Scenario");

			col = 1;

			widthInChars = 70;

			sheet1.setColumnView(col, widthInChars);

			sheet1.addCell(new Label(col, newrow, scenarioName, cellFormat1));

			if (status) { // if2

				if (browserName.equalsIgnoreCase("Chrome")) {

					col = 3;

					widthInChars = 15;

					sheet1.setColumnView(col, widthInChars);

					sheet1.addCell(new Label(col, newrow, "PASS", passcellFormat));

				} else if (browserName.equalsIgnoreCase("IE")) {

					col = 4;

					widthInChars = 15;

					sheet1.setColumnView(col, widthInChars);

					sheet1.addCell(new Label(col, newrow, "PASS", passcellFormat));

				} else if (browserName.equalsIgnoreCase("Edge")) {

					col = 5;

					widthInChars = 15;

					sheet1.setColumnView(col, widthInChars);

					sheet1.addCell(new Label(col, newrow, "PASS", passcellFormat));

				} else if (browserName.equalsIgnoreCase("FireFox")) {

					col = 6;

					widthInChars = 15;

					sheet1.setColumnView(col, widthInChars);

					sheet1.addCell(new Label(col, newrow, "PASS", passcellFormat));

				}

				else {

					MyLogger.info("Invalid Browser while creating Excel Report");

				}

				col = 7;

				widthInChars = 20;

				sheet1.setColumnView(col, widthInChars);

				sheet1.addCell(new Label(col, newrow, "", cellFormat1));

				comments = "";

			} else if (!status) {

				if (browserName.equalsIgnoreCase("Chrome")) {

					col = 3;

					widthInChars = 15;

					sheet1.setColumnView(col, widthInChars);

					sheet1.addCell(new Label(col, newrow, "FAIL", failcellFormat));

				} else if (browserName.equalsIgnoreCase("IE")) {

					col = 4;

					widthInChars = 15;

					sheet1.setColumnView(col, widthInChars);

					sheet1.addCell(new Label(col, newrow, "FAIL", failcellFormat));

				} else if (browserName.equalsIgnoreCase("Edge")) {

					col = 5;

					widthInChars = 15;

					sheet1.setColumnView(col, widthInChars);

					sheet1.addCell(new Label(col, newrow, "FAIL", failcellFormat));

				} else if (browserName.equalsIgnoreCase("FireFox")) {

					col = 6;

					widthInChars = 15;

					sheet1.setColumnView(col, widthInChars);

					sheet1.addCell(new Label(col, newrow, "FAIL", failcellFormat));

				} else {

					MyLogger.info("Invalid Browser while creating Excel Report");

				}

				col = 7;

				widthInChars = 20;

				sheet1.setColumnView(col, widthInChars);

				sheet1.addCell(new Label(col, newrow, comments, cellFormat1));

				/*
				 * 
				 * col = 7; widthInChars = 20; sheet1.setColumnView(col, widthInChars);
				 * 
				 * sheet1.addCell(new Label(col, newrow, testCaseException, cellFormat1));
				 * 
				 */

			}

		} else {

			int duplicateScenarioRow = 0;

			for (int i = 1; i < newrow; i++) {

				if (sheet.getCell(1, i).getContents().equalsIgnoreCase(scenarioName)) {

					duplicateScenarioRow = i;

				}

			}

			// System.out.println("duplicateScenarioRow: " + duplicateScenarioRow);

			if (duplicateScenarioRow == 0) {

				// =================COLUMN 0 S.NO========================

				col = 0;

				widthInChars = 12;

				sheet1.setColumnView(col, widthInChars);

				sheet1.addCell(new Label(col, newrow, Integer.toString(newrow), cellFormat1));

				// =================COLUMN 1 SCENARIO=========================

				col = 1;

				widthInChars = 70;

				sheet1.setColumnView(col, widthInChars);

				sheet1.addCell(new Label(col, newrow, scenarioName, cellFormat1));

				if (status) { // if2

					if (browserName.equalsIgnoreCase("Chrome")) {

						col = 3;

						widthInChars = 15;

						sheet1.setColumnView(col, widthInChars);

						sheet1.addCell(new Label(col, newrow, "PASS", passcellFormat));

					} else if (browserName.equalsIgnoreCase("IE")) {

						col = 4;

						widthInChars = 15;

						sheet1.setColumnView(col, widthInChars);

						sheet1.addCell(new Label(col, newrow, "PASS", passcellFormat));

					} else if (browserName.equalsIgnoreCase("Edge")) {

						col = 5;

						widthInChars = 15;

						sheet1.setColumnView(col, widthInChars);

						sheet1.addCell(new Label(col, newrow, "PASS", passcellFormat));

					} else if (browserName.equalsIgnoreCase("FireFox")) {

						col = 6;

						widthInChars = 15;

						sheet1.setColumnView(col, widthInChars);

						sheet1.addCell(new Label(col, newrow, "PASS", passcellFormat));

					}

					col = 7;

					widthInChars = 20;

					sheet1.setColumnView(col, widthInChars);

					sheet1.addCell(new Label(col, newrow, "", cellFormat1));

					comments = "";

				} else if (!status) {

					if (browserName.equalsIgnoreCase("Chrome")) {

						col = 3;

						widthInChars = 15;

						sheet1.setColumnView(col, widthInChars);

						sheet1.addCell(new Label(col, newrow, "FAIL", failcellFormat));

					} else if (browserName.equalsIgnoreCase("IE")) {

						col = 4;

						widthInChars = 15;

						sheet1.setColumnView(col, widthInChars);

						sheet1.addCell(new Label(col, newrow, "FAIL", failcellFormat));

					} else if (browserName.equalsIgnoreCase("Edge")) {

						col = 5;

						widthInChars = 15;

						sheet1.setColumnView(col, widthInChars);

						sheet1.addCell(new Label(col, newrow, "FAIL", failcellFormat));

					} else if (browserName.equalsIgnoreCase("FireFox")) {

						col = 6;

						widthInChars = 15;

						sheet1.setColumnView(col, widthInChars);

						sheet1.addCell(new Label(col, newrow, "FAIL", failcellFormat));

					}

					col = 7;

					widthInChars = 20;

					sheet1.setColumnView(col, widthInChars);

					sheet1.addCell(new Label(col, newrow, comments, cellFormat1));

					/*
					 * 
					 * col = 7; widthInChars = 20; sheet1.setColumnView(col, widthInChars);
					 * 
					 * sheet1.addCell(new Label(col, newrow, testCaseException, cellFormat1));
					 * 
					 */

				}

			} else {

				if (status) { // if2

					if (browserName.equalsIgnoreCase("Chrome")) {

						col = 3;

						widthInChars = 15;

						sheet1.setColumnView(col, widthInChars);

						sheet1.addCell(new Label(col, duplicateScenarioRow, "PASS", passcellFormat));

					} else if (browserName.equalsIgnoreCase("IE")) {

						col = 4;

						widthInChars = 15;

						sheet1.setColumnView(col, widthInChars);

						sheet1.addCell(new Label(col, duplicateScenarioRow, "PASS", passcellFormat));

					} else if (browserName.equalsIgnoreCase("Edge")) {

						col = 5;

						widthInChars = 15;

						sheet1.setColumnView(col, widthInChars);

						sheet1.addCell(new Label(col, duplicateScenarioRow, "PASS", passcellFormat));

					} else if (browserName.equalsIgnoreCase("FireFox")) {

						col = 6;

						widthInChars = 15;

						sheet1.setColumnView(col, widthInChars);

						sheet1.addCell(new Label(col, duplicateScenarioRow, "PASS", passcellFormat));

					}

					col = 7;

					widthInChars = 20;

					sheet1.setColumnView(col, widthInChars);

					sheet1.addCell(new Label(col, duplicateScenarioRow, "", cellFormat1));

					comments = "";

				} else if (!status) {

					if (browserName.equalsIgnoreCase("Chrome")) {

						col = 3;

						widthInChars = 15;

						sheet1.setColumnView(col, widthInChars);

						sheet1.addCell(new Label(col, duplicateScenarioRow, "FAIL", failcellFormat));

					} else if (browserName.equalsIgnoreCase("IE")) {

						col = 4;

						widthInChars = 15;

						sheet1.setColumnView(col, widthInChars);

						sheet1.addCell(new Label(col, duplicateScenarioRow, "FAIL", failcellFormat));

					} else if (browserName.equalsIgnoreCase("Edge")) {

						col = 5;

						widthInChars = 15;

						sheet1.setColumnView(col, widthInChars);

						sheet1.addCell(new Label(col, duplicateScenarioRow, "FAIL", failcellFormat));

					} else if (browserName.equalsIgnoreCase("FireFox")) {

						col = 6;

						widthInChars = 15;

						sheet1.setColumnView(col, widthInChars);

						sheet1.addCell(new Label(col, duplicateScenarioRow, "FAIL", failcellFormat));

					}

					col = 7;

					widthInChars = 20;

					sheet1.setColumnView(col, widthInChars);

					sheet1.addCell(new Label(col, duplicateScenarioRow, comments, cellFormat1));

					col = 6;

					/*
					 * 
					 * widthInChars = 20; sheet1.setColumnView(col, widthInChars);
					 * 
					 * sheet1.addCell(new Label(col, duplicateScenarioRow, testCaseException,
					 * 
					 * cellFormat1));
					 * 
					 */

				}

			}

		}

		// =================COLUMN 3,4,5,6,7 SCENARIO=========================

		// System.out.println("In Column 3,4,5,6,7 Scenario");

		wb1.close();

		wbcopy.write();

		wbcopy.close();

		logfile.delete();

		Workbook wb2 = Workbook.getWorkbook(new File(ofilepath));

		WritableWorkbook wbmain = Workbook.createWorkbook(new File(filepath), wb2);

		WritableSheet sheet2 = wbcopy.getSheet(0);

		sheet2.setName("Report");

		wbmain.write();

		wbmain.close();

		new File(ofilepath).delete();

	}

	public static synchronized boolean overallStatus()

			throws IOException, RowsExceededException, WriteException, BiffException {

		System.out.println("==========>Updating Overall Status in Excel Report<==========");

		Calendar cal = Calendar.getInstance();

		DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy");

		String cal1 = dateFormat.format(cal.getTime());

		// String currentDir = System.getProperty("user.dir");

		String currentDir;

		if (ITestListenerImpl.defaultConfigProperty.get().getProperty("CICD").equalsIgnoreCase("N")) {

			currentDir = ITestListenerImpl.defaultConfigProperty.get().getProperty("latestReportLocation")
					+ File.separator

					+ File.separator + "Reports";

		} else {

			currentDir = ITestListenerImpl.defaultConfigProperty.get().getProperty("CICDlatestReportLocation")
					+ File.separator

					+ File.separator + "Reports";

		}

		String filepath = currentDir + File.separator + "Final Report.xls";

		File ifilepath = new File(currentDirExcel + File.separator + "Final Report.xls");

		String ofilepath = currentDirExcel + File.separator + "Final Report_temp.xls";

		File logfile = new File(filepath);// Created object of java File

		if (!logfile.exists()) {

			System.out.println("Excel File not Present : " + filepath);

			return false;

		} // if1 ends

		else {

			Workbook wb1 = Workbook.getWorkbook(ifilepath);

			WritableWorkbook wbcopy = Workbook.createWorkbook(new File(ofilepath), wb1);

			WritableSheet sheet1 = wbcopy.getSheet(0);

			Sheet sheet = wb1.getSheet(0);

			int newrow = sheet.getRows();

			// System.out.println("TotalRows: " + newrow);

			WritableFont arialfont1 = new WritableFont(WritableFont.ARIAL, 10);

			WritableCellFormat cellFormat1 = new WritableCellFormat(arialfont1);

			cellFormat1.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat passcellFormat = new WritableCellFormat(arialfont1);

			passcellFormat.setBackground(Colour.LIGHT_GREEN);

			passcellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat failcellFormat = new WritableCellFormat(arialfont1);

			failcellFormat.setBackground(Colour.RED);

			failcellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

			int col = 2;

			int widthInChars = 20;

			sheet1.setColumnView(col, widthInChars);

			for (int i = 1; i < newrow; i++) {

				String ChromeStatus = sheet.getCell(3, i).getContents();

				String IEStatus = sheet.getCell(4, i).getContents();

				String EdgeStatus = sheet.getCell(5, i).getContents();

				String FireFoxStatus = sheet.getCell(6, i).getContents();

				if (!(ChromeStatus.equalsIgnoreCase("FAIL") || ChromeStatus.equalsIgnoreCase("PASS"))) {

					// =================COLUMN 3 Chrome NA=======================

					sheet1.addCell(new Label(3, i, "NA", cellFormat1));

				}

				if (!(IEStatus.equalsIgnoreCase("FAIL") || IEStatus.equalsIgnoreCase("PASS"))) {

					// =================COLUMN 4 IE NA=======================

					sheet1.addCell(new Label(4, i, "NA", cellFormat1));

				}
				if (!(EdgeStatus.equalsIgnoreCase("FAIL") || EdgeStatus.equalsIgnoreCase("PASS"))) {

					// =================COLUMN 4 Edge NA=======================

					sheet1.addCell(new Label(5, i, "NA", cellFormat1));

				}
				if (!(FireFoxStatus.equalsIgnoreCase("FAIL") || FireFoxStatus.equalsIgnoreCase("PASS"))) {

					// =================COLUMN 4 FireFox NA=======================

					sheet1.addCell(new Label(6, i, "NA", cellFormat1));

				}

			}

			for (int i = 1; i < newrow; i++) {

				String ChromeStatus = sheet.getCell(3, i).getContents();

				String IEStatus = sheet.getCell(4, i).getContents();

				String EdgeStatus = sheet.getCell(5, i).getContents();

				String FireFoxStatus = sheet.getCell(6, i).getContents();

				if (ChromeStatus.equalsIgnoreCase("FAIL") || IEStatus.equalsIgnoreCase("FAIL")
						|| EdgeStatus.equalsIgnoreCase("FAIL") || FireFoxStatus.equalsIgnoreCase("FAIL")) {

					// =================COLUMN 2 STATUS=======================

					sheet1.addCell(new Label(col, i, "FAILED", failcellFormat));

				} else {

					sheet1.addCell(new Label(col, i, "PASSED", passcellFormat));

				}

			}

			wb1.close();

			wbcopy.write();

			wbcopy.close();

			logfile.delete();

			Workbook wb2 = Workbook.getWorkbook(new File(ofilepath));

			WritableWorkbook wbmain = Workbook.createWorkbook(new File(filepath), wb2);

			WritableSheet sheet2 = wbcopy.getSheet(0);

			sheet2.setName("Report");

			wbmain.write();

			wbmain.close();

			new File(ofilepath).delete();

			return true;

		}

	}

	public static void reportDel() throws IOException {

		// String currentDir = System.getProperty("user.dir");

		String currentDir;

		if (ITestListenerImpl.defaultConfigProperty.get().getProperty("CICD").equalsIgnoreCase("N")) {

			currentDir = ITestListenerImpl.defaultConfigProperty.get().getProperty("latestReportLocation");

		} else {

			currentDir = ITestListenerImpl.defaultConfigProperty.get().getProperty("CICDlatestReportLocation");

		}

		File file = new File(currentDir);

		try {

			if (ITestListenerImpl.defaultConfigProperty.get().getProperty("reportAppend").equalsIgnoreCase("N")) {

				System.out.println("Existing reports will get deleted");

				if (file.exists()) {

					FileUtils.cleanDirectory(file);

				} else {

					FileUtils.forceMkdir(file);

				}

				// System.out.println("Existing reports will get deleted");

				// String filepath = currentDir + File.separator + "Results\\";

				// File file = new File(filepath);

				// String[] myFiles;

				// if (file.exists()) {

				// if (file.isDirectory()) {

				// myFiles = file.list();

				// for (int i = 0; i < myFiles.length; i++) {

				//

				// File myFile = new File(file, myFiles[i]);

				// /*

				// * System.out.println("Absolute File Path " + myFile.getAbsolutePath());

				// * System.out.println("File[" + i + "] : " + myFiles[i]);

				// */

				// // if (myFiles[i].contains(appName)

				// if (myFiles[i].contains(appName) || myFiles[i].contains(".html")) {

				// // && myFiles[i].contains(cal1)) {

				// Runtime.getRuntime().exec("cmd /c taskkill /f /im excel.exe");

				// System.out.println(" Deleting File[" + i + "] : " + myFiles[i]);

				// myFile.delete();

				// }

				// }

				// }

				// }

			} else {

				System.out.println("Existing report will get appended");

			}

		} catch (Exception e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

	}

	public static String excelMailReport() throws BiffException {

		String html;

		html = null;

		String UsrNme;

		String CertifyingAs;

		String color;

		String fontColor = "";

		int Total_Cases;

		try {

			Properties prop = new Properties();

			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir") + File.separator + "Properties" + File.separator + "OR.properties");

			prop.load(fis);

			// launchURL = prop.getProperty("url");

			launchURL = ITestListenerImpl.envURL;

			BufferedWriter out;

			// Date

			Calendar cal = Calendar.getInstance();

			DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy");

			String cal1 = dateFormat.format(cal.getTime());

			DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");

			String cal2 = dateFormat1.format(cal.getTime());

			String indexHtmlPath;

			if (ITestListenerImpl.defaultConfigProperty.get().getProperty("CICD").equalsIgnoreCase("N")) {

				indexHtmlPath = ITestListenerImpl.defaultConfigProperty.get().getProperty("latestReportLocation")
						+ File.separator + File.separator + "Reports" + File.separator + "Final Report.html";

			} else {

				indexHtmlPath = ITestListenerImpl.defaultConfigProperty.get().getProperty("CICDlatestReportLocation")

						+ File.separator + File.separator + "Reports" + File.separator + "Final Report.html";

			}

			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(indexHtmlPath), "UTF-8"));

			String currentDir;

			if (ITestListenerImpl.defaultConfigProperty.get().getProperty("CICD").equalsIgnoreCase("N")) {

				currentDir = ITestListenerImpl.defaultConfigProperty.get().getProperty("latestReportLocation")
						+ File.separator

						+ File.separator + "Reports";

			} else {

				currentDir = ITestListenerImpl.defaultConfigProperty.get().getProperty("CICDlatestReportLocation")

						+ File.separator + File.separator + "Reports";

			}

			String file_path = currentDir + File.separator + "Final Report.xls";

			File actualFile = new File(file_path);

			Workbook wb = Workbook.getWorkbook(new File(file_path));

			Sheet sh = wb.getSheet(0);

			int totalNoOfRows = sh.getRows();

			int totalNoOfCols = sh.getColumns();

			// Total_Cases = totalNoOfRows - 1;

			// System.out.println("Total Rows and cols " + totalNoOfRows + " " +

			// totalNoOfCols);

			// Failed, Passed and Critical_Error_Counter and Critical_Counter

			for (int i = 1; i < totalNoOfRows; i++) {

				for (int j = 0; j < totalNoOfCols; j++) {

					if (sh.getCell(j, i).getContents().equalsIgnoreCase("PASSED")) {
						String tcName = sh.getCell(1, i).getContents().toLowerCase().replaceAll(" ", "");
						if (!tcName.contains("Pre-requisite")) {
							Passed_Case_Counter = Passed_Case_Counter + 1;
						}
					} else if (sh.getCell(j, i).getContents().equalsIgnoreCase("FAILED")) {
						String tcName = sh.getCell(1, i).getContents().toLowerCase().replaceAll(" ", "");
						if (!tcName.contains("Pre-requisite")) {
							Failed_Case_Counter = Failed_Case_Counter + 1;
						}
					} else if (sh.getCell(j, i).getContents().equalsIgnoreCase("CRITICAL")) {

						Critical_Error_Counter = Critical_Error_Counter + 1;

					}

					if (j == 3) {

						if (sh.getCell(j, i).getContents().equalsIgnoreCase("FAIL")

								&& sh.getCell(j + 1, i).getContents().equalsIgnoreCase("CRITICAL")) {

							Critical_Counter = Critical_Counter + 1;

						}

					}

				}

			}

			// System.out.println("Failed, Passed Counter: " + Failed_Case_Counter + " " +

			// Passed_Case_Counter);

			// CertifyingAs

			if (Critical_Counter > 0) {

				CertifyingAs = "<font face='Calibri' size='12px' color='Red'><b>No-Go</b></font>";

			} else if (Failed_Case_Counter >= Passed_Case_Counter) {

				CertifyingAs = "<font face='Calibri' size='12px' color='Red'><b>No-Go</b></font>";

			} else {

				CertifyingAs = "<font face='Calibri' size='12px' color='Green'><b>Go</b></font>";

			}
			Total_Cases = Passed_Case_Counter + Failed_Case_Counter;

			// Body Content

			html = "<HTML><BODY> <font face='Calibri' size='3.5px'>Hello Team, " + "<br><br>" + "Please find the "

					+ appName + " Automated Test Report below. </font>";

			// if
			// (ITestListenerImpl.defaultConfigProperty.get().getProperty("CICD").equalsIgnoreCase("N"))
			// {
			//
			// html += "<br>Please find the Detailed Report under <b><a href="
			//
			// + ITestListenerImpl.defaultConfigProperty.get().getProperty("MailReportPath")
			// + ">"
			//
			// + File_Transfer.backUpReportName + "</a></b>" + ".</font><br><br>";
			//
			// // html += "<br><font face='Calibri' size='12px'><i>Detailed Automation
			// report
			//
			// // is available at <b><a
			//
			// //
			// href="+ITestListenerImpl.defaultConfigProperty.get().getProperty("DetailedReportPath")+">Detailed
			//
			// // Automation Report</a></b></i></font><br><br>";
			//
			// }

//		    if(launchURL.contains(".")) {
			html += "<br><br> " + "<b> AppName: </b>"
					+ ITestListenerImpl.defaultConfigProperty.get().getProperty("AppName") + "<br><b> Date: </b>" + cal2
					+ " </font><br><br>";
//		    }else {
//		    	html += "<br><br> <b> AppName: </b>" + ITestListenerImpl.defaultConfigProperty.get().getProperty("AppName") + "<br><b> Date: </b>" + cal2
//						+ " </font><br><br>";
//		    }
//			
			// Summary Table

			html += "<font face='Calibri' size='4.5'><b>SUMMARY:</b></font><br><TABLE border='1' style='border-collapse:collapse' cellpadding '5' cellspacing='0'>";

			html += "<tr height='30'><TH bgcolor='#1A5276' align='left'><font face='calibri' size='3' color='White'><b>Total Scenarios Executed</b></font></th>";

			html += "<TH bgcolor='#1A5276' align='left'><font face='calibri' size='3' color='White'><b>Total Scenarios Passed</b></font></th>";

			html += "<TH bgcolor='#1A5276' align='left'><font face='calibri' size='3' color='White'><b>Total Scenarios Failed</b></font></th>";

			html += "</tr>";

			html += "<tr align='center'><Td bgcolor='Whitesmoke'><font face='calibri' size='2.5'> "

					+ Total_Cases + " </font></td>";

			html += "<Td bgcolor='Whitesmoke'><font face='calibri' size='2.5'> " + Passed_Case_Counter

					+ " </font></td>";

			html += "<Td bgcolor='Whitesmoke'><font face='calibri' size='2.5'> " + Failed_Case_Counter

					+ " </font></td>";

			html += "</tr></Table><br><br>";

			// Split-Up Table

			html += "<font face='Calibri' size='4.5'><b>SPLIT-UP:</b></font><br><TABLE border='1' style='border-collapse:collapse' cellpadding'5' cellspacing='0'>";

			html += "<TR height='30'>";

			for (int i = 0; i < totalNoOfRows; i++) {

				for (int j = 0; j < totalNoOfCols; j++) {

					if (i == 0) {

						if (j == 4 || j == 5) {

							color = "#1A5276";

							html += "<TH width='55' bgcolor=" + color

									+ " align='center'><font face='calibri' size='3' color='White'><b>"

									+ sh.getCell(j, i).getContents().toString() + "</b></font></th>";

						} else if (j == 3) {

							color = "#1A5276";

							html += "<TH width='45' bgcolor=" + color

									+ " align='center'><font face='calibri' size='3' color='White'><b>"

									+ sh.getCell(j, i).getContents().toString() + "</b></font></th>";

						} else {

							color = "#1A5276";

							html += "<TH bgcolor=" + color

									+ " align='center'><font face='calibri' size='3' color='White'><b>"

									+ sh.getCell(j, i).getContents().toString() + "</b></font></th>";

						}

					} else {

						if (sh.getCell(j, i).getContents().equalsIgnoreCase("PASS")) {

							color = "#00ed00";

						} else if (sh.getCell(j, i).getContents().equalsIgnoreCase("PASSED")) {

							color = "#0d6b2e";

							fontColor = "#FFFFFF";

						} else if (sh.getCell(j, i).getContents().equalsIgnoreCase("FAIL")) {

							color = "#ed0000";

						} else if (sh.getCell(j, i).getContents().equalsIgnoreCase("FAILED")) {

							color = "#930000";

							fontColor = "#FFFFFF";

						} else {

							color = "Whitesmoke";

						}

						if (j == 2) {

							html += "<Td align='center' bgcolor=" + color + "><font face='calibri' size='4' color="

									+ fontColor + "><b> " + sh.getCell(j, i).getContents().toString()

									+ "</b> </font></td>";

						} else if (j == 0 || j == 3 || j == 4 || j == 5) {

							html += "<Td align='center' bgcolor=" + color + "><font face='calibri' size='2'> "

									+ sh.getCell(j, i).getContents().toString() + " </font></td>";

						} else {

							html += "<Td align='center' bgcolor=" + color + "><font face='calibri' size='2'> "

									+ sh.getCell(j, i).getContents().toString() + " </font></td>";

						}

					}

				}

				html += "</TR>";

			}

			// Tail

			html += "</TABLE> <br><font face='Calibri' size='3.5px'>";

			// html += "<br><br><b>Note:</b>";

			// html += "<i> Please find attached Detailed Automation Report.</i>";

			html += "<br> Thanks & Regards <br>";

			html += User_Name;

			html += "<br><font face='Calibri' size='3.5px'><i>Don't reply to all as this is a auto-generated report. Any further queries please contact <b><a href='mailto:"

					+ teamEmailID + "'>" + User_Name + "</a></b></i></font>";

			html += "<br></Font></BODY></HTML>";

			wb.close();

			out.write(html.toString());

			out.close();

			System.out.println("==========>Created HTML Report from Excel Report<==========");

		} catch (Exception e) {

			// e.printStackTrace();

			System.out.println("Some problem has occured while sending mail" + e.getMessage());

		}

		return html;

	}

} // reportgenerator