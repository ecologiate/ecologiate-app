package com.app.ecologiate.services;


import com.app.ecologiate.models.Usuario;

public class UserManager {

    private static Usuario loggedUser = new Usuario(1L, "untoken_asd123");

    public static Usuario getUser(){
        return loggedUser;
    }
}
