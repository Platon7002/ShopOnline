package users

/** Премиум-покупатель: наследует Customer и переопределяет скидку и роль. */
class PremiumCustomer(id: Int, name: String, email: String) : Customer(id, name, email) {

    override val role: String
        get() = "Премиум-покупатель"

    override val discountPercent: Int
        get() = 15

    override fun describe(): String = "[VIP] " + super.describe()
}
