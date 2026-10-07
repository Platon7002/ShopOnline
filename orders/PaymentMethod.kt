package orders

interface PaymentMethod {
    val name: String
    fun pay(amount: Double): Boolean
}

class CardPayment(private val cardNumber: String) : PaymentMethod {
    override val name: String = "Банковская карта"

    override fun pay(amount: Double): Boolean =
        cardNumber.length == 16 && cardNumber.all { it.isDigit() } && amount > 0
}

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

class CashPayment : PaymentMethod {
    override val name: String = "Наличные"

    override fun pay(amount: Double): Boolean = amount > 0
}
