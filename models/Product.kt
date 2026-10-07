package models

/** Товар (data class: готовые toString, equals, copy). */
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val category: Category
) {
    init {
        require(price > 0) { "Цена товара должна быть положительной: $price" }
    }
}
