package models

/** Категории товаров (enum с полем). */
enum class Category(val title: String) {
    ELECTRONICS("Электроника"),
    BOOKS("Книги"),
    CLOTHES("Одежда"),
    FOOD("Продукты")
}
