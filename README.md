Тестовое задание
Spring boot + PostgreSQL

Сборка и запуск
1. Соберите ваше Spring Boot приложение с помощью Maven чтобы получить .jar файл.
2. Постройте Docker-образ для приложения:
   docker build -t myapp:latest .
3. Поменяйте переменные окружения в docker-compose.yaml
4. Запустите docker-compose:
   docker-compose up
