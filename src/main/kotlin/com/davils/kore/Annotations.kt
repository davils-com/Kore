package com.davils.kore

/**
 * Marks a class or type as part of the Davils DSL.
 * 
 * This annotation is used to create a DSL marker that helps prevent mixing different DSL contexts,
 * which could lead to confusing or incorrect code. When applied to a class, it ensures that
 * lambdas within the DSL context cannot implicitly access members of outer scopes.
 * 
 * Usage example:
 * ```kotlin
 * @DavilsDsl
 * class MyDslClass {
 *     // DSL methods and properties
 * }
 * ```
 * 
 * Security considerations:
 * - Properly scoped DSLs help prevent accidental access to unrelated contexts
 * - Enhances code readability and maintainability
 * 
 * @sinc 0.1.0
 */
@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
public annotation class DavilsDsl

/**
 * Marks an API as experimental, indicating that it may change in future releases.
 * 
 * This annotation requires an explicit opt-in from the API consumer, with a warning level
 * if used without explicit acknowledgment. Experimental APIs are not guaranteed to maintain
 * backward compatibility and may be significantly modified or removed in future versions.
 * 
 * Usage example:
 * ```kotlin
 * @DavilsExperimental
 * fun experimentalFunction() {
 *     // Implementation
 * }
 * 
 * // To use the function:
 * @OptIn(DavilsExperimental::class)
 * fun consumer() {
 *     experimentalFunction()
 * }
 * ```
 * 
 * Security considerations:
 * - Experimental APIs may not have undergone complete security review
 * - Implementation details and behavior may change, potentially affecting security properties
 * - Users should exercise caution when using experimental APIs in production environments
 * 
 * @sinc 0.1.0
 */
@RequiresOptIn(level = RequiresOptIn.Level.WARNING, message = "This API is experimental and may change in future releases.")
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
public annotation class DavilsExperimental

/**
 * Marks an API as internal, indicating that it should not be used outside of the library.
 * 
 * This annotation requires an explicit opt-in from the API consumer, with an error level
 * if used without explicit acknowledgment. Internal APIs are intended for use only within
 * the library itself and may change without notice. They are not part of the public API
 * contract and should not be relied upon by external code.
 * 
 * Usage example:
 * ```kotlin
 * @DavilsInternal
 * fun internalFunction() {
 *     // Implementation
 * }
 * 
 * // To use the function (only within the library):
 * @OptIn(DavilsInternal::class)
 * fun libraryFunction() {
 *     internalFunction()
 * }
 * ```
 * 
 * Security considerations:
 * - Internal APIs may expose implementation details that could be security-sensitive
 * - Using internal APIs may bypass intended security controls or validations
 * - Changes to internal APIs may occur without notice, potentially breaking dependent code
 * 
 * @sinc 0.1.0
 */
@RequiresOptIn(level = RequiresOptIn.Level.ERROR, message = "This API is internal and should not be used outside of the library.")
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
public annotation class DavilsInternal
