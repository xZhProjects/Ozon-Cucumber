package ru.appline.framework.utils;

import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestStepFinished;
import io.qameta.allure.Allure;
import io.qameta.allure.cucumber5jvm.AllureCucumber5Jvm;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import ru.appline.framework.managers.DriverManager;

import static io.cucumber.plugin.event.Status.FAILED;

public class CucumberListener extends AllureCucumber5Jvm {
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepFinished.class, (testStepFinished) -> {
            if (testStepFinished.getResult().getStatus().equals(FAILED)) {
                Allure.getLifecycle().addAttachment("screenshot", "image/png", null,
                        ((TakesScreenshot) DriverManager.getINSTANCE().getDriver()).getScreenshotAs(OutputType.BYTES));
            }

        });
        super.setEventPublisher(publisher);
    }
}