package models

/** Позиция заказа: товар + количество. */
data class OrderItem(val product: Product, val quantity: Int) {
    init {
        require(quantity > 0) { "Количество должно быть положительным: $quantity" }
    }

    val total: Double
        get() = product.price * quantity
}
