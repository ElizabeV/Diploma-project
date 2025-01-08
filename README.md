# Процедура запуска автотестов
Для запуска тестов и SUT необходимо иметь на компьютере Docker Desktop, IDEA IDE.
Для начала склонировать репозиторий командой `git clone https://github.com/ElizabeV/Diploma-project.git`.
Открыть Docker на компьютере. Затем в консоли запустить докер образ командой `docker-compose up`.
По окончании процедуры в новой вкладке терминала запустить приложение командой `java -jar aqa-shop.jar`.
Приложение откроется по адресу `http://localhost:8080/`.
Автотесты запускаются командой в консоли Gradle `gradle clean test`.
Для генерации отчетной документации Allure выполнить в консоли Gradle `gradle allureServe`.
Приложение закрывается в консоли сочетанием кнопок Ctrl+C, а докер образ командой `docker-compose down`.
