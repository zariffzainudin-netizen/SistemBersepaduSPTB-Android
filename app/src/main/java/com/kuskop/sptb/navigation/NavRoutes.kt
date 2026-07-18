package com.kuskop.sptb.navigation

object NavRoutes {
    const val SPLASH = "splash"
    const val AUTH = "auth"
    const val BIOMETRIC = "biometric"
    const val MAIN = "main"
    const val DASHBOARD = "dashboard"
    const val SENARAI = "senarai"
    const val FORM_CHECKER = "form_checker"
    const val INPUT_DATABASE = "input_database"
    const val APPROVER_VIEW = "approver_view/{appId}"
    const val APPROVER_ACTION = "approver_action/{appId}"
    const val ADMIN = "admin"
    const val PKA = "pka"
    const val INBOX = "inbox"
    const val EXCEL = "excel"
    const val BAKUL = "bakul"
    const val HISTORY = "history"
    const val SETTINGS = "settings"
    const val PROFILE = "profile"
    const val DRIVE = "drive"
}

sealed class BottomNavItem(val route: String, val title: String, val icon: String) {
    data object Dashboard : BottomNavItem(NavRoutes.DASHBOARD, "Dashboard", "\uD83D\uDCCA")
    data object Senarai : BottomNavItem(NavRoutes.SENARAI, "Senarai", "\uD83D\uDCCB")
    data object Borang : BottomNavItem(NavRoutes.FORM_CHECKER, "Borang", "\uD83D\uDCDD")
    data object Inbox : BottomNavItem(NavRoutes.INBOX, "Inbox", "\uD83D\uDCE5")
    data object Lagi : BottomNavItem("lagi", "Lagi", "\u2699\uFE0F")
}
