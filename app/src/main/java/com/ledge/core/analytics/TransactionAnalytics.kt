package com.ledge.core.analytics

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient

import com.ledge.core.format.CurrencyFormatter
import com.ledge.core.utils.date.DateFormatter

import com.ledge.data.entity.TransactionEntity

fun buildReportHtml(

    transactions: List<TransactionEntity>,

    totalDebit: Long,

    totalCredit: Long,

    balance: Long,

    fromDate: Long?,

    toDate: Long?,

    isDarkTheme: Boolean = false

): String {

    /*
    ---------------------------------------------------
    THEME
    ---------------------------------------------------
    */

    val background =
        if (isDarkTheme) "#141218"
        else "#fffbfe"

    val textPrimary =
        if (isDarkTheme) "#e6e0e9"
        else "#1c1b1f"

    val textSecondary =
        if (isDarkTheme) "#cac4d0"
        else "#49454f"

    val surface =
        if (isDarkTheme) "#211f26"
        else "#f4eff4"

    val surfaceHigh =
        if (isDarkTheme) "#2b2930"
        else "#ece6f0"

    val border =
        if (isDarkTheme) "#49454f"
        else "#cac4d0"

    val rowAlt =
        if (isDarkTheme) "#1d1b20"
        else "#f7f2fa"

    /*
    ---------------------------------------------------
    ROWS
    ---------------------------------------------------
    */

    val rows =

        transactions.joinToString("") { transaction ->

            """
            <tr>

                <td>
                    ${
                DateFormatter.format(
                    transaction.createdAt
                )
            }
                </td>

                <td>
                    ${transaction.category}
                </td>

                <td>
                    ${transaction.mode}
                </td>

                <td>
                    ${transaction.type.name}
                </td>

                <td>
                    ${
                CurrencyFormatter.format(
                    transaction.amount
                )
            }
                </td>

            </tr>
            """.trimIndent()
        }

    /*
    ---------------------------------------------------
    PERIOD
    ---------------------------------------------------
    */

    val periodText =

        if (

            fromDate != null &&
            toDate != null

        ) {

            """
            <p>

                Period:

                ${
                DateFormatter.format(
                    fromDate
                )
            }

                →

                ${
                DateFormatter.format(
                    toDate
                )
            }

            </p>
            """.trimIndent()

        } else {

            ""
        }

    /*
    ---------------------------------------------------
    HTML
    ---------------------------------------------------
    */

    return """

        <html>

        <head>

            <style>

                body {

                    font-family: sans-serif;

                    padding: 24px;

                    background: $background;

                    color: $textPrimary;
                }

                h1 {

                    margin-bottom: 4px;
                }

                .generated {

                    color: $textSecondary;

                    margin-bottom: 8px;
                }

                .summary {

                    margin-top: 24px;

                    margin-bottom: 24px;

                    padding: 16px;

                    background: $surface;

                    border-radius: 12px;
                }

                .summary h3,
                .summary h2 {

                    margin: 8px 0;
                }

                table {

                    width: 100%;

                    border-collapse: collapse;
                }

                th, td {

                    border: 1px solid $border;

                    padding: 12px;

                    text-align: left;
                }

                th {

                    background: $surfaceHigh;
                }

                tr:nth-child(even) {

                    background: $rowAlt;
                }

            </style>

        </head>

        <body>

            <h1>
                Ledge Financial Report
            </h1>

            <p class="generated">

                Generated:

                ${
        DateFormatter.format(
            System.currentTimeMillis()
        )
    }

            </p>

            $periodText

            <div class="summary">

                <h3>

                    Total Credit:

                    ${
        CurrencyFormatter.format(
            totalCredit
        )
    }

                </h3>

                <h3>

                    Total Debit:

                    ${
        CurrencyFormatter.format(
            totalDebit
        )
    }

                </h3>

                <h2>

                    Balance:

                    ${
        CurrencyFormatter.format(
            balance
        )
    }

                </h2>

            </div>

            <table>

                <tr>

                    <th>Date</th>

                    <th>Category</th>

                    <th>Mode</th>

                    <th>Type</th>

                    <th>Amount</th>

                </tr>

                $rows

            </table>

        </body>

        </html>

    """.trimIndent()
}

fun printReport(

    context: Context,

    html: String

) {

    val webView =
        WebView(context)

    webView.webViewClient =

        object : WebViewClient() {

            override fun onPageFinished(

                view: WebView?,

                url: String?

            ) {

                val printManager =

                    context.getSystemService(
                        Context.PRINT_SERVICE
                    ) as PrintManager

                val printAdapter =

                    webView
                        .createPrintDocumentAdapter(
                            "Ledge_Report"
                        )

                printManager.print(

                    "Ledge_Report",

                    printAdapter,

                    PrintAttributes
                        .Builder()
                        .build()
                )
            }
        }

    webView.loadDataWithBaseURL(

        null,

        html,

        "text/html",

        "UTF-8",

        null
    )
}