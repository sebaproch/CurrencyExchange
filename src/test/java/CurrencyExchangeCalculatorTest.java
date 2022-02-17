import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class CurrencyExchangeCalculatorTest {
    private WebDriver driver;
    CurrencyExchangeCalculatorPage page;

    @BeforeClass
    public void beforeClass() {
        driver = DriverFactory.getDriver();
        page = new CurrencyExchangeCalculatorPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.get(Utils.BASE_URL);
    }

    @Test(priority = 1)
    public void fillAmountBoxTest1() {
        page.fillAmountBox(page.AMOUNT);
        Assert.assertEquals(page.checkIfNotEmptyInputBox(), page.AMOUNT);
    }

    @Test(priority = 2)
    public void changeLocalizationTest2() {
        page.clickDropUp();
        page.clickCountrySelector(page.COUNTRY);
        page.clickFlagIcon();
        Assert.assertEquals(page.checkChosenCountry(), page.COUNTRY);
    }

    @Test(priority = 3)
    public void representDifferenceInTextBoxTest3() {
        List<String> issues = page.checkingAllDifferences();
        Assert.assertTrue(issues.isEmpty(), "Issues: " + Arrays.toString(issues.toArray()));
    }

    @AfterClass
    public void afterClass() {
        driver.quit();
    }
}




