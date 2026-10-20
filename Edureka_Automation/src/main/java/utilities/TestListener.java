package utilities;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String message = "STARTED: "
                + result.getMethod().getMethodName();

        Reporter.log(message, true);
        ReportUtility.log(message);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String message = "PASSED: "
                + result.getMethod().getMethodName();

        Reporter.log(message, true);
        ReportUtility.log(message);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String message = "FAILED: "
                + result.getMethod().getMethodName()
                + " | Reason: " + result.getThrowable();

        Reporter.log(message, true);
        ReportUtility.log(message);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String message = "SKIPPED: "
                + result.getMethod().getMethodName();

        Reporter.log(message, true);
        ReportUtility.log(message);
    }
}