package com.poema.realm

// Import the Kotlin extensions for Realm.
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.createObject
import io.realm.kotlin.where

// Define your model classes by extending RealmObject.
// You must annotate all classes with `open`.
open class Dog(
    // You can put properties in the constructor as long as
    // all of them are initialized with default values. This
    // ensures that an empty constructor is generated.
    // All properties are by default persisted.
    // Non-nullable properties must be initialized
    // with non-null values.
    var name: String = "",
    var age: Int = 0
): RealmObject()
/*
open class Person(
    // Properties can be annotated with PrimaryKey or Index.
    @PrimaryKey var id: Long = 0,
    var name: String = "",
    var age: Int = 0,

    // Other objects in a one-to-one relation must also subclass RealmObject.
    var dogs: RealmList<Dog> = RealmList()
): RealmObject()*/

// ...

// Use Realm objects like regular Kotlin objects
