# FolksDev & Kod Gemisi SpringBoot Bootcamp

Bootcamp süresince geliştirilen spring boot projesine ait repodur.

## Gereksinimler

- Maven
- Docker
- Java 11
- PostgreSQL

## Kullanılan Teknolojiler

- Java 11
- Kotlin 1.5.31
- Spring Boot
- Spring Data JPA
- PostgreSQL
- H2 In memory database
- Flyway
- Hamcrest
- OpenAPI documentation
- Docker
- Docker compose
- JUnit 5

## Projenin Çalıştırılması

Proje 2 şekilde çalıştırılabilir.

### Maven

1. Projeyi cihazınıza kopyalayın: `git clone https://github.com/aoserdardemirkol/Spring-API-Blog.git`
2. Proje ana dizinine gidin: `cd Spring-API-Blog`
3. `mvn clean install` ile çalıştırılabilir jar dosyasını oluşturun.
4. `mvn spring-boot:run` ile projeyi çalıştırın.

> `http://localhost:8080` adresinde API,
>
> `http://localhost:5432` adresinde ise PostgreSQL çalışacaktır 


### Docker Compose

1. Projeyi cihazınıza kopyalayın: `git clone https://github.com/aoserdardemirkol/Spring-API-Blog.git`
2. Proje ana dizinine gidin: `cd Spring-API-Blog`
3. `docker build -t blog .` komutu ile docker image oluşturun
4. `docker-compose up` komutu ile containerları çalıştırın.

> `http://localhost:9090` adresinde API,
>
> `http://localhost:5433` adresinde ise PostgreSQL çalışacaktır

## Swagger UI

Swagger UI verilen url de çalışacaktır.

http://localhost:${PORT}/swagger-ui.html

!Port değeri genelde 8080 dir. ancak uygulama hangi portda çalışıyor ise ${PORT} değeri yerine uygulamanın çalıştığı portu yazınız.

## HTTP İstek Yapısı

Bu uygulamada 4 adet API vardır.

JUnit test coverage 100% ve Integration testleri yapılmıştır.

[![](./img/JUnit%20Test.png "FolksDev & Kod Gemisi")](https://github.com/aoserdardemirkol/)

[![](./img/Integration%20Test.png "FolksDev & Kod Gemisi")](https://github.com/aoserdardemirkol/)

|      Controller       | Metot  |     Adres     |                  Açıklama                           |        
| :-------------------: | :----: | :-----------: | :-------------------------------------------------: |
|  **UsersController**  |  GET   | /v1/users     | Bütün kullanıcıları listeler.                       |
|                       |  GET   | /v1/users/0   | id değeri 0 olan kullanıcıyı getirir.               |
|                       |  POST  | /v1/users     | Kullanıcı oluşturur.                                |
|                       |  PUT   | /v1/users/0   | id değeri 0 olan kullanıcıyı günceller.             |
|                       | DELETE | /v1/users/0   | id değerş 0 olan kullanıcıyı siler.                 |
| **TagController**     |  GET   | /v1/tag       | Bütün Tag leri listeler.                            |
|                       |  GET   | /v1/tag/0     | id değeri 0 olan Tag i getirir.                     |
|                       |  POST  | /v1/tag       | Tag oluşturur.                                      |
|                       |  PUT   | /v1/tag/0     | id değeri 0 olan Tag günceller.                     |
|                       | DELETE | /v1/tag/0     | id değeri 0 olan Tag siler.                         |
|  **EntryController**  |  GET   | /v1/entry     | Bütün gönderileri listeler.                         |
|                       |  GET   | /v1/entry/0   | id değeri 0 olan gönderiyi getirir.                 |
|                       |  POST  | /v1/entry/0   | id değeri 0 olan kullanıcıya ait gönderi oluşturur. |
|                       |  PUT   | /v1/entry/0   | id değeri 0 olan gönderiyi günceller.               |
|                       | DELETE | /v1/entry/0   | id değeri 0 olan gönderiyi siler.                   |
| **CommentController** |  GET   | /v1/comment   | Bütün yorumları listeler.                           |
|                       |  GET   | /v1/comment/0 | id değeri 0 olan yorumu getirir.                    |
|                       |  POST  | /v1/comment   | Yorum oluşturur                                     |
|                       |  PUT   | /v1/comment/0 | id değeri 0 olan yorumu günceller.                  |
|                       | DELETE | /v1/comment/0 | id değeri 0 olan yorumu siler.                      |

Bu repo [Ali Osman Serdar Demirkol](https://github.com/aoserdardemirkol) tarafından bootcamp sürecinde
oluşturulmuştur.
