# Архитектура проекта

## Чистая архитектура

Проект реализован с использованием принципов чистой архитектуры, разделенной на три основных слоя:

### 1. Domain Layer (Доменный слой)
**Расположение:** `app/src/main/java/com/example/hw_3/domain/`

- **Entities** (`domain/entity/`): Бизнес-сущности приложения
  - `ClassEntity` - класс персонажа
  - `SpellEntity` - заклинание
  - `MonsterEntity` - монстр
  - `ReferenceEntity` - справочная информация

- **Repository Interfaces** (`domain/repository/`):
  - `DnDRepository` - интерфейс репозитория для работы с данными D&D

- **Use Cases** (`domain/usecase/`):
  - `GetClassesUseCase` - получение списка классов
  - `GetClassDetailsUseCase` - получение деталей класса
  - `GetSpellsUseCase` - получение списка заклинаний (с фильтрацией)
  - `GetSpellDetailsUseCase` - получение деталей заклинания
  - `GetMonstersUseCase` - получение списка монстров (с фильтрацией)
  - `GetMonsterDetailsUseCase` - получение деталей монстра

### 2. Data Layer (Слой данных)
**Расположение:** `app/src/main/java/com/example/hw_3/data/`

- **Repository Implementation** (`data/repository/`):
  - `DnDRepositoryImpl` - реализация репозитория, работающая с API

- **API Service** (`api/`):
  - `DnDApiService` - интерфейс Retrofit для работы с D&D 5e API
  - `RetrofitClient` - клиент Retrofit
  - Поддержка query параметров:
    - `level` и `school` для фильтрации заклинаний
    - `challenge_rating` для фильтрации монстров

- **Data Models (DTOs)** (`data/`):
  - `DnDClass`, `DnDSpell`, `DnDMonster` - модели данных из API
  - `ApiReference`, `ApiListResponse` - вспомогательные модели

- **Mappers** (`data/mapper/`):
  - `DnDMapper` - преобразование DTO → Entity
  - `EntityToDataMapper` - преобразование Entity → DTO (для обратной совместимости с UI)

### 3. Presentation Layer (Слой представления)
**Расположение:** `app/src/main/java/com/example/hw_3/viewmodel/` и UI компоненты

- **ViewModels**:
  - `DnDClassViewModel` - управление состоянием для классов
  - `DnDSpellViewModel` - управление состоянием для заклинаний
  - `DnDMonsterViewModel` - управление состоянием для монстров

- **Dependency Injection** (`di/`):
  - `DnDModule` - модуль для предоставления зависимостей (use cases, repository)

## Особенности реализации

### Асинхронная загрузка данных
- Все сетевые запросы выполняются асинхронно через корутины
- Используется `viewModelScope` для управления жизненным циклом корутин
- Обработка выполняется на фоновом потоке (IO dispatcher)

### Обработка ошибок
- Использование `Result<T>` для обработки успешных и неуспешных результатов
- Детальные сообщения об ошибках для пользователя
- Разделение ошибок сети и ошибок API

### Состояние загрузки
- `StateFlow` для управления состоянием загрузки (`isLoading`)
- Отображение индикаторов загрузки в UI
- Обработка состояний: загрузка, успех, ошибка

### Query параметры
Реализованы настраиваемые запросы с параметрами:
- **Заклинания**: фильтрация по уровню (`level`) и школе магии (`school`)
- **Монстры**: фильтрация по рейтингу сложности (`challenge_rating`)

Пример использования:
```kotlin
// Получить заклинания 1-го уровня школы "evocation"
viewModel.fetchSpells(level = 1, school = "evocation")

// Получить монстров с рейтингом сложности 5.0
viewModel.fetchMonsters(challengeRating = 5.0)
```

## Поток данных

1. **UI** → вызывает метод ViewModel
2. **ViewModel** → вызывает UseCase
3. **UseCase** → вызывает Repository
4. **Repository** → вызывает API Service
5. **API Service** → выполняет HTTP запрос
6. **Ответ** → преобразуется через мапперы (DTO → Entity → DTO)
7. **Результат** → возвращается в ViewModel через StateFlow
8. **UI** → обновляется на основе состояния

## Используемые библиотеки

- **Retrofit 2.9.0** - для сетевых запросов
- **Gson** - для сериализации/десериализации JSON
- **OkHttp 4.11.0** - HTTP клиент
- **Kotlin Coroutines** - для асинхронных операций
- **StateFlow** - для реактивного управления состоянием

