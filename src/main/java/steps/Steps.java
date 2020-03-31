package steps;

import cucumber.api.java.ru.Тогда;
import pages.BasePage;
import pages.CartPage;
import pages.MainPage;
import pages.SearchPage;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static steps.Hooks.getData;


public class Steps {

    static BasePage pageObject;
    BasePage page = new MainPage();

    @Тогда("Выполнить поиск по {string}.")
    public void выполнить_поиск_по(String keywords) throws InterruptedException {
        page = page.search(keywords);
    }

    @Тогда("Ограничить цену до {int}.")
    public void ограничить_цену_до(int price) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Class example = Class.forName("pages.SearchPage");
        pageObject = (BasePage) example.newInstance();
        pageObject.input("максимальная цена", price);
    }

    @Тогда("Отметить чекбоксы:")
    public void отметить_чекбоксы(List<String> keywords) throws InterruptedException {
        keywords.forEach(element -> ((SearchPage) page).on(element));
    }

    @Тогда("Добавить в корзину из результатов поиска {string} {string}.")
    public void добавить_в_корзину_из_результатов_(String quantity, String evenOrNot) {
        ((SearchPage) page).add(quantity, evenOrNot);
        getData();
    }

    @Тогда("Перейти в корзину.")
    public void перейти_в_корзину() {
        page = page.toCart();
    }

    @Тогда("Проверить, что все добавленные ранее товары находятся в корзине.")
    public void проверить_что_все_добавленные_ранее_товары_находятся_в_корзине() {
        ((CartPage) page).checkItems();
    }

    @Тогда("Проверить, что отображается текст {string}.")
    public void проверить_что_отображается_текст(String string) {
        ((CartPage) page).checkLabel();
    }

    @Тогда("Убрать все товары из корзины.")
    public void убрать_все_товары_из_корзины() {
        ((CartPage) page).removeAll();
    }

    @Тогда("Проверить, что корзина не содержит никаких товаров.")
    public void проверить_что_корзина_не_содержит_никаких_товаров() {
        ((CartPage) page).checkEmpty();
    }
}
