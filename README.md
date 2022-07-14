# Project name

CryptoCurrencyWatcher

# Description

REST-сервис просмотра котировок криптовалют. Приложение ежеминутно получает данные со стороннего сервиса (Coinlore) и сохраняет актуальные цены в базу данных. Позволяет узнать текущую цену для валют, с которыми работает сервис. При обновлении данных, в случае если цена с момента начала отслеживания пользователем увеличилась более чем на 1%, приложение записывает в лог информацию о валюте, пользователе и  разнице цен в процентном соотношении. 

## Dependencies

* Spring Boot
* Spring Web
* Spring Data
* H2
* Lombok

## Features

* Просмотр списка доступных криптовалют
* Просмотр актуальной цены для выбранной криптовалюты
* Запись в лог сообщения о изменении цены более чем на 1%

## Plans 

* Валидация
* Тесты
