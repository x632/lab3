package com.poema.realm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*

//fully operational crud-app with listener that updates the view.
class MainActivity : AppCompatActivity() {

    var dogs = mutableListOf<Dog?>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //bara en gång!
        Realm.init(this)

        val realm = Realm.getDefaultInstance()
        getAll(realm)
        setListener(realm)

        btn_update.setOnClickListener{

            if(tv_name != null && tv_age != null){
                val name = tv_name.text!!.toString()
                val str = "${tv_age.text!!}"
                val age = str.toInt()
                updateInBackground(realm,name,age)
            }
        }

        btn_create.setOnClickListener{

            if(tv_name != null && tv_age != null){
                val name = tv_name.text!!.toString()
                val str = "${tv_age.text!!}"
                val age = str.toInt()
                createData(realm,name,age)
            }
        }

        btn_delete.setOnClickListener{

            if(tv_name != null && tv_age != null){
                val name = tv_name.text!!.toString()
                val str = "${tv_age.text!!}"
                val age = str.toInt()
                deleteData(realm,name,age)
            }
        }

        btn_read.setOnClickListener{
                getAll(realm)
        }

    }


    fun getAll(realm: Realm) {
        var str = ""
        dogs = realm.where<Dog>().findAll()

    println("!!! från readfunktion $dogs")
        for (dog in dogs){
           str += "name : ${dog?.name.toString()} \nage : ${dog?.age.toString()}\n"
        }
        tv_result.text = str
    }

    fun createData(realm: Realm,name: String, age: Int) {
        val temp = Dog(name, age)
        realm.beginTransaction()
        val dog = realm.copyToRealm(temp)
        println("!!! answer from save function: $dog")
        realm.commitTransaction()
        //getAll(realm)
    }

    fun deleteData(realm:Realm,name: String, age:Int){
        realm.executeTransactionAsync(Realm.Transaction { bgRealm ->
            val dog = bgRealm.where<Dog>().equalTo("name", name).equalTo("age",age).findFirst()!!
            dog.deleteFromRealm()
        }, Realm.Transaction.OnSuccess {
           //getAll(realm)
        })
    }

    fun updateInBackground(realm: Realm, name:String, age:Int){
        realm.executeTransactionAsync(Realm.Transaction { bgRealm ->
            val dog = bgRealm.where<Dog>().equalTo("name", name).findFirst()!!
            dog.age = age // Update its age value.
        }, Realm.Transaction.OnSuccess {
           //getAll(realm)
        })
    }
    fun setListener(realm: Realm){

        (dogs as RealmResults<Dog>?)?.addChangeListener { results, changeSet ->
            // Query results are updated in real time with fine grained notifications.
            println("!!! Listener results: $results")
            var str = ""
            for (dog in results){
                str += "name : ${dog?.name.toString()} \nage : ${dog?.age.toString()}\n"
            }
            tv_result.text = str

            println("!!! Changeset: insertions: ${changeSet.insertions}, deletions ${changeSet.deletions}, changes : ${changeSet.changes}")
        }

    }
}


