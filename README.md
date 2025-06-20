# Процедура запуска автотестов
Для запуска тестов и SUT необходимо иметь на компьютере Docker Desktop, IDEA IDE.
Для начала склонировать репозиторий командой `git clone https://github.com/ElizabeV/Diploma-project.git`.

1) Открыть Docker на компьютере. 
2) Запустить IDEA IDE на компьютере.
3) Затем в консоли IDEA запустить докер образ командой `docker-compose up` и дождаться текста об успешном поднятии контейнеров (ready for connection).
4) По окончании процедуры в новой вкладке терминала запустить приложение с БД командой `java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar ./aqa-shop.jar` (для MySQL). Для подключения к
PostgreSQL нужно запустить командой`java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/db" -jar ./aqa-shop.jar``.
Если не работает верхний регистр в IDEA, то в терминале локальной машины сменить директорию на проект (например,`cd c:\repo\Diploma-VE\`)
и затем запустить приложение с БД той же командой.
5) Приложение откроется по адресу `http://localhost:8080/`.
6) Автотесты запускаются командой в консоли Gradle `gradle clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"` для MySQL или соответствующий адрес PostgreSQL указать в значении db.url.
Для генерации отчетной документации Allure выполнить в консоли Gradle `gradle allureServe`.
7) Приложение закрывается в консоли сочетанием кнопок Ctrl+C, а докер образ командой `docker-compose down`.
