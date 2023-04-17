package ru.appline.framework.pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.appline.framework.model.Product;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static ru.appline.framework.model.Product.listProducts;


public class BasketPage extends BasePage {

    @FindBy(xpath = "//span[contains(text(), 'Удалить')]/../../a/span")
    private List<WebElement> productNameElements;

    @FindBy(xpath = "//span[contains(text(),'Добавить компанию')]")
    private WebElement alert;

    @FindBy(xpath = "//section[@data-widget='total']/div/div/div/span[position()=1]")
    private WebElement countProductsBasketElement;

    @FindBy(xpath = "//section[@data-widget='total']/div/div/div/span[position()=2]")
    private WebElement countProductsElement;

    @FindBy(xpath = "//span[contains (text(), 'Удалить выбранные')]")
    private WebElement deleteAllProductsElement;

    @FindBy(xpath = "//*[contains(text(), 'Удаление товаров')]")
    private List<WebElement> subWindowDeleteElement;

    @FindBy(xpath = "//div[contains(text(), 'Корзина')]")
    private WebElement checkOpen;

    @FindBy(xpath = "//*[contains(text(), 'Удаление товаров')]/../div/div/button/span/span")
    private WebElement subWindowDeleteButton;

    @FindBy(xpath = "//*[contains(text(), 'Корзина пуста')]")
    private List<WebElement> basketIsEmptyElement;


    public BasketPage closeAlert() {
        elementToBeVisible(alert);
        elementToBeClickable(alert.findElements(By.xpath("./../../../../..//button")).get(1)).click();
        return this;
    }

    public BasketPage checkOpenPage() {
        elementToBeVisible(checkOpen);
        return this;
    }

    public BasketPage checkProductsInBasket() {
        int size = listProducts.size();
        explicitWait(2500);
        assertEquals("Количество товаров не соответствует", listProducts.size(), productNameElements.size());
        List<String> actualProductsName = new ArrayList<>();
        for (WebElement element : productNameElements) {
            actualProductsName.add(element.getText());
        }
        assertTrue("Не все товары корректно были добавлены в корзину", isContainsAllProducts(listProducts, actualProductsName));
        StringBuilder textCountProducts = new StringBuilder();
        textCountProducts.append(countProductsBasketElement.getText().trim())
                .append(" - ")
                .append(countProductsElement.getText().trim().split(" •")[0]);
            assertEquals("Текст 'Ваша корзина - " + size + " товаров' отображается некорректно",
                    "Ваша корзина - " + size + " товаров",
                    textCountProducts.toString());
        return this;
    }


    public BasketPage addTextFile() {
        Product product = listProducts.stream()
                .max(Product::compareTo)
                .get();
        try (FileWriter out = new FileWriter("src/main/resources/allProducts.txt", false)) {
            out.write("Продукт с максимальной ценой: \n " + product.getName() + ", его цена: " + product.getPrice() + " ₽\n\n");
            out.write("Список товаров: \n ");
            int i = 1;
            for (Product prod : listProducts) {
                out.write("" + i + prod.toString());
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Allure.addAttachment("Файл со списком товаров:", "text/plain", new ByteArrayInputStream(getBytes()), "text");
        return this;
    }

    public BasketPage deleteAllProducts() {
        elementToBeClickable(deleteAllProductsElement).click();
        assertFalse(subWindowDeleteElement.isEmpty());
        elementToBeClickable(subWindowDeleteButton).click();
        explicitWait(2000);
        assertFalse("Нет сообщения о том что корзина пуста", basketIsEmptyElement.isEmpty());
        return this;
    }

    public byte[] getBytes() {
        try {
            return Files.readAllBytes(Paths.get("src/main/resources/allProducts.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
