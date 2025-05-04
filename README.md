# Проект: Микросервисы User и Company с Spring Cloud и Docker

## Описание

Данный проект состоит из двух микросервисов (User Service и Company Service), API-шлюза (Gateway), сервера конфигурации (Config Server) и сервиса обнаружения (Eureka). Проекты взаимодействуют между собой по REST через API Gateway.

* **Config Server** — централизованное хранение конфигураций всех сервисов.
* **Eureka Server** — сервис регистрации и обнаружения микросервисов.
* **API Gateway** — единая точка входа для REST-запросов, маршрутизирует запросы к нужным сервисам.
* **User Service** — управление данными пользователей (CRUD).
* **Company Service** — управление данными компаний (CRUD).
* **PostgreSQL** — база данных для сервисов.

Все компоненты запускаются и связываются через Docker Compose.

---

## Структура репозитория

```text
├── config-server/          # Сервер конфигурации
├── eureka-server/          # Сервис обнаружения (Eureka)
├── gateway/                # Spring Cloud Gateway
├── user-service/           # Сервис управления пользователями
├── company-service/        # Сервис управления компаниями
└── docker-compose.yml      # Docker Compose для всего стека
```

---

## Предварительные требования


Перед сборкой убедитесь, что базовый образ JDK загружен:
```bash
docker pull ilgizsamudinov/oracle-jdk:24
```


* Git
* Docker 
* Docker 
* Java 24
* Maven 3.9

---

## Сборка проекта

1. Клонируйте репозиторий:

   ```bash
   git clone https://github.com/ilgiz-samudinov/avb.git
   cd avb
   ```

2. Сборка и упаковка артефактов сервисов вручную:

   Для каждого сервиса необходимо перейти в его директорию и запустить Maven-сборку:

   ```bash
   cd config-server
   mvn clean package -DskipTests
   cd ../eureka-server
   mvn clean package -DskipTests
   cd ../gateway
   mvn clean package -DskipTests
   cd ../user-service
   mvn clean package -DskipTests
   cd ../company-service
   mvn clean package -DskipTests
   cd ..
   ```

3. Сборка Docker-образов:

   ```bash
   docker-compose build
   ```

---

## Запуск сервисов

1. Запустите все контейнеры:

   ```bash
   docker-compose up -d
   ```
2. Убедитесь, что контейнеры работают:

   ```bash
   docker-compose ps
   ```
3. Логи можно просмотреть командой:

   ```bash
   docker-compose logs -f
   ```

---

## Доступные эндпоинты

После успешного запуска все запросы проходят через API Gateway по адресу `http://localhost:8080`.

### User Service

* `GET  /api/users` — получить список всех пользователей (каждый пользователь содержит данные о компании).
* `GET  /api/users/{id}` — получить пользователя по ID.
* `GET  /api/users/by-ids?ids={id1},{id2},...` — получить список пользователей по списку ID.
* `POST /api/users` — создать нового пользователя.
* `PUT  /api/users/{id}` — обновить данные пользователя.
* `DELETE /api/users/{id}` — удалить пользователя.

### Company Service

* `GET  /api/companies` — получить список всех компаний (каждая компания содержит список пользователей).
* `GET  /api/companies/{id}` — получить компанию по ID (с информацией о сотрудниках).
* `GET  /api/companies/by-ids?ids={id1},{id2},...` — получить список компаний по списку ID.
* `POST /api/companies` — создать новую компанию.
* `POST /api/companies/{id}/employees?employeeId={userId}` — добавить сотрудника в компанию.
* `PUT  /api/companies/{id}` — обновить данные компании.
* `DELETE /api/companies/{id}` — удалить компанию.

### Eureka Dashboard

* `http://localhost:8761` — интерфейс Eureka для просмотра зарегистрированных сервисов.

### Config Server

* `http://localhost:8888/{application}/{profile}` — REST API для получения конфигураций.

---

## Остановка и очистка\`

* Остановить контейнеры:

  ```bash
  docker-compose down
  ```
* Удалить все образы и тома (при необходимости):

  ```bash
  docker-compose down --rmi all --volumes
  ```
