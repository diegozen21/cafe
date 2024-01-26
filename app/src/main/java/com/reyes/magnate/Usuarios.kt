package com.reyes.magnate

import android.provider.BaseColumns

object Usuarios {
    class UserEntry : BaseColumns {
        companion object {
            const val TABLE = "usuario"
            const val COLUMN_ID = "ID"
            const val COLUMN_NOMBRES = "nombres"
            const val COLUMN_APELLIDO_P = "apellido"
            const val COLUMN_CORREO = "correo"
            const val COLUMN_USERNAME = "username"
            const val COLUMN_PASSWORD = "password"
        }
    }
}