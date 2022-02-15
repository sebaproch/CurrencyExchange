import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CurrencyExchangeCalculatorTest {
    private WebDriver driver;
    CurrencyExchangeCalculatorPage page;

    @BeforeClass
    public void beforeClass() throws Exception {
        System.setProperty("webdriver.chrome.driver",
                Utils.CHROME_DRIVER_LOCATION);
        driver = new ChromeDriver();
        page = new CurrencyExchangeCalculatorPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(Utils.BASE_URL);
    }

    @Test(priority = 0)
    public void fillAmountBoxTest1() throws Exception {
        page.fillAmountBox(page.AMOUNT);
        Assert.assertEquals(page.checkIfNotEmptyInputBox(), page.AMOUNT);
    }

    @Test(priority = 1)
    public void changeLocalizationTest2() throws Exception {
        page.clickDropup();
        page.clickCountrySelector(page.COUNTRY);
        page.clickFlagIcon();
        Assert.assertEquals(page.checkChosenCountry(), page.COUNTRY);
    }

    @Test(priority = 2)
    public void representDifferenceInTextBoxTest3() throws Exception {
        List<String> issues = page.checkingAllDifferences();
        Assert.assertTrue(issues.isEmpty(), "Issues: " + Arrays.toString(issues.toArray()));
    }

    @AfterClass
    public void afterClass() throws Exception {
        driver.quit();
    }
}




