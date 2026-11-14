# RentTrack - Equipment Rental & Tracking System

A website for a shop that rents out cameras, laptops, projectors and tools.
Users sign up, log in and book equipment. The admin manages the equipment, takes returns,
calculates late fees and records damage.

**Built with:** Java 17, Spring Boot 3, Spring MVC, JSP + JSTL, Spring Data MongoDB, BCrypt, plain CSS.

## How to run

You need:

1. **JDK 17** or newer
2. **MongoDB** running on your computer (MongoDB Community Server, default port 27017)

Start MongoDB first, then in this folder run:

```
mvnw.cmd spring-boot:run        (Mac/Linux:  ./mvnw spring-boot:run)
```

Open http://localhost:8080

In IntelliJ / Eclipse / VS Code you can also open the folder as a Maven project and run `RentTrackApplication.java`.

### Demo accounts (created automatically on the first run)

| Role  | Email               | Password |
|-------|---------------------|----------|
| Admin | admin@renttrack.com | admin123 |
| User  | user@renttrack.com  | user123  |

New users can create their own account from the Sign up page. Admin accounts cannot be created from the website.

The first run also creates one booking of the demo user (Bosch Drill Machine) that is already 2 days late,
so the late fee can be shown in the demo: Admin > Rentals > Return.

### Database

The connection is set in `src/main/resources/application.properties`:

```
spring.data.mongodb.uri=mongodb://localhost:27017/renttrackdb
```

To use MongoDB Atlas (cloud) instead, replace that line with your Atlas connection string.
The database and its collections (`users`, `equipment`, `rentals`, `damage_reports`) are created automatically
and can be seen in MongoDB Compass. Drop the database to start again with the sample data.

## Who can do what

| Visitor (not logged in) | User | Admin |
|---|---|---|
| Browse and search equipment | Everything a visitor can | Dashboard with counts and overdue list |
| Sign up / log in | Book an available item | Add equipment and update its details |
| | See own rentals and bills | See all rentals, take returns (late fee + damage) |
| | | Damage reports, mark repaired |
| | | See registered users |

## Features from the problem statement

| Feature | Where to see it | Code |
|---|---|---|
| Equipment listing | Home page (search, category filter), Admin > Equipment | `HomeController`, `AdminEquipmentController`, `EquipmentService` |
| Rental booking | "Book now" on an item (login needed) | `BookingController`, `RentalService.bookRental()` |
| Availability tracking | Status label on every item, Admin dashboard counts | `Equipment.status`, `AdminController` |
| Late return penalty | Admin > Rentals > Return, user's My Rentals | `Rental.getLateDays()`, `RentalService.calculateLateFee()` |
| Damage reporting | Tick "damaged" while returning, Admin > Damage Reports | `DamageReport`, `DamageService` |

## MVC structure

```
src/main/java/com/renttrack
    RentTrackApplication.java   starts the server

    model/                      M - the data (each class = one MongoDB collection)
        User.java                   account with role ADMIN or USER
        Equipment.java              one item
        Rental.java                 one booking
        DamageReport.java           one damage report

    controller/                 C - receives the request, calls a service, picks the JSP
        HomeController.java             /                       public
        AuthController.java             /login /register /logout
        BookingController.java          /book/{id} /my-rentals  user
        AdminController.java            /admin /admin/users     admin
        AdminEquipmentController.java   /admin/equipment...     admin
        AdminRentalController.java      /admin/rentals... /admin/damage...   admin

    service/                    business rules (used by the controllers)
        UserService.java            sign up, login, password hashing
        EquipmentService.java       search, add, update, availability counts
        RentalService.java          booking, return, late fee
        DamageService.java          damage reports, mark repaired

    repository/                 talks to MongoDB (used by the services)

    config/
        LoginInterceptor.java       checks login and role before protected pages
        WebConfig.java              says which URLs are protected
        SampleData.java             creates demo accounts, 8 items and one late rental on first run

src/main/webapp/WEB-INF/views   V - the JSP pages
    common/     userHeader.jspf, adminHeader.jspf, footer.jspf
    home.jsp, login.jsp, register.jsp, book.jsp, myRentals.jsp
    error.jsp                       shown when a page is not found or something fails
    admin/      dashboard.jsp, equipment.jsp, equipmentEdit.jsp, rentals.jsp, return.jsp, damage.jsp, users.jsp

src/main/resources
    application.properties          port, JSP folder, MongoDB connection
    static/css/style.css            design
```

## How a request flows

```
Browser -> (LoginInterceptor) -> Controller -> Service -> Repository -> MongoDB
                                     |
                                     +--> puts data in the Model -> JSP (View) -> Browser
```

## How login works

1. Sign up: `UserService.register()` checks the details and saves the user. The password is
   hashed with **BCrypt**, so the real password is never stored.
2. Login: `UserService.login()` finds the user by email and compares the typed password with the hash.
   If it matches, `AuthController` puts the user object in the **session** as `loggedUser`.
3. Every request to `/admin/**`, `/book/**` or `/my-rentals` first passes through `LoginInterceptor`:
   - no `loggedUser` in the session -> redirect to `/login`
   - `/admin` page but role is not `ADMIN` -> redirect to home
4. Logout: the session is destroyed.

## The rules

- **Rent** = number of days between start date and due date x rent per day (minimum 1 day).
- **Late days** = days between due date and actual return date (0 if returned on time).
- **Late fee** = late days x rent per day x 1.5 (the price on the booking day is used, even if the admin changes it later)
- A booking cannot start in the past and a return cannot be dated in the future.
- **Total bill** = rent + late fee + damage charge

Example: camera at 800/day, booked for 3 days, returned 2 days late with 500 damage
-> rent 2400 + late fee (2 x 800 x 1.5 = 2400) + damage 500 = 5300.

## Item status

- `AVAILABLE` -> can be booked
- `RENTED` -> set when a user books it
- `REPAIR` -> set when the admin records damage on return; back to `AVAILABLE` after "Mark repaired"

## How MongoDB is used

- `@Document` on a model class means its objects are saved as documents in a collection.
- `@Id` is the `_id` field, MongoDB generates it.
- `@DBRef` in `Rental` and `DamageReport` stores a link to the equipment document instead of copying it.
- The repositories extend `MongoRepository`, which already has `save()`, `findAll()`, `findById()` and `count()`.
  Methods like `findByEmail()` or `findByStatus()` are turned into MongoDB queries by Spring from the method name.
