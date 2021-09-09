package com.liang.review.aidl

import android.os.IInterface

interface PersonManger :IInterface{
    /* 添加人数
    *
    * @throws RemoteException
    */
    fun addPerson(person: Person)

    /**
     * 删除人数
     *
     * @throws RemoteException
     */
    fun deletePerson(person: Person)

    /**
     * 获取人数
     *
     * @throws RemoteException
     */
    fun  getPersons():List<Person>
}