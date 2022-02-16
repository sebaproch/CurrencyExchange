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
        System.setProperty("webdriver.chrome.driver",
                Utils.CHROME_DRIVER_LOCATION);
        driver = new ChromeDriver();
        page = new CurrencyExchangeCalculatorPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10000));
        driver.get(Utils.BASE_URL);
    }

    @Test
    public void fillAmountBoxTest1() {
        page.fillAmountBox(page.AMOUNT);
        Assert.assertEquals(page.checkIfNotEmptyInputBox(), page.AMOUNT);
    }

    @Test(priority = 1)
    public void changeLocalizationTest2() throws Exception{
        page.clickDropUp();
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
    public void afterClass() {
        driver.quit();
    }
}




