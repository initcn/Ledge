# Ledge

Ledge is a modern Android expense tracking application built with Kotlin, Jetpack Compose, Room, and Hilt. It offers a comprehensive, offline-first approach to personal financial management with a clean Material 3 UI.

## Key Features

* **Financial Management**: Track income, expenses, debt, and lending effortlessly.
* **Budgeting**: Set and monitor monthly category-specific budgets.
* **Analytics**: Gain insights via a financial dashboard and detailed category breakdowns.
* **Reporting**: Filter transactions by type, category, or time period and export printable financial reports.
* **Search & Paging**: Efficiently browse transaction history with paged loading.
* **Security & Reliability**: Built-in biometric lock support and a robust JSON-based backup/restore system.
* **Multi-Currency**: Supports local (INR) and international (USD) currency standards.
* **Modern UI/UX**: Designed with Material 3 theming for a cohesive experience.

## Tech Stack

* **Language**: Kotlin
* **UI**: Jetpack Compose (Material 3)
* **Architecture**: MVVM with Layered Architecture
* **Database**: Room (with optimized SQL aggregation and indexing)
* **Dependency Injection**: Hilt
* **Data Persistence**: DataStore Preferences
* **Asynchronous Operations**: Coroutines and Flow
* **Data Loading**: Paging 3

## Architecture

Ledge follows a clean, modular layered architecture:

* `core/`: Shared utilities, formatters, enums, and extension functions.
* `data/`: Room database (entities, DAOs), repository implementations, and Backup manager.
* `domain/`: Business logic, use cases, financial rules, and validation logic.
* `ui/`: Jetpack Compose screens, reusable UI components, and theme definitions.
* `di/`: Hilt modules for dependency injection.

## Financial Tracking & Analytics

### Supported Categories

* **Income**: Primary/Secondary income, investment returns, and loan repayments received.
* **Expenses**: Utilities, groceries, transport, health, shopping, savings, debt repayment, EMIs, and money lent.

### Dashboard Capabilities

The analytics dashboard provides:

* Total income vs. total expenses.
* Net balance tracking (with optional debt/lending inclusion).
* Outstanding debt and lending recovery monitoring.
* Visual breakdowns of category-wise spending.

## Build Requirements

* **IDE**: Android Studio
* **Language**: Kotlin
* **System**: Android SDK
* **Build System**: Gradle

### Getting Started

1. **Clone the repository**:
```bash
git clone <repository-url>

```

2. **Build the project**:
```bash
./gradlew build

```

3. **Run the application**:
```bash
./gradlew installDebug
```



## License

This project is intended for educational and personal use.
