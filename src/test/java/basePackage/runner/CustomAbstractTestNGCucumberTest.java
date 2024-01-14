package basePackage.runner;

import org.testng.ITestContext;

import org.testng.annotations.BeforeClass;

import org.testng.annotations.AfterClass;

import org.testng.annotations.DataProvider;

import org.testng.annotations.Test;

import org.testng.xml.XmlTest;

import basePackage.steps.CommonFunctions;
import io.cucumber.testng.CucumberPropertiesProvider;

import io.cucumber.testng.FeatureWrapper;

import io.cucumber.testng.PickleWrapper;

import io.cucumber.testng.TestNGCucumberRunner;

public class CustomAbstractTestNGCucumberTest extends CommonFunctions {

	private TestNGCucumberRunner testNGCucumberRunner;

	@BeforeClass(alwaysRun = true)

	public void setUpClass(ITestContext context) {

		XmlTest currentXmlTest = context.getCurrentXmlTest();

		CucumberPropertiesProvider properties = currentXmlTest::getParameter;

		testNGCucumberRunner = new TestNGCucumberRunner(this.getClass(), properties);

	}

	@SuppressWarnings("unused")

	@Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")

	public void runScenario(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) {

		// the 'featureWrapper' parameter solely exists to display the feature

		// file in a test report

		testNGCucumberRunner.runScenario(pickleWrapper.getPickle());

	}

	/**
	 * 
	 * Returns two dimensional array of {@link PickleWrapper}s with their
	 * 
	 * associated {@link FeatureWrapper}s.
	 *
	 * 
	 * 
	 * @return a two dimensional array of scenarios features.
	 * 
	 */

	@DataProvider

	public Object[][] scenarios() {

		if (testNGCucumberRunner == null) {

			return new Object[0][0];

		}

		return testNGCucumberRunner.provideScenarios();

	}

	@AfterClass(alwaysRun = true)
	public void tearDownClass() {
		if (testNGCucumberRunner == null) {
			return;
		}
		testNGCucumberRunner.finish();
	}

}
