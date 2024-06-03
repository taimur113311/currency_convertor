
# Currency Converter


A modern and efficient currency converter application built using Jetpack Compose and Kotlin. The application allows users to convert between different currencies and view historical conversion data. It fetches real-time currency conversion rates from a remote API and displays popular currency conversions with a visually appealing UI.


## Features

- Real-time Currency Conversion: Convert between different currencies using the latest exchange rates.
- Historical Data: View historical conversion data for the last three days.
- Popular Currencies Conversion: Displays conversions from the base currency to popular currencies like USD, EUR, GBP, JPY, and AUD.
- Responsive UI: Utilizes Jetpack Compose for a modern and responsive user interface.

## Demo

* Video: https://github.com/taimur113311/currency_convertor/assets/44231625/28c6a402-c10b-4a87-92f0-ab72e66ff89c

<div align="center">
  <img src="https://github.com/taimur113311/currency_convertor/assets/44231625/c7266773-6e59-4d2f-a636-e378b4ac5ea4" width="230px" />  <img src="https://github.com/taimur113311/currency_convertor/assets/44231625/212f6bee-7bd4-4ad6-8a3b-8e543fcf0027" width="230px" />  
</div>
<div align="center">
   <img src="https://github.com/taimur113311/currency_convertor/assets/44231625/8db7c84e-61be-4c5b-9e2f-7acd60373c47" width="230px" />
<img src="https://github.com/taimur113311/currency_convertor/assets/44231625/b2c21941-58d9-4c10-9453-b9586276d8ed" width="230px" /> <br>
  
</div>

## Installation


Open the project: Open the project in Android Studio.

Build the project: Sync and build the project to download all dependencies.

Run the application: Run the app on an emulator or a physical device.
Clone the repository:

```bash
git clone https://github.com/yourusername/currency-converter.git
cd currency-converter
```
 - #### Note: Please change the endpoints from 'CurrencyConversionApi' to mock endpoints as we have a limited access of free version.
    
## Architecture
The application is built using the Clean Architecture principles and follows the Model-View-ViewModel (MVVM) architectural pattern. This ensures a scalable, maintainable, and testable codebase.

### Layers
  #### Presentation Layer: Contains UI components and ViewModels.
  - ViewModel: Exposes data to the UI and handles the presentation logic.
  - Compose UI: Jetpack Compose for building responsive and modern UIs.
#### Domain Layer: Contains business logic.
 - Use Cases: Encapsulates business logic and executes specific tasks.
- Models: Data models that represent the entities in the domain layer.
#### Data Layer: Handles data operations.
- Repositories: Provides data to the domain layer from various data sources.
- Remote Data Source: Fetches data from the network (e.g., REST APIs using Retrofit).
- Models: Data models that represent the models in the remote layer.
## Usage
- Main Screen
  - The main screen allows the user to select currencies for conversion and view the conversion rate.
- Conversion History Screen
  - The conversion history screen shows the conversion rates of the selected currencies over the last three days and popular currencies conversion.
 - Navigation
   - The application uses Jetpack Navigation for handling navigation between different screens.
```Kotlin
sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object HistoryScreen : Screen("history_screen/{baseCurrency}/{fromCurrency}/{toCurrency}") {
        fun createRoute(baseCurrency: String, fromCurrency: String, toCurrency: String) =
            "history_screen/$baseCurrency/$fromCurrency/$toCurrency"
    }
}
```



## Technology Stack & Open-source libraries

- Minimum SDK level 21
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Data objects that notify views when the underlying database changes.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes.
- [Dependency Injection](https://developer.android.com/training/dependency-injection)
  - [Hilt](https://dagger.dev/hilt) - Easier way to incorporate Dagger DI into Android apps.
- [Jetpack Compose](https://developer.android.com/develop/ui/compose)
- [Compose Navigation](https://developer.android.com/develop/ui/compose/navigation)

- [Retrofit2 & OkHttp3](https://github.com/square/retrofit) - Construct the REST APIs.





## Contributing
Contributions are welcome! 

Please open an issue or submit a pull request for any improvements or bug fixes.

