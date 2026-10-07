package models

enum class OrderStatus(val title: String) {
    NEW("Новый"),
    PAID("Оплачен"),
    SHIPPED("Отправлен"),
    DELIVERED("Доставлен"),
    CANCELLED("Отменён");

    fun allowedNext(): List<OrderStatus> = when (this) {
        NEW -> listOf(PAID, CANCELLED)
        PAID -> listOf(SHIPPED, CANCELLED)
        SHIPPED -> listOf(DELIVERED)
        DELIVERED, CANCELLED -> emptyList()
    }

    fun canTransitionTo(next: OrderStatus): Boolean = next in allowedNext()
}
