package com.poema.realm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*

//fully operational realm crud-app with listener that updates the view whenever there are changes.

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

            if(tv_name.text.toString() == "" || tv_age.text.toString() == ""){
                makeToast()
            }
            else{ val name = tv_name.text!!.toString()
                val str = "${tv_age.text!!}"
                val age = str.toInt()
                updateInBackground(realm,name,age)}
        }

        btn_create.setOnClickListener{

            if(tv_name.text.toString() == "" || tv_age.text.toString() == ""){
                makeToast()
            }
            else{
                val name = tv_name.text!!.toString()
                val str = "${tv_age.text!!}"
                val age:Int = str.toInt()
                createData(realm, name, age)}
        }

        btn_delete.setOnClickListener{

            if(tv_name.text.toString() == "" || tv_age.text.toString() == ""){
                makeToast()
            }
            else{ val name = tv_name.text!!.toString()
                val str = "${tv_age.text!!}"
                val age = str.toInt()
                deleteData(realm,name,age)}
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
        val dog:Dog? = realm.copyToRealm(temp)
        println("!!! answer from save function: $dog")
        if(dog==null)return
        realm.commitTransaction()
    }

    fun deleteData(realm:Realm,name: String, age:Int){
        realm.executeTransactionAsync(Realm.Transaction { bgRealm ->
            val dog = bgRealm.where<Dog>().equalTo("name", name).equalTo("age",age).findFirst()

            if (dog != null) {
                dog.deleteFromRealm()
            }
        }, Realm.Transaction.OnSuccess {

        })
    }

    fun updateInBackground(realm: Realm, name:String, age:Int){
        realm.executeTransactionAsync(Realm.Transaction { bgRealm ->
            val dog = bgRealm.where<Dog>().equalTo("name", name).findFirst()!!
            dog.age = age // Update its age value.
        }, Realm.Transaction.OnSuccess {
        })
    }
    fun setListener(realm: Realm){

        (dogs as RealmResults<Dog>?)?.addChangeListener { results, changeSet ->

            println("!!! Listener results: $results")
            var str = ""
            for (dog in results){
                str += "name : ${dog?.name.toString()} \nage : ${dog?.age.toString()}\n"
            }
            tv_result.text = str

            println("!!! Changeset: insertions: ${changeSet.insertions}, deletions ${changeSet.deletions}, changes : ${changeSet.changes}")
        }
    }
    fun makeToast(){
        Toast.makeText(this,"You need to fill in both fields",Toast.LENGTH_LONG).show()
    }
}



