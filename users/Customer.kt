package users

/** Обычный покупатель (наследует User). */
open class Customer(id: Int, name: String, email: String) : User(id, name, email) {

    var loyaltyPoints: Int = 0
        private set

    override val role: String
        get() = "Покупатель"

    override val discountPercent: Int
        get() = 5

    fun addPoints(points: Int) {
        if (points > 0) {
            loyaltyPoints += points
        }
    }

    override fun describe(): String = super.describe() + ", баллов: $loyaltyPoints"
}
