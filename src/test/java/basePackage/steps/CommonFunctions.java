package basePackage.steps;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.codec.binary.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;

import org.apache.logging.log4j.core.appender.rolling.action.IfAccumulatedFileCount;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.jboss.aerogear.security.otp.Totp;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.v85.network.Network;
import org.openqa.selenium.devtools.v85.network.model.Headers;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.interactions.touch.ScrollAction;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.aventstack.extentreports.ExtentTest;
import com.github.dockerjava.api.model.Driver;
import com.google.common.util.concurrent.Uninterruptibles;
import com.opencsv.CSVReader;

import freemarker.template.utility.StringUtil;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.internal.operators.single.SingleCache;
import io.reactivex.rxjava3.internal.operators.single.SingleContains;
//import jdk.nashorn.internal.runtime.arrays.IntElements;
import basePackage.utilities.EncryptionAndDecryption;
import basePackage.utilities.ITestListenerImpl;
import basePackage.utilities.MyLogger;
import basePackage.utilities.ReportGenerator;

public class CommonFunctions extends ITestListenerImpl {

	private static final boolean Value = false;
	// protected static WebDriver browser;
	protected Properties prop = new Properties();
	protected FileInputStream fis;
	protected static ThreadLocal<ReportGenerator> rg = new ThreadLocal<ReportGenerator>();

	// Declare ThreadLocal Driver (ThreadLocalMap) for ThreadSafe Tests
	protected static ThreadLocal<RemoteWebDriver> remoteDriver = new ThreadLocal<RemoteWebDriver>();
	protected static ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
	public static ThreadLocal<String> browserName = new ThreadLocal<String>();
	public static ThreadLocal<Properties> defaultConfigProperty = new ThreadLocal<Properties>();

	// window switching variables
	public String childWinAdd;
	public String parWinAdd;
	int counter = 0;
	int envLoadCounter = 0;
	public static boolean isChromeMfa = false;
	public static boolean isEdgeMfa = false;
	public static boolean isChromeEnv = false;
	public static boolean isEdgeEnv = false;
	public static boolean isIllusContinue = false;
	public static boolean advSetVal = false;
	public static boolean corpTrustTRS = false;
	public static boolean corpTrustIDV = false;
	String parentWindow = null, childWindow = null;
	static int i;
	public String eAppID;
	HashMap<String, String> mapData = new HashMap<>();

