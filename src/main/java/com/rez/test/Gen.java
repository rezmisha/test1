package com.rez.test;

class Gen<T> {
    T ob; // объявление объекта типа T

    // Передать конструктору ссылку на объект типа T
    Gen(T o) {
        ob = o;
    }

    // Вернуть ob
    T getob() {
        return ob;
    }

    // Показать тип T
    void showType() {
        System.out.println("Тип T: " + ob.getClass().getName());
    }
}

