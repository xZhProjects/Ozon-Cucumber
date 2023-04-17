package ru.appline.framework.managers;

import ru.appline.framework.model.Product;
import ru.appline.framework.utils.PropsConst;

import java.util.concurrent.TimeUnit;

import static ru.appline.framework.utils.PropsConst.BASE_URL;
import static ru.appline.framework.utils.PropsConst.PAGE_LOAD_TIMEOUT;

public class InitManager {
    private static final TestPropManager properties = TestPropManager.getTestPropManager();
    private static final DriverManager driverManager = DriverManager.getINSTANCE();
    private static final PageManager pageManager = PageManager.getINSTANCE();

    public static void initFramework() {
        driverManager.getDriver().manage().window().maximize();
        driverManager.getDriver().manage().timeouts()
                .implicitlyWait(Integer.parseInt(properties.getProperty(PropsConst.IMPLICITLY_WAIT)), TimeUnit.SECONDS);
        driverManager.getDriver().manage()
                .timeouts().pageLoadTimeout(Integer.parseInt(properties.getProperty(PAGE_LOAD_TIMEOUT)), TimeUnit.SECONDS);
        driverManager.getDriver().get(TestPropManager.getTestPropManager().getProperty(BASE_URL));
    }

    public static void quitFramework() {
        driverManager.quitDriver();
        pageManager.shutDown();
        Product.listProducts.clear();

    }
}
