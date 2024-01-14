package basePackage.steps;

import java.util.Arrays;
import java.util.HashMap;

import org.openqa.selenium.TimeoutException;

import com.aventstack.extentreports.ExtentTest;

import basePackage.utilities.ExcelUtil;
import basePackage.utilities.ITestListenerImpl;
import basePackage.utilities.MyLogger;

public class ExecutionMethods extends CommonFunctions {
	public static ThreadLocal<String> scenarioName = new ThreadLocal<String>();

	public boolean userLoadsURL(String url, ExtentTest logInfo) throws Exception {
		boolean result = false;
		try {
			getDriver().get(prop.getProperty(url));
			MyLogger.info("Launched URL: " + prop.getProperty(url));
			logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "test environment launched");
			result = true;
		} catch (TimeoutException e) {
			e.printStackTrace();
			getDriver().navigate().refresh();
			getDriver().get(prop.getProperty(url));
			logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "test environment launched");
			MyLogger.info("Launched URL: " + prop.getProperty(url));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean user_clicks_toolsQA_mainMenu(String mainMenu, ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Boolean resultArr[] = new Boolean[100];
		int i = 0;
		try {
			resultArr[i++] = click("sel_mainMenu_alertsAndFramesAndWindows", logInfo);
			resultArr[i++] = click("sel_subMenu_alertsAndFramesAndWindows_browserWindowTab", logInfo);
			result = !Arrays.asList(resultArr).contains(false);
			return result;
		} catch (

		Exception e) {
			MyLogger.info("Exception occurred : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}

	public boolean user_clicks_subMenu_and_perform(ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Boolean resultArr[] = new Boolean[100];
		int i = 0;
		try {
			resultArr[i++] = click("sel_subMenu_alertsAndFramesAndWindows_browserWindowTab", logInfo);
			waitFor(500, logInfo);
			resultArr[i++] = click("sel_subMenu_alertsAndFramesAndWindows_newTabButton", logInfo);
			resultArr[i++] = switchToChildWindow(logInfo);
			resultArr[i++] = closeChildWindow(logInfo);
			result = !Arrays.asList(resultArr).contains(false);
			return result;
		} catch (Exception e) {
			MyLogger.info("Exception occurred : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}

// #==================Sauce Labs===========================#
	public boolean user_login_into_swagLab(ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Boolean resultArr[] = new Boolean[100];
		HashMap<String, String> credentials = new HashMap<>();
		ExcelUtil.openExcel();
		ExcelUtil.getRowData("TestCases", scenarioName.get());
		String accessCredentials = ExcelUtil.getColumnData("LogIn Credentials");
		credentials.clear();
		credentials = getCellValue(accessCredentials, logInfo);
		int i = 0;
		try {

			resultArr[i++] = click("Sel_swagLabs_userName", logInfo);
			resultArr[i++] = entersValue(ExcelUtil.getAttribute("UserName"), "Sel_swagLabs_userName", logInfo);
			resultArr[i++] = click("Sel_swagLabs_passWord", logInfo);
			resultArr[i++] = entersValue(ExcelUtil.getAttribute("Password"), "Sel_swagLabs_passWord", logInfo);
			resultArr[i++] = click("Sel_swagLabs_logInButton", logInfo);
			result = !Arrays.asList(resultArr).contains(false);
			return result;
		} catch (Exception e) {
			MyLogger.info("Exception occurred : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}

	public boolean user_do_sorting(ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Boolean resultArr[] = new Boolean[100];
		int i = 0;
		HashMap<String, String> productData = new HashMap<>();
		ExcelUtil.openExcel();
		ExcelUtil.getRowData("TestCases", scenarioName.get());
		String productDetails = ExcelUtil.getColumnData("Products");
		productData.clear();
		productData = getCellValue(productDetails, logInfo);
		try {
			if (productData.containsKey("Sortby")) {
				resultArr[i++] = click("Sel_swagLabs_homePage_sortByDD", logInfo);
				String sortOption = ExcelUtil.getAttribute("Sortby");
				String xPath2 = "//select[@data-test='product_sort_container']//child::option[contains(text(),'"
						+ sortOption + "')]";
				resultArr[i++] = clickDynamic(xPath2, "Sel_sortBy_Option", logInfo);
			} else {
				System.out.println("Sortby is not present");
			}
			result = !Arrays.asList(resultArr).contains(false);
			return result;
		} catch (Exception e) {
			MyLogger.info("Exception occurred : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}

	public boolean user_adds_product_inCart(ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Boolean resultArr[] = new Boolean[100];
		int i = 0;
		HashMap<String, String> productData = new HashMap<>();
		ExcelUtil.openExcel();
		ExcelUtil.getRowData("TestCases", scenarioName.get());
		String productDetails = ExcelUtil.getColumnData("Products");
		productData.clear();
		productData = getCellValue(productDetails, logInfo);

		try {
			if (productData.containsKey("Total Products")) {
				String productCount = ExcelUtil.getAttribute("Total Products");
				if (productCount != "0" || productCount != "N/A") {
					int count = Integer.parseInt(productCount);
					for (int x = 1; x <= count; x++) {
						String product = ExcelUtil.getAttribute("ProductName" + x);
						String xPath = "(//div[@class='inventory_item_name ' and contains(text(),'" + product
								+ "')]//following::button[contains(text(),'Add to cart')])[1]";
						resultArr[i++] = clickDynamic(xPath, "Sel_select_ProductToCart" + " for " + product, logInfo);
					}
				}
				resultArr[i++] = click("Sel_swagLabs_cartButton", logInfo);
			} else {
				System.out.println("Total Product is not present");
			}
			result = !Arrays.asList(resultArr).contains(false);
			return result;
		} catch (Exception e) {
			MyLogger.info("Exception occurred : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	public boolean user_process_the_cart(ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Boolean resultArr[] = new Boolean[100];
		int i = 0;

		try {
			resultArr[i++] = click("Sel_swagLabs_yourCart_checkOut_button", logInfo);
			result = !Arrays.asList(resultArr).contains(false);
			return result;
		} catch (Exception e) {
			MyLogger.info("Exception occurred : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	public boolean user_process_the_checkOut_information(ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Boolean resultArr[] = new Boolean[100];
		int i = 0;
		HashMap<String, String> checkOutData = new HashMap<>();
		ExcelUtil.openExcel();
		ExcelUtil.getRowData("TestCases", scenarioName.get());
		String checkOutDetails = ExcelUtil.getColumnData("CheckOut");
		checkOutData.clear();
		checkOutData = getCellValue(checkOutDetails, logInfo);
		try {
			if (checkOutData.containsKey("FirstName")) {
				resultArr[i++] = entersValue(ExcelUtil.getAttribute("FirstName"),
						"Sel_swagLabs_checkOut_info_firstName", logInfo);
			} else {
				System.out.println("FirstName is not present");
			}

			if (checkOutData.containsKey("LastName")) {
				resultArr[i++] = entersValue(ExcelUtil.getAttribute("LastName"), "Sel_swagLabs_checkOut_info_lastName",
						logInfo);
			} else {
				System.out.println("LastName is not present");
			}

			if (checkOutData.containsKey("PostCode")) {
				resultArr[i++] = entersValue(ExcelUtil.getAttribute("PostCode"),
						"Sel_swagLabs_checkOut_info_postalCode", logInfo);
			} else {
				System.out.println("PostCode is not present");
			}
			resultArr[i++] = click("Sel_swagLabs_checkOut_info_continueBtn", logInfo);
			result = !Arrays.asList(resultArr).contains(false);
			return result;
		} catch (Exception e) {
			MyLogger.info("Exception occurred : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	public boolean user_process_the_checkOut_overView(ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Boolean resultArr[] = new Boolean[100];
		int i = 0;
		HashMap<String, String> checkOutData = new HashMap<>();
		ExcelUtil.openExcel();
		ExcelUtil.getRowData("TestCases", scenarioName.get());
		String checkOutDetails = ExcelUtil.getColumnData("CheckOut");
		checkOutData.clear();
		checkOutData = getCellValue(checkOutDetails, logInfo);
		try {
			String totalValue = getText("Sel_swagLabs_checkOut_overView_totalValue", logInfo);
			System.out.println(totalValue);
			ExcelUtil.writeData("Total Amount", totalValue);
			resultArr[i++] = click("Sel_swagLabs_checkOut_overView_finish_button", logInfo);
			result = !Arrays.asList(resultArr).contains(false);
			return result;
		} catch (Exception e) {
			MyLogger.info("Exception occurred : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
}