	@BeforeClass
	@Parameters({ "browser", "positionx", "positiony", "port" })
	public void setupBrowser(String browser, @Optional("0") Integer x, @Optional("0") Integer y,
			@Optional("127.0.0.1:9222") String port) throws Exception {
		try {
			DesiredCapabilities capabilities = new DesiredCapabilities();
			browserName.set(browser);
			capabilities.setCapability("browserName", browser);
			System.out.println("====Browser is " + browser.toUpperCase() + " ====");
			fis = new FileInputStream(System.getProperty("user.dir") + "\\Properties\\DefaultConfig.properties");
			prop.load(fis);
			String driverPath = null;

			if (browser.equalsIgnoreCase("chrome")) {
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
				options.addArguments("--no-sandbox");
				options.addArguments("start-maximized");
				options.addArguments("enable-automation");
				options.addArguments("--disable-browser-side-navigation");
				options.addArguments("--disable-gpu");
				options.addArguments("--disable-features=VizDisplayCompositor");
				options.addArguments("disable-features=DownloadBubble,DownloadBubbleV2");
				options.setExperimentalOption("useAutomationExtension", false);
				options.addExtensions(new File("./Extensions/AdBlocker.crx"));

				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				chromePrefs.put("profile.default_content_setting_values.automatic_downloads", 1);
				Calendar cal = Calendar.getInstance();
				DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy");
				String cal1 = dateFormat.format(cal.getTime());
				chromePrefs.put("safebrowsing.enabled", true);
				chromePrefs.put("download.prompt_for_download", false);
				chromePrefs.put("directory_upgrade", true);

				// To disable Save As Popup in incognito
				List<String> enabledLabsExperiments = new ArrayList<>();
				enabledLabsExperiments.add("download-bubble@2");
				enabledLabsExperiments.add("download-bubble-v2@2");
				chromePrefs.put("browser.enabled_labs_experiments", enabledLabsExperiments);
				options.setExperimentalOption("localState", chromePrefs);
				options.addArguments("safebrowsing-disable-extension-blacklist");
				options.addArguments("--remote-allow-origins=*");
				options.setExperimentalOption("prefs", chromePrefs);
				options.addArguments("--safebrowsing-disable-download-protection");
				options.addArguments("safebrowsing-disable-extension-blacklist");

				if (prop.getProperty("CICD").equalsIgnoreCase("Y")) {
					String downloadPath = System.getProperty("user.dir") + "\\NextGenBDD_Results" + "\\Reports_" + cal1
							+ "\\Downloads";
					System.out.println("CICD Path: " + downloadPath);
					chromePrefs.put("download.default_directory", downloadPath);
				} else {
					String downloadPath = getDefaultProp().getProperty("latestReportLocation");
					System.out.println("DownloadPath: " + downloadPath);
					chromePrefs.put("download.default_directory", downloadPath + "\\Reports_" + cal1 + "\\Downloads");
				}

				if (prop.getProperty("Headless").equalsIgnoreCase("Y")) {
					options.addArguments("--window-size=1600,1000");
					options.addArguments("--headless");
				}
				if (prop.getProperty("Grid").equalsIgnoreCase("Y")) {
					capabilities.merge(options);
					remoteDriver.set(new RemoteWebDriver(new URL(prop.getProperty("GridUrl")), capabilities));
				} else {
					if (prop.getProperty("CICD").equalsIgnoreCase("Y")) {
						WebDriverManager.chromedriver().setup();
					} else if (prop.getProperty("BrowserDirectPath").equalsIgnoreCase("Y")) {
						driverPath = "C:\\Program Files\\Selenium ChromeDriver\\120.0.6099.62\\chromedriver.exe";
						System.setProperty("webdriver.chrome.driver", driverPath);
					} else if (prop.getProperty("SeleniumManager").equalsIgnoreCase("Y")) {
						System.out.println("Browser Launch using Selenium Manager");
					} else if (prop.getProperty("WebDriverManager").equalsIgnoreCase("Y")) {
						WebDriverManager.chromedriver().setup();
					} else {
						System.out.println("Check the Browser Launch Options");
					}

					if (prop.getProperty("ChromeProfile").equalsIgnoreCase("Y")) {
						options.addArguments("chrome.switches", "--disable-extensions");
						options.addArguments("user-data-dir=C:\\Users\\" + System.getProperty("user.name")
								+ "\\AppData\\Local\\Google\\Chrome\\User Data" + i++);
						System.out.println("===============Setting ChromeProfile " + i + "===============");
					}
					if (prop.getProperty("isChromeExisting").equalsIgnoreCase("Y")) {
						options.setExperimentalOption("debuggerAddress", port);
						System.out.println("Started existing Chrome");
					}
					if (prop.getProperty("ChromeIncognito").equalsIgnoreCase("Y")) {
						options.addArguments("--incognito");
						options.addArguments("start-maximized");
						// options.addArguments("--window-size=1400,800");
					}
					if (prop.getProperty("MobileEmulation").equalsIgnoreCase("Y")) {
						Map<String, String> mobileEmulation = new HashMap<>();
						mobileEmulation.put("deviceName", prop.getProperty("DeviceName"));
						options.setExperimentalOption("mobileEmulation", mobileEmulation);
					}

					webDriver.set(new ChromeDriver(options));
				}

			} else if (browser.equalsIgnoreCase("IE")) {
				InternetExplorerOptions options = new InternetExplorerOptions();
				capabilities.setCapability("requireWindowFocus", true);
				// capabilities.setJavascriptEnabled(true);
				options.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				options.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
				// options.attachToEdgeChrome();
				// options.withEdgeExecutablePath("C:\\Program Files
				// (x86)\\Microsoft\\Edge\\Application\\msedge.exe");
				capabilities.merge(options);
				if (prop.getProperty("Grid").equalsIgnoreCase("Y")) {
					remoteDriver.set(new RemoteWebDriver(new URL(prop.getProperty("GridUrl")), capabilities));
				} else {
					System.setProperty("webdriver.ie.driver",
							"C:\\Program Files (x86)\\Selenium IE Driver Server 32 Bit\\IEDriverServer.exe");
					webDriver.set(new InternetExplorerDriver(options));
					// }
					getDriver().manage().window().setPosition(new Point(x, y));
				}

			} else if (browser.equalsIgnoreCase("Edge")) {
				Runtime.getRuntime().exec("taskkill /f /im msedge.exe");
				EdgeOptions options = new EdgeOptions();
				options.setBinary("C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe");

				if (prop.getProperty("EdgeIncognito").equalsIgnoreCase("Y")) {
					options.addArguments("-inprivate");
				} else {
					options.addArguments(
							"user-data-dir=C:\\Users\\" + System.getProperty("user.name")
									+ "\\AppData\\Local\\Microsoft\\Edge\\User Data\\",
							"profile-directory=Default", "--disable-extensions");
				}
				if (prop.getProperty("EdgeProfile").equalsIgnoreCase("Y")) {
					options.addArguments(
							"user-data-dir=C:\\Users\\" + System.getProperty("user.name")
									+ "\\AppData\\Local\\Microsoft\\Edge\\User Data\\" + i++,
							"profile-directory=Default", "--disable-extensions");
					System.out.println("===============Setting EdgeProfile " + i + "===============");
				}
				// options.addArguments("--remote-debugging-port=9222");
				options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
				options.addArguments("--no-sandbox"); // Bypass OS security model
//				HashMap<String, Object> edgePrefs = new HashMap<String, Object>();
//				edgePrefs.put("download.default_directory",
//						"C:\\Users\\" + System.getProperty("user.name") + "\\Downloads\\EdgeDownloads");
//				options.setExperimentalOption("prefs", edgePrefs);
				if (prop.getProperty("Headless").equalsIgnoreCase("Y")) {
					options.addArguments("--window-size=1400,600");
					options.addArguments("--headless");
				}
				capabilities.merge(options);
				if (prop.getProperty("Grid").equalsIgnoreCase("Y")) {
					remoteDriver.set(new RemoteWebDriver(new URL(prop.getProperty("GridUrl")), capabilities));
				} else {
					WebDriverManager.edgedriver().setup();
					webDriver.set(new EdgeDriver(options));
					// }
					getDriver().manage().window().setPosition(new Point(x, y));
				}
			} else {
				System.out.println("----------------Invalid Browser-------------");
			}
			getDriver().manage().window().maximize();
			// getDriver().manage().window().setSize(new Dimension(1500, 1000));
			// getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		} catch (Exception e) {
			System.out.println("In start Browser Exception");
			e.printStackTrace();
			throw e;
		}

	}

