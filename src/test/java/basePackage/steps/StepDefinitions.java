package basePackage.steps;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import com.aventstack.extentreports.ExtentTest;

import basePackage.utilities.MyLogger;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefinitions extends ExecutionMethods {
	boolean result;
	ExtentTest extentStep;
	Properties prop = getORProp();
	WebDriver driver = getDriver();
	public String validEmailAddress = null;
	public String memberForRoleFilterCheck = null;
	public String TCName = null;
	public String status = null;

	@Before
	public void startScenario(Scenario Scenario) {
		Collection<String> tags = Scenario.getSourceTagNames();
		MyLogger.info("");
		MyLogger.info("");
		scenarioName.set(Scenario.getName().split("_")[0]);
		MyLogger.info("******Start Scenario: " + Scenario.getName() + " " + tags.toString() + " : "
				+ getBrowserName().toUpperCase() + "  " + Thread.currentThread().getId() + " ******");
		scenario = extent.createTest(Scenario.getName()).assignDevice(getBrowserName().toUpperCase());
	}

	public void beforeStep() {
		MyLogger.info("");
		MyLogger.info("======Start Step: " + StepListener.stepName.get() + " : " + getBrowserName().toUpperCase() + " "
				+ Thread.currentThread().getId() + " =====");

		if (!StepListener.stepName.get().contains("verif")) {
			extentStep = scenario.createNode(StepListener.stepName.get());
		}
		result = false;
	}

	public void afterStep(boolean result) throws Throwable {
		MyLogger.info("======End Step: " + StepListener.stepName.get() + " : " + getBrowserName().toUpperCase() + " "
				+ Thread.currentThread().getId() + " =====");
		if (!result && getDefaultProp().getProperty("FailedScreenshots").equalsIgnoreCase("Y")) {
			extentStep.addScreenCaptureFromPath(captureScreenShot(driver), StepListener.stepName.get());
		}
		logReport(result, StepListener.stepName.get(), extentStep, null);

	}

	public void afterStep(boolean result, Exception e) throws Throwable {
		MyLogger.info("======End Step: " + StepListener.stepName.get() + " : " + getBrowserName().toUpperCase() + " "
				+ Thread.currentThread().getId() + " =====");
		if (getDefaultProp().getProperty("FailedScreenshots").equalsIgnoreCase("Y")) {
			extentStep.addScreenCaptureFromPath(captureScreenShot(driver), StepListener.stepName.get());
		}
		logReport(result, StepListener.stepName.get(), extentStep, e);
	}

	@After
	public void closeScenario(Scenario Scenario) throws Exception {
		MyLogger.info("******End Scenario: " + Scenario.getName() + " : " + getBrowserName().toUpperCase() + "  "
				+ Thread.currentThread().getId() + "******");
		String scenarioStatus = scenario.getStatus().toString();
		@SuppressWarnings("unused")
		boolean scenarioStatusFinal = scenarioStatus.equalsIgnoreCase("PASS") ? true : false;
		System.out.println("==========Scenario Status " + Scenario.getStatus() + "================");
		if (Scenario.getStatus().toString().equalsIgnoreCase("UNDEFINED")) {
			System.out.println("***>> Scenario '" + Scenario.getName() + "' failed at line(s) " + Scenario.getLine()
					+ " with status '" + Scenario.getStatus() + "");
			scenario.fail("SCENARIO STATUS : " + Scenario.getStatus().toString());
		}
		// rg.get().report(scenarioStatusFinal, "", Scenario.getName());
	}

	public void logReport(boolean result, String step, ExtentTest logInfo, Exception e) {
		try {
			if (result) {
				logResult("PASS", driver, logInfo, step, e);
				Assert.assertTrue(true);
			} else if (!result) {
				logResult("FAIL", driver, logInfo, step, e);
				Assert.fail(step, e);
			}
		} catch (Exception e1) {
			logResult("FAIL", driver, logInfo, step, e1);
			Assert.fail(step, e);
		}
	}

	// For Tools QA
	@Then("User clicks {string} menu")
	public void user_clicks_the_mainMenu(String mainMenu) throws Throwable {
		int i = 0;
		Boolean resultArr[] = new Boolean[100];
		boolean result = false;
		try {
			this.beforeStep();
			resultArr[i++] = user_clicks_toolsQA_mainMenu(mainMenu, extentStep);
			resultArr[i++] = user_clicks_subMenu_and_perform(extentStep);
			result = !Arrays.asList(resultArr).contains(false);
			this.afterStep(result);
		} catch (Exception e) {
			e.printStackTrace();
			this.afterStep(result, e);
		}
	}

	@Given("User launches the browser with {string} website")
	public void user_launches_the_browser_with_website(String url) throws Throwable {
		int i = 0;
		Boolean resultArr[] = new Boolean[100];
		boolean result = false;
		try {
			this.beforeStep();
			resultArr[i++] = userLoadsURL(url, extentStep);
			result = !Arrays.asList(resultArr).contains(false);
			this.afterStep(result);
		} catch (Exception e) {
			e.printStackTrace();
			this.afterStep(result, e);
		}
	}

	// For Swag Labs
	@And("User login into the swagLabs Portal")
	public void user_login_into_the_swag_labs_portal() throws Throwable {
		int i = 0;
		Boolean resultArr[] = new Boolean[100];
		boolean result = false;

		try {
			this.beforeStep();
			resultArr[i++] = user_login_into_swagLab(extentStep);
			result = !Arrays.asList(resultArr).contains(false);
			this.afterStep(result);
		} catch (Exception e) {
			e.printStackTrace();
			this.afterStep(result, e);
		}
	}

	@Then("User proceeding with an order")
	public void user_proceeding_with_an_order() throws Throwable {
		int i = 0;
		Boolean resultArr[] = new Boolean[100];
		boolean result = false;
		try {
			this.beforeStep();
			resultArr[i++] = user_do_sorting(extentStep);
			resultArr[i++] = user_adds_product_inCart(extentStep);
			resultArr[i++] = user_process_the_cart(extentStep);
			resultArr[i++] = user_process_the_checkOut_information(extentStep);
			resultArr[i++] = user_process_the_checkOut_overView(extentStep);
			result = !Arrays.asList(resultArr).contains(false);
			this.afterStep(result);
		} catch (Exception e) {
			e.printStackTrace();
			this.afterStep(result, e);
		}
	}
}
