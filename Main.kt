import models.Category
import models.OrderStatus
import models.Product
import orders.CardPayment
import orders.CashPayment
import orders.OrderService
import orders.WalletPayment
import results.OperationResult
import users.Admin
import users.Customer
import users.PremiumCustomer
import users.User

fun printResult(result: OperationResult) {
    val text = when (result) {
        is OperationResult.Success -> "[OK] ${result.message}"
        is OperationResult.Failure -> "[ОШИБКА] ${result.reason}"
        is OperationResult.NotFound -> "[НЕ НАЙДЕНО] Заказ #${result.orderId} не существует"
    }
    println(text)
}

fun printTitle(title: String) {
    println()
    println("=== $title ===")
}

fun main() {
    printTitle("1. Каталог (data class, enum)")
    val laptop = Product(1, "Ноутбук", 80000.0, Category.ELECTRONICS)
    val book = Product(2, "Книга по Kotlin", 500.0, Category.BOOKS)
    val headphones = Product(3, "Наушники", 3000.0, Category.ELECTRONICS)
    println(laptop)

    val saleLaptop = laptop.copy(price = 70000.0)
    println("copy(): $saleLaptop")
    println("laptop == saleLaptop ? ${laptop == saleLaptop}")

    for (category in Category.values()) {
        println("Категория: ${category.title}")
    }

    try {
        Product(99, "Брак", -5.0, Category.FOOD)
    } catch (e: IllegalArgumentException) {
        println("Некорректный товар не создан: ${e.message}")
    }

    printTitle("2. Пользователи (наследование, полиморфизм)")
    val anna = Customer(1, "Анна", "anna@mail.ru")
    val boris = PremiumCustomer(2, "Борис", "boris@mail.ru")
    val admin = Admin(3, "Админ", "admin@shop.ru")

    val users: List<User> = listOf(anna, boris, admin)
    for (user in users) {
        println(user.describe())       
    }
    println("Смена email Анны на неверный: ${anna.changeEmail("не-почта")}")
    println("Смена email Анны на верный: ${anna.changeEmail("anna@gmail.com")}, теперь ${anna.email}")

    printTitle("3. Заказ Анны: полный путь")
    val service = OrderService()
    val order1 = service.createOrder(anna)

    printResult(service.addItem(order1.id, laptop, 1))
    printResult(service.addItem(order1.id, book, 2))
    printResult(service.addItem(order1.id, headphones, 0))      
    println(order1.summary())

    printResult(service.pay(order1.id, WalletPayment(50000.0)))
    printResult(service.pay(order1.id, CardPayment("1234567812345678")))
    printResult(service.addItem(order1.id, book, 1))          
    printResult(service.deliver(order1.id))                 
    printResult(service.ship(order1.id))
    printResult(service.cancel(order1.id))                
    printResult(service.deliver(order1.id))
    println("Баллы Анны после доставки: ${anna.loyaltyPoints}")

    printTitle("4. Заказ Бориса: отмена и ошибки")
    val order2 = service.createOrder(boris)
    printResult(service.pay(order2.id, CashPayment()))        
    printResult(service.addItem(order2.id, headphones, 2))
    printResult(service.cancel(order2.id))
    printResult(service.pay(order2.id, CashPayment()))         
    printResult(service.ship(999))                            
    println(order2.summary())

    printTitle("5. Итоги")
    println("Допустимые переходы статусов:")
    for (status in OrderStatus.values()) {
        val next = status.allowedNext().map { it.title }
        println("  ${status.title} -> ${if (next.isEmpty()) "конец" else next.joinToString(", ")}")
    }
    println("Заказов по статусам:")
    for ((status, count) in service.countByStatus()) {
        println("  ${status.title}: $count")
    }
}