	public String getBrowserName() {
		return browserName.get();
	}

	// @BeforeMethod
	public WebDriver getDriver() {
		try {
			// Get driver from ThreadLocalMap
			Properties properties = new Properties();
			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir") + "\\Properties\\DefaultConfig.properties");
			properties.load(fis);
			if (properties.getProperty("Grid").equalsIgnoreCase("Y")) {
				return remoteDriver.get();
			} else {
				return webDriver.get();
			}
		} // return driver;
		catch (Exception e) {
			System.out.println("In GetDriver catch");
			e.printStackTrace();
			return null;
		}

	}

	@AfterClass
	public void endBrowser() throws Exception {
		if (prop.getProperty("DontCloseBrowser").equalsIgnoreCase("N")) {
			getDriver().quit();
		}

	}

	public Properties getORProp() {

		try {
			fis = new FileInputStream(System.getProperty("user.dir") + "\\Properties\\OR.properties");
			prop.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return prop;
	}

	public Properties getDefaultProp() {

		try {
			fis = new FileInputStream(System.getProperty("user.dir") + "\\Properties\\DefaultConfig.properties");
			prop.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return prop;
	}

	public boolean dummymethodskeleton(ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Boolean resultArr[] = new Boolean[100];
		try {

			result = !Arrays.asList(resultArr).contains(false);
			return result;
		} catch (

		Exception e) {
			MyLogger.info("Exception occurred : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}

	public HashMap getCellValue(String cellData, ExtentTest logInfo) throws Throwable {
		try {
			String key = null;
			String value = null;
			String splitt;
			if (cellData.contains("\n")) {
				mapData.clear();
				String[] strArray = cellData.split("\n");
				for (int i = 0; i < strArray.length; i++) {
					splitt = strArray[i];
					key = StringUtils.substringBefore(splitt, ":-");
					value = StringUtils.substringAfter(splitt, ":-");
					mapData.put(key, value);
				}
			} else {
				mapData.clear();
				key = StringUtils.substringBefore(cellData, ":-");
				value = StringUtils.substringAfter(cellData, ":-");
				mapData.put(key, value);
			}
			return mapData;
		} catch (Exception e) {
			MyLogger.info("Exception occurred : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	public void waitForPageToLoad() {
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(30));
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver wdriver) {
				return ((JavascriptExecutor) getDriver()).executeScript("return document.readyState")
						.equals("complete");
			}
		});
	}

	// temporary method wait until to be used instead of thread sleep
	public void waitFor(int time, ExtentTest logInfo) throws Exception {
		boolean eleFind = false;
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(time));
			wait.wait(time);
			MyLogger.info("Waited until given time using waitFor");
			logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "Waited until given time");
		} catch (Exception e) {
			MyLogger.info("Loading, waited and ... not appeared");
		}
	}

