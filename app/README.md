# CryptoPulse Pro - App Module

This folder contains the main Android application module for the CryptoPulse Pro project.

The app module includes the source code, layouts, resources, manifest file, Gradle configuration, and all Android-specific files needed to build and run the application.

---

## Module Location

```text
CryptoPulse/app/
```

---

## Main Source Code Location

```text
app/src/main/java/com/unyt/cryptopulse/
```

The Java source code is organized into the following packages:

```text
activities
adapters
api
database
fragments
models
views
```

---

## Activities Package

Location:

```text
app/src/main/java/com/unyt/cryptopulse/activities/
```

This package contains the full-screen activities used in the app.

### LoginActivity.java

The first screen of the app. It allows the user to enter an email and password. The entered credentials are checked against saved users in the local Room database.

### SignupActivity.java

A sign-up screen where the user can enter a name, email, password, and password confirmation. If the input is valid, the user account is saved in the local Room database.

### MainActivity.java

The main container activity. It checks the saved login session, holds the bottom navigation bar, and loads the app fragments.

### CoinDetailsActivity.java

The details screen for a selected cryptocurrency. It displays coin information, allows saving the coin to favorites, sharing the coin, opening CoinGecko in the browser, and returning to the previous screen.

---

## Fragments Package

Location:

```text
app/src/main/java/com/unyt/cryptopulse/fragments/
```

This package contains the fragments displayed inside `MainActivity`.

### HomeFragment.java

Displays live cryptocurrency market data from the CoinGecko API. It includes a search bar, filters, currency selection, a chart, and a RecyclerView list.

### FavoritesFragment.java

Displays the cryptocurrencies saved in the local Room database. Each favorite item includes a Remove from Favorites button.

### ProfileFragment.java

Displays logged-in user information, including name and email. It also includes change password functionality, an email contact button, project feature information, and logout functionality.

---

## Adapters Package

Location:

```text
app/src/main/java/com/unyt/cryptopulse/adapters/
```

This package contains RecyclerView adapters.

### CoinAdapter.java

Displays live cryptocurrency data inside the Home screen RecyclerView.

### FavoriteAdapter.java

Displays saved favorite coins inside the Favorites screen RecyclerView and handles removing coins from favorites.

---

## API Package

Location:

```text
app/src/main/java/com/unyt/cryptopulse/api/
```

This package contains the network communication code.

### CoinGeckoApi.java

Defines the Retrofit API endpoint used to request cryptocurrency market data.

### RetrofitClient.java

Creates and manages the Retrofit instance used to communicate with the CoinGecko API.

---

## Database Package

Location:

```text
app/src/main/java/com/unyt/cryptopulse/database/
```

This package contains Room database files.

### User.java

Defines the database entity for saved user accounts.

### UserDao.java

Defines database operations for users, including insert, find by email, login check, email existence check, and password update.

### FavoriteCoin.java

Defines the database entity for a saved favorite coin.

### FavoriteDao.java

Defines the database operations for favorite coins, such as inserting, deleting, retrieving, and checking saved coins.

### AppDatabase.java

Creates and manages the Room database instance. The database includes both users and favorite coins.

---

## Models Package

Location:

```text
app/src/main/java/com/unyt/cryptopulse/models/
```

### Coin.java

Represents one cryptocurrency object received from the CoinGecko API. It includes fields such as ID, symbol, name, image, current price, market cap rank, market cap, and 24-hour price change.

---

## Views Package

Location:

```text
app/src/main/java/com/unyt/cryptopulse/views/
```

### MiniChartView.java

A custom view that draws a small chart using Canvas, Paint, and Path. It is used to improve the visual appearance of the Home and Coin Details screens.

---

## Layout Resources

Location:

```text
app/src/main/res/layout/
```

The main layout files are:

```text
activity_login.xml
activity_signup.xml
activity_main.xml
activity_coin_details.xml
fragment_home.xml
fragment_favorites.xml
fragment_profile.xml
item_coin.xml
item_favorite_coin.xml
```

### activity_login.xml

Defines the login screen layout.

### activity_signup.xml

Defines the sign-up screen layout with password confirmation.

