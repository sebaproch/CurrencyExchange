import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CurrencyExchangeCalculatorPage extends PageObject {
    private WebDriverWait wait;
    protected final String COUNTRY = "Poland";
    protected final Double AMOUNT = 255.0;
    @FindBy(xpath = "//input[contains(@class, 'ng-not-empty')][@type='text']")
    protected WebElement notEmptyInputBox;
    @FindBy(xpath = "//input[contains(@class, 'ng-empty')][@type='text']")
    protected WebElement emptyInputBox;
    @FindBy(xpath = "//span[@class='dropup']")
    protected WebElement dropUp;
    @FindBy(xpath = "//div[@class='dropup']//button[@id='countries-dropdown']//span[@class='caret']")
    protected WebElement countrySelector;
    @FindBy(xpath = "//ul[@class='dropdown-menu']//li//a//span")
    protected WebElement popupSelector;
    @FindBy(xpath = "//span[contains(@class, 'flag-icon-pl')]")
    protected WebElement flagIcon;
    @FindBy(xpath = "//button[@id='countries-dropdown']")
    protected WebElement countryButton;
    @FindBy(xpath = "//ul[@class='dropdown-menu']//li//a")
    protected List<WebElement> dropdownElement;
    @FindBy(xpath = "//td[contains(@data-title, 'sera')][1]")
    protected WebElement paysValueElement;
    @FindBy(xpath = "//td[@data-title='mBank amount'][1]")
    protected WebElement firstBankValueElement;

    String allE = "./td[@data-title='mBank amount']";
    String xpathAllRows = "//table//tr[@class='ng-scope']";
    String differenceElementValue = "./td[@data-title='mBank amount']//span[contains(@class, 'other-bank-loss')]";
    String paysValue = "./td[contains(@data-title, 'sera')]//span[@class='ng-binding']";
    String firstBankView = "./td[@data-title='mBank amount']//span[@class='ng-binding']";

    public CurrencyExchangeCalculatorPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(8));
    }

    public void fillAmountBox(double quantity) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(9));
        wait.until(ExpectedConditions.visibilityOf(notEmptyInputBox));
        emptyInputBox.sendKeys(String.valueOf(quantity));
    }

    public Double checkIfNotEmptyInputBox() {
        wait.until(ExpectedConditions.visibilityOf(notEmptyInputBox));
        WebElement visibleValue = notEmptyInputBox;
        return Double.parseDouble(visibleValue.getAttribute("value"));
    }

    public void clickDropUp() {
        wait.until(ExpectedConditions.visibilityOf(dropUp));
        dropUp.click();
    }

    public void clickCountrySelector(String name) {
        wait.until(ExpectedConditions.visibilityOf(countrySelector));
        countrySelector.click();
        wait.until(ExpectedConditions.visibilityOf(popupSelector));
        List<WebElement> allElements = dropdownElement;

        for (WebElement li : allElements) {
            try {
                if (li.getText().contains(name)) {
                    li.click();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void clickFlagIcon() {
        wait.until(ExpectedConditions.visibilityOf(flagIcon));
        flagIcon.click();
    }

    public String checkChosenCountry() {
        wait.until(ExpectedConditions.visibilityOf(countryButton));
        return countryButton.getText().replaceAll("\\s", "");
    }

    public List<String> checkingAllDifferences() {
        List<String> issues = new ArrayList<>();
        int row = 1;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(11));
        wait.until(ExpectedConditions.visibilityOf(paysValueElement));
        List<WebElement> allRows = driver.findElements(By.xpath(xpathAllRows));
        for (WebElement tr : allRows) {
            String t = tr.findElement(By.xpath(allE)).getText();
            if (!t.contains(".")) {
                continue;
            }
            WebElement pays = tr.findElement(By.xpath(paysValue));
            double paysAmount;
            try {
                if (pays.isDisplayed()) {
                    paysAmount = Double.parseDouble(pays.getText().replaceAll(",", ""));
                    wait.until(ExpectedConditions.visibilityOf(firstBankValueElement));
                    WebElement firstBank = tr.findElement(By.xpath(firstBankView));
                    double firstBankAmount;
                    if (firstBank.isDisplayed()) {
                        firstBankAmount = Double.parseDouble(firstBank.getText().replaceAll(",", ""));
                        if (firstBankAmount < paysAmount) {
                            WebElement differenceElement = tr.findElement(By.xpath(differenceElementValue));
                            double differenceAmount;
                            String text = differenceElement.getText().replaceAll("\\s", "").replaceAll(",", "").replaceAll("[\\[\\](){}]", "");
                            differenceAmount = Double.parseDouble(text);
                            double differenceAmountRound = Math.round(differenceAmount);
                            double expected = firstBankAmount - paysAmount;
                            double expectedRound = Math.round(expected);
                            if (expectedRound != differenceAmountRound) {
                                issues.add("Invalid Bank Loss for row: " + row);
                            }
                        }
                    }
                }
                row++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return issues;
    }
}



