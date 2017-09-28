package com.app.ecologiate.services;


import com.app.ecologiate.models.Nivel;
import com.app.ecologiate.models.Usuario;

public class UserManager {

    private static Usuario loggedUser = new Usuario(1L, "Ecologiate", "",
            "ecologiateapp@gmail.com", "", 0L, new Nivel(1L,"Novato",
            "ecologiate.herokuapp.com/images/avatar_novato.png",
            0L,1000L));

    public static Usuario getUser(){
        return loggedUser;
    }
}
