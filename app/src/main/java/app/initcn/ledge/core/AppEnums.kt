package app.initcn.ledge.core

enum class CurrencyType {

    INR,

    USD
}

enum class TransactionType {

    DEBIT,

    CREDIT
}

enum class DashboardPeriod(

    val label: String
) {

    ALL("All"),

    DAY("1 Day"),

    WEEK("1 Week"),

    MONTH("1 Month"),

    CUSTOM("Custom")
}