### activity_main.xml

Defines the main activity layout with the fragment container and bottom navigation.

### activity_coin_details.xml

Defines the details screen for a selected cryptocurrency.

### fragment_home.xml

Defines the Home screen UI, including market overview, search bar, filters, SeekBar, radio buttons, checkbox, chart, and RecyclerView.

### fragment_favorites.xml

Defines the Favorites screen UI.

### fragment_profile.xml

Defines the Profile screen UI, including logged-in user information, change password inputs, email contact, and logout button.

### item_coin.xml

Defines the reusable layout for one cryptocurrency card in the live market RecyclerView.

### item_favorite_coin.xml

Defines the reusable layout for one saved favorite cryptocurrency card, including the Remove from Favorites button.

---

## Drawable Resources

Location:

```text
app/src/main/res/drawable/
```

The drawable resources include backgrounds and UI styling files such as:

```text
bg_gradient.xml
button_bg.xml
card_bg.xml
card_glow.xml
input_bg.xml
```

These files define gradient backgrounds, rounded cards, input field backgrounds, and button styles.

---

## Menu Resources

Location:

```text
app/src/main/res/menu/
```

The menu resource used by the app is:

```text
bottom_menu.xml
```

This file defines the bottom navigation items:

* Home
* Favorites
* Profile

---

## Values Resources

Location:

```text
app/src/main/res/values/
```

Important values files include:

```text
colors.xml
styles.xml
```

### colors.xml

Defines the colors used in the app, including dark background colors, cyan, purple, green, red, white, and text colors.

### styles.xml

Defines the main application theme:

```text
Theme.CryptoPulse
```

---

## Android Manifest

Location:

```text
app/src/main/AndroidManifest.xml
```

The manifest defines:

* Internet permission
* App theme
* App label
* App icon
* Activities
* Launcher activity

The app requires internet permission because it loads live cryptocurrency data from an external API.

---

## Gradle File

Location:

```text
app/build.gradle
```

This file contains the app-level Gradle configuration and dependencies.

Main dependencies include:

* AppCompat
* Material Components
* RecyclerView
* Room
* Retrofit
* Gson Converter
* Glide

---

## Main Features in This Module

This module implements:

* Local account creation
* Login with saved user credentials
* Password confirmation during sign-up
* Password change from Profile
* Logged-in user name and email display
* Logout
* Multi-activity structure
* Bottom navigation
* Fragments
* RecyclerView lists
* Search bar for coins
* Top 10 filter
* 24-hour movement SeekBar filter
* USD/EUR radio button selection
* Retrofit API communication
* CoinGecko API data loading
* Room local database
* Favorites saving system
* Remove from favorites
* Implicit intents
* Custom chart view
* Dark-mode UI design
* App icon

---

## How the App Module Works

The application starts from `LoginActivity`.

A new user can create an account in `SignupActivity`. The account is saved locally using Room database.

After login or sign-up, the user is taken to `MainActivity`.

`MainActivity` loads three fragments through bottom navigation:

```text
HomeFragment
FavoritesFragment
ProfileFragment
```

The Home screen loads live cryptocurrency data from the CoinGecko API using Retrofit. The user can search coins by name or symbol, switch between USD and EUR, filter by Top 10, and filter by 24-hour movement using a SeekBar.

When the user taps a coin, `CoinDetailsActivity` opens and displays more information about the selected cryptocurrency.

The user can save a coin to favorites, and the saved coin is stored locally using Room database.

The Favorites screen reads saved coins from Room and displays them in a RecyclerView. The user can also remove saved coins from favorites.

The Profile screen displays the logged-in user’s name and email, allows the user to change their password, opens the email app for contact, and allows logout.

---

## Notes

This module contains the main implementation of the CryptoPulse Pro Android application.

The app uses a local Room database for user authentication. It does not connect to an online authentication server.

For educational simplicity, passwords are stored locally as plain text. In a real production app, passwords should be securely hashed or handled by a secure backend/authentication provider.

The chart view is a visual demo and does not show real historical market data.

Live cryptocurrency information depends on internet access and the availability of the CoinGecko API.
