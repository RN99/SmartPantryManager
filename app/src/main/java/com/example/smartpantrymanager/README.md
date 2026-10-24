# Smart Pantry Manager

## Overview
Smart Pantry Manager is an Android application built in **Java using Android Studio**.  
It helps users reduce food waste by tracking pantry items, monitoring expiry dates, and suggesting recipes that can be prepared strictly with available ingredients.

The app was tested and demonstrated on a **Samsung Galaxy A05**, with a full video demo included in the assignment submission.

---

## Features
- **Pantry Management**: Add, edit, and delete ingredients (name, quantity, unit, expiry date).
- **Expiry Alerts**: Toggle alerts in settings to notify when items are expiring soon.
- **Strict Recipe Matching**: Suggested Recipes only appear if all required ingredients are present in the pantry with sufficient quantity.
- **Recipe Collection**: Add your own recipes and link them to ingredients.
- **Clean UI**: Gradient background, intuitive icons, and logical navigation flow.

---

## System Design
- **Screens**: Pantry List, Add/Edit Ingredient, Suggested Recipes, Recipe Detail, Settings.
- **Database**:
    - Ingredients Table
    - Recipes Table
    - Recipe_Ingredients Table

---

##  Database Choice
SQLite was chosen for this project because:
- It is lightweight and runs locally on-device.
- It supports full CRUD operations (Create, Read, Update, Delete).
- It integrates seamlessly with Android Studio via `SQLiteOpenHelper`.
- It ensures persistence even after the app is closed.

---

## Setup Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/RN99/SmartPantryManager.git
