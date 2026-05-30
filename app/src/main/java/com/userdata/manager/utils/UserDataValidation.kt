package com.userdata.manager.utils

import com.userdata.manager.R

object UserDataValidation {

    fun dataValidation(
        name: String,
        address: String,
        phone: String
    ): Int? {
        if (name.isBlank()) return R.string.name_can_not_be_empty
        if (address.isBlank()) return R.string.address_cannot_be_empty
        if (phone.isBlank()) return R.string.phone_number_cannot_be_empty
        if (!phone.matches(Regex("\\d{10}"))) return R.string.phone_number_must_be_10_digits

        return null
    }
}