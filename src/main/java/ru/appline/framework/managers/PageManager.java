package ru.appline.framework.managers;

import ru.appline.framework.pages.BasePage;
import ru.appline.framework.pages.BasketPage;

import java.util.HashMap;
import java.util.Map;


public class PageManager {

    private static PageManager INSTANCE = null;
    private Map<String, BasePage> mapPages = new HashMap<>();

    private PageManager() {
    }

    public static PageManager getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new PageManager();
        }
        return INSTANCE;
    }

    public void shutDown() {
        mapPages.clear();
    }
    public <T extends BasePage> T getPage(Class<T> tClass) {
        if (mapPages.isEmpty() || mapPages.get(tClass.getName()) == null) {
            try {
                mapPages.put(tClass.getName(), tClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return (T) mapPages.get(tClass.getName());
    }
}