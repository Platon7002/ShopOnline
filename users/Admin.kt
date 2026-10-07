package users

/** Администратор магазина: скидок не имеет. */
class Admin(id: Int, name: String, email: String) : User(id, name, email) {

    override val role: String
        get() = "Администратор"

    override val discountPercent: Int
        get() = 0
}
