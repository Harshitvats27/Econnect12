# E-Connect12 🎓

A comprehensive school management app solution built to make school life easier for students, parents, and schools — bringing attendance, homework, notices, curriculum, holidays, and marks together in one place.

> **Status:** ✅ Demo available — see the Demo Video section below.

---

## 🎯 Purpose of the Project

E-Connect12 is an app created to make school life easier for students, parents, and schools. It brings everything together in one place — attendance, homework, notices, curriculum, holidays, and marks. Parents can stay up-to-date with their child's progress in real-time, and students can easily access all their school activities. For schools, it helps reduce the administrative load and keeps things running smoothly. The goal of E-Connect12 is to improve communication, make information more accessible, and ensure that everyone stays connected, making the entire school experience simpler and more organized.

---

## 👥 Target Audience

* **Students:** Provides easy access to academic records and school-related activities.
* **Parents:** Enables real-time tracking of their child's progress, attendance, and other key school activities.
* **Schools:** Provides schools full access to their work, made safer with online record saving.
* **Efficient Management:** Simplifies administrative tasks and tracks student progress in real-time.
* **Enhanced Communication:** Strengthens communication between schools, parents, and students, ensuring everyone stays informed.

---

## 🎯 Objective

The goal is to create an easy-to-use app that keeps parents and students connected to everything happening at school. It ensures that important updates, like attendance or homework, are never missed. E-Connect12 aims to make school life simpler and more organized, while helping everyone stay involved.

---

## 🏗️ Tech Stack

* **Frontend:** Android (Java)
* **Backend:** Java, Spring Boot (Full Spring Boot architecture — REST APIs, Service/Repository layers, Role-Based Access Control)
* **Database:** PostgreSQL
* **Architecture:** Role-based student management system with distinct access levels for Students, Parents, and School Admins

---

## ✨ Key Features

* **Role-Based Access:** Separate dashboards and permissions for Students, Parents, and School Administrators.
* **Real-Time Progress Tracking:** Parents can track attendance, marks, and academic progress as it happens.
* **Centralized School Data:** Attendance, homework, notices, curriculum, and holidays managed from a single system.
* **Secure Record Keeping:** Online-saved records reduce paperwork and improve safety of school data.
* **Simplified Administration:** Reduces manual administrative load for schools with structured, automated workflows.

---

## ⚙️ Setup & Installation

### Prerequisites
* Java JDK (11 or higher)
* Spring Boot (via Maven/Gradle)
* PostgreSQL database instance
* Android Studio (for the Android client)

### Steps to Run

**Backend (Spring Boot):**
1. **Clone the repository:**
```bash
   git clone https://github.com/your-username/econnect12.git
```
2. **Configure the database:**
   Update `application.properties` (or `application.yml`) with your PostgreSQL connection details:
```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/econnect12
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
```
3. **Build and run:**
```bash
   mvn clean install
   mvn spring-boot:run
```

**Android App:**
1. Open the Android project in Android Studio.
2. Update the base API URL to point to your running Spring Boot backend.
3. Build and run the app on an emulator or physical device.

---

## 🛡️ Security & Access Control

E-Connect12 uses Role-Based Access Control (RBAC) at the Spring Boot backend level, ensuring Students, Parents, and School Admins only access the data and features relevant to their role — keeping academic records safe and organized.

---

## 🎥 Demo Video

Watch the app in action here: **[Demo Video - Google Drive](https://drive.google.com/file/d/1F4YEDTiGpKjsnZsRpXFA7Px7qtuMu0Ch/view)**

---
## 🎥 Backend Repository

Check the repository here: **[Econnect12appbackend](https://github.com/Harshitvats27/Econnect12appbackend)**

---

**Developed & Maintained by:** Harshit Vats
**Project:** E-Connect12
