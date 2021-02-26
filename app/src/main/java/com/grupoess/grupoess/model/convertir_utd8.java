package com.grupoess.grupoess.model;

public class convertir_utd8 {
    public String get_text(String texto){
        texto = texto.replace("Ã¡","á");
        texto = texto.replace("Ã³","ó");
        texto = texto.replace("Ã©","é");
        texto = texto.replace("Ã“","ó");
        texto = texto.replace("Ã±","ñ");
        texto = texto.replace("&#8211;","-");

        return texto;
    }
}
