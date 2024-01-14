package basePackage.runner;

import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import basePackage.utilities.MyLogger;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(plugin = { "pretty", "html:target/site/cucumber-pretty", "json:target/cucumber/cucumber.json",
		"basePackage.steps.StepListener",
		"timeline:test-output-thread/" }, features = { "src/test/java/basePackage/features" }, glue = {
				"basePackage/steps" }, monochrome = true, publish = true, tags = "@swagLabFullFlow ")

public class TestRunner extends CustomAbstractTestNGCucumberTest {
	@BeforeClass(alwaysRun = true)
	public void setUpClassLog(ITestContext context) {
		MyLogger.startTest(this.getClass().getSimpleName() + "_" + context.getName());
	}
}
