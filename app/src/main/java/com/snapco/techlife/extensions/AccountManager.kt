package com.snapco.techlife.extensions

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.snapco.techlife.data.model.UserAccount

class AccountManager(
    context: Context,
) {
    private val prefs: SharedPreferences = context.sharedPreferences("accounts_prefs")
    private val gson = Gson()

    // Hàm lấy danh sách tài khoản
    fun getAccounts(): List<UserAccount> {
        val json = prefs.getString(KEY_ACCOUNTS, "[]")
        return gson.fromJson(json, object : TypeToken<List<UserAccount>>() {}.type)
    }

    // Hàm thêm tài khoản mới
    fun addAccount(account: UserAccount) {
        val accounts = getAccounts().toMutableList()
        if (accounts.none { it.id == account.id }) { // Kiểm tra tránh trùng lặp id
            accounts.add(account)
            saveAccounts(accounts)
        }
    }

    // Hàm xóa tài khoản theo ID
    fun removeAccountById(accountId: String) {
        val accounts = getAccounts().toMutableList()
        val updatedAccounts = accounts.filter { it.id != accountId }
        saveAccounts(updatedAccounts) // Lưu lại danh sách sau khi xóa
    }

    // Hàm xóa tất cả tài khoản
    fun clearAllAccounts() {
        saveAccounts(emptyList()) // Lưu danh sách rỗng
    }

    fun updateAccount(updatedAccount: UserAccount) {
        val accounts = getAccounts().toMutableList()
        val index = accounts.indexOfFirst { it.id == updatedAccount.id }
        if (index != -1) {
            accounts[index] = updatedAccount
            saveAccounts(accounts)
        }
    }

    // Hàm lưu danh sách tài khoản vào SharedPreferences
    private fun saveAccounts(accounts: List<UserAccount>) {
        val json = gson.toJson(accounts)
        prefs.edit().putString(KEY_ACCOUNTS, json).apply()
    }

    companion object {
        private const val KEY_ACCOUNTS = "accounts_list"
    }
}
