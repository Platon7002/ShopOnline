package results

/**
 * Результат любой операции (sealed class).
 * Возможных вариантов ровно три, поэтому when по нему не требует else.
 */
sealed class OperationResult {
    data class Success(val message: String) : OperationResult()
    data class Failure(val reason: String) : OperationResult()
    data class NotFound(val orderId: Int) : OperationResult()
}
