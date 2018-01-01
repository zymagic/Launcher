package com.abs.launcher.model.db

/**
 * Created by zy on 17-12-29.
 */
@Target(AnnotationTarget.TYPE)
annotation class Table(val name: String)

enum class ColumnType {
    INTEGER, TEXT, BLOB
}

@Target(AnnotationTarget.FIELD)
annotation class Type(val type: ColumnType)

@Target(AnnotationTarget.FIELD)
annotation class PrimaryKey

@Target(AnnotationTarget.FIELD)
annotation class NotNull

@Target(AnnotationTarget.FIELD)
annotation class Def(val value: String)
