package ru.appline.framework.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.appline.framework.managers.DriverManager;
import ru.appline.framework.managers.PageManager;
import ru.appline.framework.model.Product;

import java.util.List;

public class BasePage {

    protected PageManager application = PageManager.getINSTANCE();
    protected DriverManager driverManager = DriverManager.getINSTANCE();
    protected Actions action = new Actions(driverManager.getDriver());
    protected JavascriptExecutor js = (JavascriptExecutor)driverManager.getDriver();
    protected WebDriverWait wait = new WebDriverWait(driverManager.getDriver(), 10, 1000);

    public BasePage() {
        PageFactory.initElements(driverManager.getDriver(), this);
    }


    protected void scrollToElementJs(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }


    protected WebElement elementToBeClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }


    public WebElement elementToBeVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }


    public void fillInputField(WebElement field, String value) {
        scrollToElementJs(field);
        field.sendKeys(value);
    }

    public void fillInputField(WebElement field, Integer value) {
        scrollToElementJs(field);
        field.sendKeys("" + value);
    }

    public void explicitWait(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Integer utilParsInteger(String value) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(value.replaceAll("\\D",""));
    }


    public boolean isContainsAllProducts(List<Product> expectedProductsName, List<String> actualProductsName) {
        for (Product prod: expectedProductsName) {
            if(actualProductsName.contains(prod.getName())) return true;
        }
        return true;
    }

}
