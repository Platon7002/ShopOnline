package results

sealed class OperationResult {
    data class Success(val message: String) : OperationResult()
    data class Failure(val reason: String) : OperationResult()
    data class NotFound(val orderId: Int) : OperationResult()
}
