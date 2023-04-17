package ru.appline.framework.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.appline.framework.managers.DriverManager;
import ru.appline.framework.model.Product;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static ru.appline.framework.model.Product.listProducts;


public class SearchPage extends BasePage {

    @FindBy(xpath = "//div[contains (text(), 'Цена')]/..//p[contains (text(), 'до')]/../input")
    private WebElement searchUpLimitElement;

    @FindBy(xpath = "//*[text() = 'NFC']/../..//input")
    private WebElement nfcCheckBox;

    @FindBy(xpath = "//*[text() = 'Xiaomi']/../..//input")
    private WebElement xiaomiCheckBox;

    @FindBy(xpath = "//*[text() = 'Samsung']/../..//input")
    private WebElement samsungCheckBox;

    @FindBy(xpath = "//*[text() = 'Beats']/../..//input")
    private WebElement beatsCheckBox;

    @FindBy(xpath = "//*[contains(text(), 'Высокий рейтинг')]/../../..//input")
    private WebElement maxRatingCheckBox;

    @FindBy(xpath = "//div[contains(@class,'search-result')]/div/div")
    private List<WebElement> allSearchElements;

    @FindBy(xpath = "//header/div/div/a/span[text() = 'Корзина']/../span[position() = 1]")
    private WebElement basketCountElement;

    @FindBy(xpath = "//header/div/div/a/span[text() = 'Корзина']/..//*[@class][position() = 2]")
    private WebElement basketElement;

    @FindBy(xpath = "//div[contains(@data-widget, 'searchResultsFilters')]")
    private WebElement searchFiltersElement;

    @FindBy(xpath = "//div[contains(text(), 'Бренды')]/..//span[@class='show']")
    private WebElement btn;

    private WebElement element;

    public SearchPage clickProductSearch(Integer upLimit) {
        scrollToElementJs(searchUpLimitElement);
        while (!searchUpLimitElement.getAttribute("value").isEmpty()) {
            searchUpLimitElement.sendKeys(Keys.BACK_SPACE);
        }
        fillInputField(searchUpLimitElement, upLimit);
        explicitWait(500);
        action.sendKeys(Keys.ENTER);
        utilWaitSearchFiltersElement("" + upLimit);
        assertEquals("Ограничение цены " + upLimit + " в графе 'до' заполнено некорректно", "" + upLimit, searchUpLimitElement.getAttribute("value"));
        return application.getPage(SearchPage.class);
    }

    public SearchPage fillCheckbox(String nameField, String value) {
        WebElement element = null;
        switch (nameField) {
            case "Высокий рейтинг":
                element = maxRatingCheckBox;
                break;
            case "NFC":
                element = nfcCheckBox;
                break;
            case "Beats":
                viewAllBrandsElements();
                element = beatsCheckBox;
                break;
            case "Samsung":
                viewAllBrandsElements();
                element = samsungCheckBox;
                break;
            case "Xiaomi":
                viewAllBrandsElements();
                element = xiaomiCheckBox;
                break;
            default:
                fail("Поле с наименованием '" + nameField + "' отсутствует на странице " + "'Поиска'");

        }
        Actions act = new Actions(DriverManager.getINSTANCE().getDriver());
        explicitWait(2000);
        act.moveToElement(element.findElement(By.xpath("./..")));
        if (Boolean.parseBoolean(value) ^ Boolean.parseBoolean(element.getAttribute("checked"))) {
            element.findElement(By.xpath("./..")).click();
            utilWaitSearchFiltersElement(nameField);
        }
        return this;
    }


    public BasketPage fillBasket(Integer countProduct) {
        for (int i = 1, j = 0; j < countProduct; i++) {
            List<WebElement> elm = allSearchElements.get(i * 2 - 1).findElements(By.xpath(".//span[text()='В корзину']"));
            if (elm.size() == 2) {
                elm.get(1).click();
                add(allSearchElements.get(i * 2 - 1));
                j++;
                wait.until(ExpectedConditions.textToBePresentInElement(basketCountElement, "" + j));
            } else if (!elm.get(0).findElement(By.xpath("./../../../../..//b")).getText().contains("час") && elm.size() == 1) {
                elm.get(0).click();
                add(allSearchElements.get(i * 2 - 1));
                j++;
            }
        }
        elementToBeClickable(basketElement).click();
        return application.getPage(BasketPage.class);
    }

    public BasketPage fillBasketWithHP() {
        int arraySize = 0;
        int j = 0;
        do {
            while (arraySize * 2 < allSearchElements.size()) {
                List<WebElement> inBasket = allSearchElements.get(arraySize * 2).findElements(By.xpath(".//span[text()='В корзину']"));
                if (inBasket.size() == 2) {
                    (inBasket.get(1)).click();
                    add(allSearchElements.get(arraySize * 2));
                    j++;
                    wait.until(ExpectedConditions.textToBePresentInElement(basketCountElement, "" + j));
                } else if (inBasket.size() == 1) {
                    if (!inBasket.get(0).findElement(By.xpath("./../../../../..//b")).getText().contains("час")) {
                        (inBasket.get(0)).click();
                        add(allSearchElements.get(arraySize * 2));
                        j++;
                        wait.until(ExpectedConditions.textToBePresentInElement(basketCountElement, "" + j));
                    } else if (!inBasket.get(0).findElement(By.xpath("./../../../../..//b")).getText().contains("час")){
                        continue;

                    }
                }
                arraySize++;
            }
            arraySize = 0;
        } while (nextPage());
        elementToBeClickable(basketElement).click();
        return application.getPage(BasketPage.class);
    }

    private boolean nextPage() {
        try {
            driverManager.getDriver().findElement(By.xpath("//div[contains(text(), 'Дальше')]")).click();
            return true;
        } catch (NoSuchElementException exception) {
            return false;
        }
    }

    public void viewAllBrandsElements() {
        try {
            DriverManager.getINSTANCE().getDriver().manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
            btn.click();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
        }
    }


    public void utilWaitSearchFiltersElement(String s) {
        int i = 0;
        boolean isPresentText = false;
        while (!isPresentText && i < 20) {
            try {
                scrollToElementJs(searchFiltersElement);
                isPresentText = searchFiltersElement.getText().replaceAll("[^A-Za-zА-Яа-я0-9]", "").contains(s);
                i++;
            } catch (StaleElementReferenceException e) {
                System.out.println("StaleElementReferenceException №" + i);
            }
        }
    }

    private void add(WebElement product) {
        String title = product.findElement(By.xpath(".//a/span")).getText();
        List<WebElement> spans = product.findElements(By.xpath("./div/div/span[contains(text(),'₽')]"));
        int value = 0;
        if (spans.get(0).getText().contains("-")) {
            value = utilParsInteger(spans.get(1).getText());
        } else {
            value = utilParsInteger(spans.get(0).getText());
        }
        listProducts.add(new Product(title, value));
    }
}