	public String getLocator(String ele) throws Throwable {
		String xpath = null;
		try {
			if (ele.startsWith("sel_dynamic#$")) {
				System.out.println("The ele before conversion is: " + ele);
				xpath = StringUtils.substringAfter(ele, "sel_dynamic#$");
				System.out.println("The ele before conversion is: " + xpath);
			} else if (ele.startsWith("dynamic#$")) {
				System.out.println("The ele before conversion is: " + ele);
				xpath = StringUtils.substringAfter(ele, "dynamic#$");
				System.out.println("The ele before conversion is: " + xpath);
			} else {
				xpath = prop.getProperty(ele);
			}
			return xpath;
		} catch (Exception e) {
			MyLogger.info("Exception occurred : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}

	// method to check whether Checkbox is clicked or to click
	public boolean click_checkbox(String xpath_label, String xpath_input, ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Exception e1;
		try {
			if (isElementPresent(xpath_label, logInfo)) {
				WebElement element = getDriver().findElement(By.xpath(prop.getProperty(xpath_input)));
				if (element.isSelected()) {
					// System.out.println("Check box is already selected");
					click(xpath_label, logInfo);
					click(xpath_label, logInfo);
					result = true;
				} else {
					click(xpath_label, logInfo);
					result = true;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e1 = e;
			e.printStackTrace();
			throw e1;
		}
		return result;
	}

	// method to verify Checkbox is unchecked
	public boolean checkboxIsUnchecked(String xpath_input, ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Exception e1;
		try {
			if (isElementPresent(xpath_input, logInfo)) {
				WebElement element = getDriver().findElement(By.xpath(prop.getProperty(xpath_input)));
				if (element.isSelected()) {
					result = false;
				} else {
					result = true;
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), xpath_input + " is unchecked");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e1 = e;
			e.printStackTrace();
			throw e1;
		}
		return result;
	}

	// method to fetch the value from given attribute
	public String getText(String ele, ExtentTest logInfo) throws Throwable {
		String attribValue = null;
		Exception e1;
		try {
			if (isElementPresent(ele, logInfo)) {
				if (ele.toLowerCase().startsWith("sel")) {
					attribValue = getDriver().findElement(By.xpath(prop.getProperty(ele))).getText();
					MyLogger.info("Value fetched on " + ele + " using selenium. " + "And the value is " + attribValue);
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), " Value fetched from " + ele);
				} else {
				}
			}
		} catch (Exception e) {
			e1 = e;
			e.printStackTrace();
			throw e1;
		}
		return attribValue;

	}

	public String getValue(String ele, ExtentTest logInfo) throws Throwable {
		String attribValue = null;
		Exception e1;
		try {
			if (isElementPresent(ele, logInfo)) {
				if (ele.toLowerCase().startsWith("sel")) {
					attribValue = getDriver().findElement(By.xpath(prop.getProperty(ele))).getAttribute("Value");
					MyLogger.info("Value fetched on " + ele + " using selenium. " + "And the value is " + attribValue);
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), " Value fetched from " + ele);
				} else {
				}
			}
		} catch (Exception e) {
			e1 = e;
			e.printStackTrace();
			throw e1;
		}
		return attribValue;

	}

	// method to click elements
	public boolean click(String ele, ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Exception e1 = null;
		try {
			if (prop.getProperty(ele).startsWith("Ext")) {
				if (compwait(ele, logInfo)) {
					JavascriptExecutor jse = (JavascriptExecutor) getDriver();
					jse.executeScript(prop.getProperty(ele) + ".click()");
					MyLogger.info("Clicked on component query" + ele);
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "Clicked on " + ele);
					result = true;
					// isLoading();
					// loading();
				}
			} else {
				if (isElementPresent(ele, logInfo)) {
					if (ele.toLowerCase().startsWith("sel")) {
						getDriver().findElement(By.xpath(prop.getProperty(ele))).click();
						MyLogger.info("Clicked on " + ele + " using selenium");
						logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "Clicked on " + ele);
						result = true;
						// isLoading();
						// loading();
					} else {
						JavascriptExecutor jse = (JavascriptExecutor) getDriver();
						jse.executeScript("arguments[0].click();",
								getDriver().findElement(By.xpath(prop.getProperty(ele))));
						MyLogger.info("Clicked on " + ele + "using java script executor");
						logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "Clicked on " + ele);
						result = true;
						// isLoading();
						// loading();
					}
				}
			}
			return result;
		} catch (StaleElementReferenceException e) {
			MyLogger.info("Retry click on StaleElement Exception" + e.getMessage());
			// e.printStackTrace();
			try {
				result = staleClick(ele, logInfo);
			} catch (Exception excep) {
				e1 = excep;
				MyLogger.info("Exception occurred : " + excep);
				excep.printStackTrace();
				throw e1;
			}
			MyLogger.info("StaleElement Exception Handled" + e.getMessage());
		} catch (Exception e) {
			e1 = e;
			MyLogger.info("Exception occurred : " + e.getMessage());
			e.printStackTrace();
			logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), ele + " is not clicked");
			throw e1;
		}
		return result;
	}

	public boolean clickDynamic(String xPath, String ele, ExtentTest logInfo) throws Throwable {
		boolean result = false;
		try {
			if (isXpathPresent(xPath, logInfo)) {
				if (ele.toLowerCase().startsWith("sel")) {
					getDriver().findElement(By.xpath(xPath)).click();
					MyLogger.info("Clicked dynamically on " + ele + " using Selenium");
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "Clicked dynamically on " + ele);
					result = true;
				} else {
					JavascriptExecutor jse = (JavascriptExecutor) getDriver();
					jse.executeScript("arguments[0].click();", getDriver().findElement(By.xpath(xPath)));
					MyLogger.info("Clicked dynamically on " + ele + "using java script executor");
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "Clicked dynamically on " + ele);
					result = true;
				}
			}
		} catch (StaleElementReferenceException e) {
			try {
				if (isXpathPresent(xPath, logInfo)) {
					if (ele.toLowerCase().startsWith("sel")) {
						getDriver().findElement(By.xpath(xPath)).click();
						MyLogger.info("Clicked dynamically on " + ele + " using Selenium");
						logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()),
								"Clicked dynamically on " + ele);
						result = true;
					} else {
						JavascriptExecutor jse = (JavascriptExecutor) getDriver();
						jse.executeScript("arguments[0].click();", getDriver().findElement(By.xpath(xPath)));
						MyLogger.info("Clicked dynamically on " + ele + "using java script executor");
						logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()),
								"Clicked dynamically on " + ele);
						result = true;
					}
				}
			} catch (Exception e1) {
				MyLogger.info("Exception occured: " + e.getMessage());
				e1.printStackTrace();
				throw e1;
			}
		} catch (Exception e) {
			MyLogger.info("Exception occured: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	// stale element click
	public boolean staleClick(String ele, ExtentTest logInfo) throws Throwable {
		boolean result = false;
		if (prop.getProperty(ele).startsWith("Ext")) {
			if (compwait(ele, logInfo)) {
				JavascriptExecutor jse = (JavascriptExecutor) getDriver();
				jse.executeScript(prop.getProperty(ele) + ".click()");
				MyLogger.info("Stale element clicked " + ele);
				logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "Clicked on " + ele);
				result = true;
				// isLoading();
				// loading();
			}
		} else {
			if (isElementPresent(ele, logInfo)) {
				if (ele.toLowerCase().startsWith("sel")) {
					getDriver().findElement(By.xpath(prop.getProperty(ele))).click();
					MyLogger.info("Clicked on " + ele + " using selenium");
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "Clicked on " + ele);
					result = true;
				} else {
					JavascriptExecutor jse = (JavascriptExecutor) getDriver();
					jse.executeScript("arguments[0].click();",
							getDriver().findElement(By.xpath(prop.getProperty(ele))));
					MyLogger.info("Timeout element clicked " + ele + "using java script executor");
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "Clicked on " + ele);
					result = true;
					// isLoading();
					// loading();
				}
			}
		}
		return result;
	}

	// method to pass input to fields
	public boolean entersValue(String text, String ele, ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Exception e1;
		try {
			if (prop.getProperty(ele).startsWith("Ext")) {
				if (compwait(ele, logInfo)) {
					JavascriptExecutor jse = (JavascriptExecutor) getDriver();
					jse.executeScript(prop.getProperty(ele) + ".setValue('')");
					jse.executeScript(prop.getProperty(ele) + ".setValue('" + text + "')");
					MyLogger.info("Entered value  " + text + " in " + ele);
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()),
							"entered value " + text + " in " + ele);
					result = true;
				}
			} else {
				if (isElementPresent(ele, logInfo)) {
					if (ele.toLowerCase().startsWith("sel")) {
						getDriver().findElement(By.xpath(prop.getProperty(ele))).clear();
						getDriver().findElement(By.xpath(prop.getProperty(ele))).sendKeys(text);
						MyLogger.info("Entered value " + text + " in " + ele + " using send keys");
						logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()),
								"entered value " + text + " in " + ele);
					} else {
						JavascriptExecutor jse = (JavascriptExecutor) getDriver();
						jse.executeScript("arguments[0].value='" + text + "';",
								getDriver().findElement(By.xpath(prop.getProperty(ele))));
						MyLogger.info("Entered value " + text + " in " + ele + " using java script executor");
						logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()),
								"entered value " + text + " in " + ele);
					}
					result = true;
				}
			}
		} catch (StaleElementReferenceException staleexcep) {
			staleexcep.printStackTrace();
			MyLogger.info("stele element exception handled");
			if (prop.getProperty(ele).startsWith("Ext")) {
				if (compwait(ele, logInfo)) {
					JavascriptExecutor jse = (JavascriptExecutor) getDriver();
					jse.executeScript(prop.getProperty(ele) + ".setValue('')");
					jse.executeScript(prop.getProperty(ele) + ".setValue('" + text + "')");
					MyLogger.info("Entered value " + text + " in " + ele);
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()),
							"entered value " + text + " in " + ele);
					result = true;
				}
			} else {
				if (isElementPresent(ele, logInfo)) {
					if (ele.toLowerCase().startsWith("sel")) {
						getDriver().findElement(By.xpath(prop.getProperty(ele))).clear();
						getDriver().findElement(By.xpath(prop.getProperty(ele))).sendKeys(text);
						MyLogger.info("Entered value " + text + " in " + ele + " using send keys");
						logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()),
								"entered value " + text + " in " + ele);
					} else {
						JavascriptExecutor jse = (JavascriptExecutor) getDriver();
						jse.executeScript("arguments[0].value='" + text + "';",
								getDriver().findElement(By.xpath(prop.getProperty(ele))));
						MyLogger.info("Entered value " + text + " in " + ele + " using java script executor");
						logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()),
								"entered value " + text + " in " + ele);
					}
					result = true;
				}
			}
			staleexcep.printStackTrace();
		} catch (Exception e) {
			e1 = e;
			e.printStackTrace();
			logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), ele + " is not entered");
			throw e1;
		}

		return result;
	}

	// Method to enters value in the dynamic xpath
	public boolean entersValueDynamic(String text, String xPath, String ele, ExtentTest logInfo) throws Throwable {
		boolean result = false;
		try {
			if (isXpathPresent(xPath, logInfo)) {
				if (ele.toLowerCase().startsWith("sel")) {
					getDriver().findElement(By.xpath(xPath)).clear();
					getDriver().findElement(By.xpath(xPath)).click();
					getDriver().findElement(By.xpath(xPath)).sendKeys(text);
					MyLogger.info("Entered dynamic value " + text + " in " + ele + " using Selenium");
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()),
							"entered dynamic value " + text + " in " + ele);
					result = true;
				} else {
					JavascriptExecutor jse = (JavascriptExecutor) getDriver();
					jse.executeScript("arguments[0].value='" + text + "';", getDriver().findElement(By.xpath(xPath)));
					MyLogger.info("Entered dynamic value " + text + " in " + ele + " using java script executor");
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()),
							"entered dynamic value " + text + " in " + ele);
					result = true;
				}
			}
		} catch (StaleElementReferenceException e) {
			try {
				if (isXpathPresent(xPath, logInfo)) {
					if (ele.toLowerCase().startsWith("sel")) {
						getDriver().findElement(By.xpath(xPath)).clear();
						getDriver().findElement(By.xpath(xPath)).click();
						getDriver().findElement(By.xpath(xPath)).sendKeys(text);
						MyLogger.info("Entered dynamic value " + text + " in " + ele + " using Selenium");
						logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()),
								"entered dynamic value " + text + " in " + ele);
						result = true;
					} else {
						JavascriptExecutor jse = (JavascriptExecutor) getDriver();
						jse.executeScript("arguments[0].value='" + text + "';",
								getDriver().findElement(By.xpath(xPath)));
						MyLogger.info("Entered dynamic value " + text + " in " + ele + " using java script executor");
						logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()),
								"entered dynamic value " + text + " in " + ele);
						result = true;
					}
				}
			} catch (Exception e1) {
				MyLogger.info("Exception occured: " + e.getMessage());
				e1.printStackTrace();
				throw e1;
			}
		} catch (Exception e) {
			MyLogger.info("Exception occured: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	public boolean dateCalculator(String date, String xPath, ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Boolean resultArr[] = new Boolean[100];
		try {

			// Date Conversion
			String Days = StringUtils.substringBetween(date, "Date +", " Days");
			System.out.println(Days);
			int noOfDays = Integer.parseInt(Days);
			Calendar cal = Calendar.getInstance();
			LocalDate systemDate = LocalDate.now();
			cal.setTime(new Date());
			int days = 0;
			while (days < noOfDays) {
				systemDate = systemDate.plusDays(1);
				if (!(systemDate.getDayOfWeek() == DayOfWeek.SATURDAY
						|| systemDate.getDayOfWeek() == DayOfWeek.SUNDAY)) {
					++days;
				}
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
			String newDate = formatter.format(systemDate);

			// Date change validation - Month End
			if (newDate.startsWith("29")) {
				// cal.add(Calendar.DATE, 3);
				systemDate = systemDate.plusDays(3);
			} else if (newDate.startsWith("30")) {
				// cal.add(Calendar.DATE, 2);
				systemDate = systemDate.plusDays(2);
			} else if (newDate.startsWith("31")) {
				// cal.add(Calendar.DATE, 1);
				systemDate = systemDate.plusDays(1);
			} else {
				System.out.println("The converted date was not fell on month end.");
			}
			newDate = formatter.format(systemDate);

			// Adding the date inside the field
			Thread.sleep(1000);
			getDriver().findElement(By.xpath(xPath)).click();
			getDriver().findElement(By.xpath(xPath)).click();
			getDriver().findElement(By.xpath(xPath)).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
			getDriver().findElement(By.xpath(xPath)).sendKeys(newDate);
			result = true;
			Thread.sleep(2000);
			result = !Arrays.asList(resultArr).contains(false);
			return result;
		} catch (

		Exception e) {
			MyLogger.info("Exception occurred : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}

	public boolean performAction(String operation, String ele, ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Exception e1;
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(15));
			WebElement element = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty(ele))));
			if (element.isDisplayed()) {
				Actions act = new Actions(getDriver());
				if (operation.equalsIgnoreCase("click") || operation.equalsIgnoreCase("clicks")) {
					// act.click(element).build().perform();
					act.moveToElement(element).click().perform();
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), ele + " is clicked");
					// logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), ele);
					result = true;
				} else if (operation.equalsIgnoreCase("hover")) {
					act.moveToElement(element).build().perform();
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), ele + " is hovered");
					// logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), ele);
					result = true;
				} else if (operation.equalsIgnoreCase("scroll")) {
					act.moveToElement(element).build().perform();
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), ele + " is scrolled");
					// logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), ele);
					result = true;
				} else if (operation.startsWith("sendkeys")) {
					String text = operation.substring(9);
					act.sendKeys(element, text).build().perform();
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()),
							"entered text " + text + " into" + ele);
					// logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), ele);
					result = true;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e1 = e;
			e.printStackTrace();
			logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "action is not performed on " + ele);
			throw e1;
		}
		return result;
	}

	public void loadingSpinner(ExtentTest logInfo) throws Exception {
		// Loading spin with entire screen blur
		boolean eleFind = false;
		String pls_wait_xpath = "//p[@id='loadingspinner']";
		try {
			WebElement ele = getDriver().findElement(By.xpath(pls_wait_xpath));
			if (ele.isDisplayed()) {
				eleFind = true;
				logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "pls_wait_xpath is displayed");
			}
			if (eleFind) {
				MyLogger.info("Loading.............");
				WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(4));
				// wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(pls_wait_xpath)));
				wait.until(ExpectedConditions.invisibilityOf(ele));
				MyLogger.info("Waited until Please Wait disappears");
				logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "pls_wait_xpath is disappeared");
			}
		} catch (Exception e) {
			MyLogger.info("Loading is Fast----->loading Spinner is not appeared");
		}
	}

	// method to check element is present on page UI by XPATH
	public boolean isElementPresent(String ele, ExtentTest logInfo) throws Throwable {
		String xpath = prop.getProperty(ele);
		Exception e1 = null;
		boolean found = false;
		final long startTime = System.currentTimeMillis();
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(12));
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			if (!(element == null)) {
				// wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(element)));
				// wait.until(ExpectedConditions.stalenessOf(element));
				found = true;
			} else {
				found = false;
			}
		} catch (Exception e) {

			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			MyLogger.info("Element not found after waiting for:" + totalTime + " ms : " + xpath);
			e.printStackTrace();
			e1 = e;
		}
		if (found) {
			scrollElement(xpath);
			if (getDefaultProp().getProperty("AppName").equals("MyPractice")) {
				highLightElement(xpath);
			}
			// logInfo.addScreenCaptureFromBase64String((captureScreenShotBase64(getDriver())),
			// ele);
			logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), ele + " is found");
			// logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), ele);
		} else {
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			MyLogger.info("Exception : Element not found after waiting for:" + totalTime + " ms : " + xpath);
			logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), ele + " is not found");
			throw e1;
		}
		return found;
	}

	// method to scroll for the element
	public void scrollElement(String element) throws Exception {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		if (element.startsWith("Ext")) {
			if (element.endsWith("]") && !element.contains("get")) {
				js.executeScript(element + ".el.dom.scrollIntoView({block:'center',inline:'nearest'})");
			} else if (element.contains("getSelectionModel()")) {
				String[] q1 = element.split("getSelectionModel()");
				js.executeScript(q1[0] + "el.dom.scrollIntoView({block:'center',inline:'nearest'})");
			} else if (element.contains("setValue")) {
				String[] q1 = element.split("setValue");
				js.executeScript(q1[0] + "el.dom.scrollIntoView({block:'center',inline:'nearest'})");
			} else {
				// js.executeScript(element + ".scrollIntoView()");
				js.executeScript(element + ".scrollIntoView({block:'center',inline:'nearest'})");
			}
		} else {
			// js.executeScript("arguments[0].scrollIntoView(true);",
			// getDriver().findElement(By.xpath(element)));
			js.executeScript("arguments[0].scrollIntoView({block:'center',inline:'nearest'});",
					getDriver().findElement(By.xpath(element)));
		}
	}

	// method to refresh the window
	public boolean refreshwindow(ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Exception e1;
		try {
			getDriver().navigate().refresh();
			logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "window refreshed");
			result = true;
		} catch (Exception e) {
			e1 = e;
			e.printStackTrace();
			MyLogger.info("Exception occurred : " + e);
			throw e1;
		}
		return result;
	}

	public boolean switchToMainWindow(ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Set<String> allWind = getDriver().getWindowHandles();
		for (String eachWind : allWind) {
			if (eachWind.equals(parWinAdd)) {
				getDriver().switchTo().window(eachWind);
				logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "switched to parent window");
				MyLogger.info(result);
				result = true;
			}
		}
		return result;
	}

	public boolean switchToChildWindow(ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Set<String> allWind = getDriver().getWindowHandles();
		for (String eachWind : allWind) {
			if (!eachWind.equals(childWinAdd) && !eachWind.equals(parWinAdd)) {
				getDriver().switchTo().window(eachWind);
				logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "switched window");
				result = true;
			} else if (childWinAdd.equals(eachWind)) {
				getDriver().switchTo().window(childWinAdd);
				logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "switched to child window");
				result = true;
			}
		}
		return result;
	}

	public boolean closeChildWindow(ExtentTest logInfo) throws Throwable {
		boolean result = true;
		getDriver().close();
		// logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "closed
		// child window");
		return result;
	}

	public boolean switchWindowToParent(ExtentTest logInfo) throws Throwable {
		boolean result = false;
		Exception e1;
		try {

			Set<String> windAdd = getDriver().getWindowHandles();
			for (String each : windAdd) {
				if (parWinAdd.equals(each)) {
					getDriver().switchTo().window(parWinAdd);
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "switched to parent window");
					result = true;
				}
			}
		} catch (Exception e) {
			e1 = e;
			MyLogger.info("Exception occurred : " + e.getMessage());
			e.printStackTrace();
			throw e1;
		}
		return result;
	}

	// method to close window when alert does not appear
	public boolean closechildwindow(ExtentTest logInfo) throws Throwable {
		boolean result = true;
		Exception e1;
		try {
			System.out.println("*****************closechildwindow******************");
			Set<String> a = getDriver().getWindowHandles();
			System.out.println("Total windows: " + a);
			int noofwindows = a.size();
			MyLogger.info("total no of windows: " + noofwindows);
			MyLogger.info("Child Window: " + childWinAdd);
			MyLogger.info("Parent Window: " + parWinAdd);
			for (String b : a) {
				if (b.equals(childWinAdd)) {
					getDriver().switchTo().window(childWinAdd);
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "switched to child window");
					getDriver().close();
					MyLogger.info("childwindow closed " + childWinAdd);
					MyLogger.info("counter value is " + counter);
					getDriver().switchTo().window(parWinAdd);
					logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), "switched to parent window");
					System.out.println("Parent window title: " + getDriver().getTitle());
					result = true;
				}
			}
		} catch (Exception e) {
			e1 = e;
			e.printStackTrace();
			MyLogger.info("Exception occurred : " + e);
			result = false;
			throw e1;
		}
		return result;

	}

	// method to highlight element
	public void highLightElement(String element) {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		if (element.startsWith("Ext")) {
			// js.executeScript(element + ".setAttribute('style','border: solid 5px
			// yellow')");
			js.executeScript(element + ".style.border='2px solid yellow';");
		} else {
			// js.executeScript("arguments[0].setAttribute('style','border: solid 5px
			// yellow')",
			// getDriver().findElement(By.xpath(element)));
			js.executeScript("arguments[0].style.border='2px solid yellow';",
					getDriver().findElement(By.xpath(element)));
		}
	}

	// method to check element is present on page UI by COMP-QUERY
	public boolean compwait(String ele, ExtentTest logInfo) throws Exception {
		String query = prop.getProperty(ele);
		boolean found = false;
		Exception e1 = null;
		String compquery;
		final long startTime = System.currentTimeMillis();
		while ((System.currentTimeMillis() - startTime) < 30000) {
			try {
				JavascriptExecutor jse = (JavascriptExecutor) getDriver();
				if (query.contains("dom.")) {
					compquery = query.replaceAll("dom.+", "");
					found = (boolean) jse.executeScript("return " + compquery + "isVisible()");
					if (found) {
						break;
					}
				} else if (query.contains("dom")) {
					compquery = query.replaceAll("dom", "");
					found = (boolean) jse.executeScript("return " + compquery + "isVisible()");
					if (found) {
						break;
					}
				} else if (query.contains("[0].select")) {
					String[] q1 = query.split("select");
					compquery = q1[0];
					found = (boolean) jse.executeScript("return " + compquery + "el.isVisible()");
					if (found) {
						break;
					}
				} else if (query.contains("getSelectionModel()")) {
					String[] q1 = query.split("getSelectionModel()");
					compquery = q1[0];
					found = (boolean) jse.executeScript("return " + compquery + "el.isVisible()");
					if (found) {
						break;
					}
				} else {
					compquery = query;
					found = (boolean) jse.executeScript("return " + compquery + ".el.isVisible()");
					if (found) {
						break;
					}
				}
				// Thread.sleep(2000);
				long endTime = System.currentTimeMillis();
				long totalTime = endTime - startTime;
				// MyLogger.info("False: Element not found after waiting for:" + totalTime + "ms
				// : " + query);
			} catch (Exception e) {
				Thread.sleep(2000);
				long endTime = System.currentTimeMillis();
				long totalTime = endTime - startTime;
				// MyLogger.info("Element not found after waiting for:" + totalTime + " ms : " +
				// query);
				e1 = e;
			}
		}
		if (found) {
			if (query.contains("[0].select")) {
				String[] q2 = query.split("select");
				String q3 = q2[0] + "el.dom";
				scrollElement(q3);
				logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), ele + " is found");
			} else {
				scrollElement(query);
				logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), ele + " is found");
			}

			// highLightElement(query);
			// logInfo.addScreenCaptureFromBase64String((captureScreenShotBase64(getDriver())),ele);
		} else {
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			MyLogger.info("Exception : Element not found after waiting for:" + totalTime + "ms : " + query);
			if (e1 != null) {
				logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), ele + " is not found");
				throw e1;
			} else {
				logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), ele + " is not found");
				throw new Exception("Element not found after waiting for:" + totalTime + "ms : " + query);
			}
		}
		return found;
	}

	public boolean isXpathPresent(String xpath, ExtentTest logInfo) throws Exception {
		Exception e1 = null;
		boolean found = false;
		final long startTime = System.currentTimeMillis();
		try {
			waitForPageToLoad();
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(30));
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			if (!(element == null)) {
				// wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(element)));
				// wait.until(ExpectedConditions.stalenessOf(element));
				found = true;
			} else {
				found = false;
			}
		} catch (Exception e) {
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			MyLogger.info("Element not found after waiting for:" + totalTime + " ms : " + xpath);
			e.printStackTrace();
			e1 = e;
		}
		if (found) {
			scrollElement(xpath);
			highLightElement(xpath);
			// logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()));
			logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), xpath + " is displayed");
			// logInfo.addScreenCaptureFromBase64String((captureScreenShotBase64(getDriver())),
			// ele);
		} else {
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			MyLogger.info("Exception : Element not found after waiting for:" + totalTime + " ms : " + xpath);
			logInfo.addScreenCaptureFromPath(captureScreenShot(getDriver()), xpath + " is not displayed");
			throw e1;
		}
		return found;
	}
}