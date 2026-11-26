# Список покупок (Kotlin)

Android-приложение для ведения списка покупок с локальным хранением данных в SQLite.

## Требования
- Android Studio (версия с поддержкой Android Gradle Plugin 8.7.3)
- JDK 17
- Android SDK 35
- Эмулятор или устройство Android 6.0 (API 23) и выше

## Запуск
1. Распакуйте архив.
2. В Android Studio выберите **Open** и укажите папку `ShoppingListKotlin`.
3. Дождитесь синхронизации Gradle.
4. Создайте/выберите эмулятор Android API 23 или выше.
5. Нажмите Run.

## Экраны
- `MainActivity` — список товаров.
- `ProductDetailsActivity` — подробности и удаление.
- `AddEditProductActivity` — добавление и редактирование.

## Архитектура
- `Product` — модель товара.
- `DatabaseHelper` — создание SQLite-базы и таблицы.
- `ProductRepository` — операции CRUD.
- Разметка экранов находится в `app/src/main/res/layout`.

## Структура SQLite
База: `shopping_list.db`, таблица: `products`.

Поля: `id`, `name`, `quantity`, `price`, `is_purchased`.

## Примечание
Проект подготовлен как исходный Android Studio-проект. Перед сдачей откройте его в Android Studio, выполните Gradle Sync и запустите на эмуляторе/устройстве. Фактическая сборка в Android Studio в рамках подготовки архива не выполнялась.

## Сборка без Android Studio

В архив добавлен GitHub Actions workflow: `.github/workflows/android.yml`. Инструкция по загрузке проекта на GitHub и скачиванию APK находится в `README_GITHUB.md`.

**Важно:** APK нельзя установить на iPhone. Успешная сборка подтверждает компиляцию проекта, но для проверки интерфейса и SQLite нужен Android-эмулятор или Android-устройство.
