package orders

/** Интерфейс способа оплаты. Реализаций несколько, это и есть полиморфизм. */
interface PaymentMethod {
    val name: String
    fun pay(amount: Double): Boolean
}

/** Оплата картой: номер должен состоять из 16 цифр. */
class CardPayment(private val cardNumber: String) : PaymentMethod {
    override val name: String = "Банковская карта"

    override fun pay(amount: Double): Boolean =
        cardNumber.length == 16 && cardNumber.all { it.isDigit() } && amount > 0
}

/** Оплата с электронного кошелька: списывает деньги, если хватает баланса. */
class WalletPayment(private var balance: Double) : PaymentMethod {
    override val name: String = "Электронный кошелёк"

    override fun pay(amount: Double): Boolean {
        if (amount > balance) {
            return false
        }
        balance -= amount
        return true
    }
}

/** Оплата наличными при получении. */
class CashPayment : PaymentMethod {
    override val name: String = "Наличные"

    override fun pay(amount: Double): Boolean = amount > 0
}
