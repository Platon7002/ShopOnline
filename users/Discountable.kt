package users

/** Интерфейс: всё, что имеет скидку. Есть реализация по умолчанию. */
interface Discountable {
    val discountPercent: Int

    fun applyDiscount(amount: Double): Double = amount * (100 - discountPercent) / 100
}
