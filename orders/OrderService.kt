package orders

import models.OrderStatus
import models.Product
import results.OperationResult
import users.Customer
import users.User
class OrderService {

    private val orders = mutableMapOf<Int, Order>()
    private var nextId = 1

    fun createOrder(customer: User): Order {
        val order = Order(nextId++, customer)
        orders[order.id] = order
        return order
    }

    private fun withOrder(orderId: Int, action: (Order) -> OperationResult): OperationResult {
        val order = orders[orderId] ?: return OperationResult.NotFound(orderId)
        return action(order)
    }

    fun addItem(orderId: Int, product: Product, quantity: Int): OperationResult =
        withOrder(orderId) { it.addItem(product, quantity) }

    fun pay(orderId: Int, method: PaymentMethod): OperationResult =
        withOrder(orderId) { it.pay(method) }

    fun ship(orderId: Int): OperationResult =
        withOrder(orderId) { it.ship() }

    fun cancel(orderId: Int): OperationResult =
        withOrder(orderId) { it.cancel() }

    fun deliver(orderId: Int): OperationResult = withOrder(orderId) { order ->
        val result = order.deliver()
        val customer = order.customer
        if (result is OperationResult.Success && customer is Customer) {
            customer.addPoints((order.total / 100).toInt())
        }
        result
    }

    fun countByStatus(): Map<OrderStatus, Int> =
        orders.values.groupingBy { it.status }.eachCount()
}
