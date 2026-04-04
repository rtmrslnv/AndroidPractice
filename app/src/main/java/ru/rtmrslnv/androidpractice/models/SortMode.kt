package ru.rtmrslnv.androidpractice.models

enum class SortMode(val value : String, val russianName : String = "") {
    RELEVANT("relevant", "Релевантные"),
    RECENT("recent", "Недавние"),
    SALARY_ASC("salaryAsc", "З/п(по возрастанию)"),
    SALARY_DESC("salaryDesc", "З/п(по убыванию)"),
    NAME_A_TO_Z("nameAToZ", "Название(A-Z)"),
    NAME_Z_TO_A("nameZToA", "Название(Z-A)");

    companion object {
        fun fromValue(value : String) : SortMode? {
            return values().firstOrNull { it.value == value }
        }
    }
}