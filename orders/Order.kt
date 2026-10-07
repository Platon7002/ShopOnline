package orders

import models.OrderItem
import models.OrderStatus
import models.Product
import results.OperationResult
import users.User

private fun money(value: Double): String = "%.2f руб.".format(value)

/** Заказ. Состояние скрыто (private), менять его можно только методами. */
class Order(val id: Int, val customer: User) {

    private val _items = mutableListOf<OrderItem>()

    // Наружу отдаём копию списка, чтобы его нельзя было испортить
    val items: List<OrderItem>
        get() = _items.toList()

    var status: OrderStatus = OrderStatus.NEW
        private set

    val subtotal: Double
        get() = _items.sumOf { it.total }

    val total: Double
        get() = customer.applyDiscount(subtotal)

    fun addItem(product: Product, quantity: Int): OperationResult {
        if (status != OrderStatus.NEW) {
            return OperationResult.Failure("Нельзя менять заказ в статусе «${status.title}»")
        }
        if (quantity <= 0) {
            return OperationResult.Failure("Количество должно быть положительным")
        }
        _items.add(OrderItem(product, quantity))
        return OperationResult.Success("Добавлено: ${product.name} × $quantity")
    }

    fun pay(method: PaymentMethod): OperationResult {
        if (_items.isEmpty()) {
            return OperationResult.Failure("Заказ пуст, оплачивать нечего")
        }
        if (!status.canTransitionTo(OrderStatus.PAID)) {
            return OperationResult.Failure("Оплата невозможна в статусе «${status.title}»")
        }
        if (!method.pay(total)) {
            return OperationResult.Failure("Оплата отклонена (${method.name})")
        }
        return moveTo(OrderStatus.PAID)
    }

    fun ship(): OperationResult = moveTo(OrderStatus.SHIPPED)

    fun deliver(): OperationResult = moveTo(OrderStatus.DELIVERED)

    fun cancel(): OperationResult = moveTo(OrderStatus.CANCELLED)

    /** Единственное место, где меняется статус, с проверкой допустимости перехода. */
    private fun moveTo(next: OrderStatus): OperationResult {
        if (!status.canTransitionTo(next)) {
            return OperationResult.Failure("Переход «${status.title}» → «${next.title}» невозможен")
        }
        status = next
        return OperationResult.Success("Заказ #$id: статус «${next.title}»")
    }

    fun summary(): String {
        val lines = StringBuilder()
        for (item in _items) {
            lines.append("   - ${item.product.name} × ${item.quantity} = ${money(item.total)}\n")
        }
        return """
            |Заказ #$id | клиент: ${customer.name} | статус: ${status.title}
            |${lines.toString().trimEnd()}
            |Сумма: ${money(subtotal)}, скидка ${customer.discountPercent}%, к оплате: ${money(total)}
        """.trimMargin()
    }
}
