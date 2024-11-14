package com.example.rushapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class DataHandler(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    // Function to populate sample data
    fun populateSampleData() {
        val db = dbHelper.writableDatabase

        // Insert a workshop if not already present
        val workshopId = getWorkshopIdByName(db, "Example Workshop")
            ?: dbHelper.insertWorkshop(db, "Example Workshop", "123 Main Street")

        // Insert a mechanic associated with the workshop if not already present
        val mechanicId = getMechanicIdByEmail(db, "johndoe@example.com")
            ?: dbHelper.insertMechanic(db, workshopId, "John Doe", "johndoe@example.com", "123-456-7890", "Mechanic")

        // Insert a customer if not already present
        val customerId = getCustomerIdByName(db, "Customer")
            ?: dbHelper.insertCustomer(db, "Customer")

        // Insert a user if not already present
        val userId = getUserIdByEmail(db, "alice@example.com")
            ?: dbHelper.insertUser(db, mechanicId, customerId, "Alice Smith", "alice@example.com", "987-654-3210", "Admin", "password123")

        // Insert a vehicle if not already present
        val vehicleId = getVehicleIdByVin(db, "1HGCM82633A123456")
            ?: dbHelper.insertVehicle(db, userId, "Toyota", "Corolla", 2020, "1HGCM82633A123456")

        // Insert a service if not already present
        if (!isServicePresent(db, mechanicId, vehicleId, "2024-11-12")) {
            dbHelper.insertService(db, mechanicId, vehicleId, "2024-11-12", "Oil Change", "Changed engine oil and filter")
        }

        // Insert a notification if not already present
        if (!isNotificationPresent(db, userId, "2024-11-13")) {
            dbHelper.insertNotification(db, userId, "2024-11-13", "Your next service is due soon.")
        }

        db.close()
    }

    // Function to get Workshop ID by name
    private fun getWorkshopIdByName(db: SQLiteDatabase, name: String): Long? {
        val cursor = db.query(
            "Workshops",
            arrayOf("workshopId"),
            "name = ?",
            arrayOf(name),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) {
                it.getLong(it.getColumnIndexOrThrow("workshopId"))
            } else {
                null
            }
        }
    }

    // Function to get Mechanic ID by email
    private fun getMechanicIdByEmail(db: SQLiteDatabase, email: String): Long? {
        val cursor = db.query(
            "Mechanics",
            arrayOf("mechanicId"),
            "email = ?",
            arrayOf(email),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) {
                it.getLong(it.getColumnIndexOrThrow("mechanicId"))
            } else {
                null
            }
        }
    }

    // Function to get Customer ID by name
    private fun getCustomerIdByName(db: SQLiteDatabase, name: String): Long? {
        val cursor = db.query(
            "Customers",
            arrayOf("customerId"),
            "name = ?",
            arrayOf(name),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) {
                it.getLong(it.getColumnIndexOrThrow("customerId"))
            } else {
                null
            }
        }
    }

    // Function to get User ID by email
    private fun getUserIdByEmail(db: SQLiteDatabase, email: String): Long? {
        val cursor = db.query(
            "Users",
            arrayOf("userId"),
            "email = ?",
            arrayOf(email),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) {
                it.getLong(it.getColumnIndexOrThrow("userId"))
            } else {
                null
            }
        }
    }

    // Function to get Vehicle ID by VIN
    private fun getVehicleIdByVin(db: SQLiteDatabase, vin: String): Long? {
        val cursor = db.query(
            "Vehicles",
            arrayOf("vehicleId"),
            "vin = ?",
            arrayOf(vin),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) {
                it.getLong(it.getColumnIndexOrThrow("vehicleId"))
            } else {
                null
            }
        }
    }

    // Function to check if a service is present
    private fun isServicePresent(db: SQLiteDatabase, mechanicId: Long, vehicleId: Long, date: String): Boolean {
        val cursor = db.query(
            "Services",
            arrayOf("serviceId"),
            "mechanicId = ? AND vehicleId = ? AND date = ?",
            arrayOf(mechanicId.toString(), vehicleId.toString(), date),
            null,
            null,
            null
        )
        return cursor.use { it.moveToFirst() }
    }

    // Function to check if a notification is present
    private fun isNotificationPresent(db: SQLiteDatabase, userId: Long, date: String): Boolean {
        val cursor = db.query(
            "Notifications",
            arrayOf("notificationId"),
            "userId = ? AND date = ?",
            arrayOf(userId.toString(), date),
            null,
            null,
            null
        )
        return cursor.use { it.moveToFirst() }
    }
}
