# Medical Records System for College Nursing Room

## Overview
The Medical Records System is a Java-based application designed for managing and recording medical treatments provided in a college nursing room. It allows authorized users—Admins, Nurses, and Students—to log in and access relevant medical records and perform specific tasks based on their role. This system aims to enhance record-keeping, improve accessibility to medical histories, and support efficient administrative management.

## Features
**Role-Based Access Control:** Three roles with unique access permissions—Admin, Nurse, and Student.

**Medical Record Management:** Allows Nurses to add new treatment entries and view patient history.

**Automated Timestamping:** Date and time are automatically recorded for each entry.

**User Interface (UI):** Built using Java Swing, providing a clean and intuitive design.

**Database Integration:** Uses MySQL to store, retrieve, and update medical records securely.


## Technologies Used
**Java:** Core application logic and GUI.

**Swing:** For GUI development.

**MySQL:** For database management and storage of medical records.

**JDBC:** To connect Java with MySQL.

**NetBeans:** IDE for development (NetBeans version 23).


## Prerequisites
**JDK 23.0.1:** Ensure you have JDK 23.0.1 installed.

**MySQL 8.0:** Set up MySQL and create the necessary database and tables.

**JDBC 9.1:** Ensure the appropriate JDBC driver is available.

**NetBeans IDE:** Recommended IDE for development and testing.

## System Workflow
## Login Page
The login page is the entry point for all users, where they provide credentials and select their role.

**Roles:**
**_Admin:_** Access to administrative reports and monthly summaries.

**_Nurse:_** Add treatment entries, view patient history, and view daily entries.

**_Student:_** Access to personal treatment history.

## Admin Pages
**Monthly Summary Page:** Displays a summary of total visits, common diagnoses, and frequently administered treatments for the month.

**All Entries Page:** Shows all treatment entries for a selected month, offering a comprehensive view for administrative purposes.

## Nurse Pages
**Add Entry:** Allows Nurses to record new treatment entries. The system automatically fills in the date and time.

**View Patient History:** Nurses can access a specific student’s complete treatment history.

**View Daily Entries:** Displays all entries for the current day.

## Student Page
**View Treatment History:** Students can log in to view only their personal treatment records, including details of symptoms, diagnosis, treatment given, and follow-up notes.


## Data Model
The following are the fields for each treatment record stored in the MySQL database:

### Field	Description
_Date_	Automatically fetched date of entry

_Time_	Automatically fetched time of entry

_Symptoms_	Symptoms reported by the student

_Diagnosis_	Diagnosis provided by the nurse

_TreatmentGiven_	Treatment provided during the visit

_FollowUpNotes_	Any follow-up instructions or notes

## User Interface
The UI is designed with a clean, minimalistic, light theme for ease of use. The navigation footer is positioned at the bottom, providing easy access to different parts of the application.

**Icons and Styling:** Cool icons and a consistent light color palette help improve usability.

**Navigation:** Each role-based page has clearly labeled buttons for specific functions.

## Future Improvements
**Security Enhancements:** Integrate password encryption and possibly multi-factor authentication.

**Additional Reports:** Add advanced reporting for further analytics on medical trends.

**Mobile Compatibility:** Consider a mobile-friendly version for remote access.

## Troubleshooting
**Database Connection Issues:**
Ensure MySQL service is running and accessible.
Check that database credentials in DBConnection.java are accurate.

**Missing JDBC Driver:**
Verify that JDBC 9.1 driver is included in your project library.

**UI Issues:**
Ensure you’re using the latest version of NetBeans for compatibility with Swing components.
