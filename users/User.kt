package users

/** Абстрактный пользователь: общая часть для всех ролей. */
abstract class User(
    val id: Int,
    val name: String,
    email: String
) : Discountable {

    // Инкапсуляция: читать email можно снаружи, менять только через changeEmail()
    var email: String = email
        private set

    abstract val role: String

    fun changeEmail(newEmail: String): Boolean {
        if (!newEmail.contains("@")) {
            return false
        }
        email = newEmail
        return true
    }

    open fun describe(): String = "$role: $name ($email), скидка $discountPercent%"
}
