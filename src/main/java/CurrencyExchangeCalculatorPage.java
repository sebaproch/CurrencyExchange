import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
    protected WebElement dropup;
    @FindBy(xpath = "//div[@class='dropup']//button[@id='countries-dropdown']//span[@class='caret']")
    protected WebElement countrySelector;
    @FindBy(xpath = "//ul[@class='dropdown-menu']//li//a//span")
    protected WebElement popupSelector;
    @FindBy(xpath = "//span[contains(@class, 'flag-icon-pl')]")
    protected WebElement flagIcon;
    @FindBy(xpath = "//button[@id='countries-dropdown']")
    protected WebElement countryButton;

    String xpathAllRows = "//table//tr[@class='ng-scope']";
    String dropdownElement = "//ul[@class='dropdown-menu']//li//a";

    public CurrencyExchangeCalculatorPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, 7);
    }

    public void fillAmountBox(double quantity) {
        wait.until(ExpectedConditions.visibilityOf(notEmptyInputBox));
        emptyInputBox.sendKeys(String.valueOf(quantity));
    }

    public Double checkIfNotEmptyInputBox() {
        wait.until(ExpectedConditions.visibilityOf(notEmptyInputBox));
        WebElement visibleValue = notEmptyInputBox;
        Double quantityValue = Double.parseDouble(visibleValue.getAttribute("value"));
        return quantityValue;
    }

    public void clickDropup() {
        wait.until(ExpectedConditions.visibilityOf(dropup));
        dropup.click();
    }

    public void clickCountrySelector(String name) throws Exception {
        wait.until(ExpectedConditions.visibilityOf(countrySelector));
        countrySelector.click();
        wait.until(ExpectedConditions.visibilityOf(popupSelector));
        List<WebElement> allElements = driver.findElements(By.xpath(dropdownElement));

        for (WebElement li : allElements) {
            try {
                if (li.getText().contains(name)) {
                    li.click();
                    break;
                }
            } catch (Exception e) {
            }
        }
    }

    public void clickFlagIcon() {
        wait.until(ExpectedConditions.visibilityOf(flagIcon));
        flagIcon.click();
    }

    public String checkChosenCountry() {
        wait.until(ExpectedConditions.visibilityOf(countryButton));
        String visibleCountry = countryButton.getText().replaceAll("\\s", "");
        return visibleCountry;
    }

    public List<String> checkingAllDifferences() throws Exception {
        List<String> issues = new ArrayList<>();
        Integer row = 1;
        List<WebElement> allRows = driver.findElements(By.xpath(xpathAllRows));
        for (WebElement tr : allRows) {
            WebElement paysera = tr.findElement(By.xpath("./td[@data-title='Paysera rate']//span[@class='ng-binding']"));
            double payseraAmount = 0;
            if (paysera.isDisplayed()) {
                payseraAmount = Double.parseDouble(paysera.getText().replaceAll(",", ""));
            }
            try {
                WebElement firstBank = tr.findElement(By.xpath("./td[@data-title='mBank amount']//span[@class='ng-binding']"));
                double firstBankAmount = 0;
                if (firstBank.isDisplayed()) {
                    firstBankAmount = Double.parseDouble(firstBank.getText().replaceAll(",", ""));
                    if (firstBankAmount < payseraAmount) {
                        WebElement differenceElement = tr.findElement(By.xpath("./td[@data-title='mBank amount']//span[contains(@class, 'other-bank-loss')]"));
                        double differenceAmount = 0;
                        if (firstBank.isDisplayed()) {
                            String text = differenceElement.getText().replaceAll("\\s", "").replaceAll(",", "").replaceAll("[\\[\\](){}]", "");
                            differenceAmount = Double.parseDouble(text);
                            double differenceAmountRound = Math.round(differenceAmount * 100) / 100;
                            double expected = firstBankAmount - payseraAmount;
                            double expectedRound = Math.round(expected * 100) / 100;
                            if (expectedRound != differenceAmountRound) {
                                issues.add("Invalid Bank Loss for row: " + row);
                            }
                        } else {
                            issues.add("Bank Amount is not presented for row: " + row);
                        }
                    }
                }
            } catch (Exception e) {
            }
            row++;
        }
        return issues;
    }
}



