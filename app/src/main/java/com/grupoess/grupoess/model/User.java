package com.grupoess.grupoess.model;

public class User {
    private static String id;
    private static String nombre;
    private static String apellido;
    private static String direccion;
    private static String telefono;
    private static String correo;
    private static String fecha_ultimo_ingreso;

    public void set_user(String v_id, String v_nonbre, String v_apellido, String v_direccion, String v_telefono,
                         String v_correo, String v_fecha_ultimo_ingreso){
        id = v_id;
        nombre = v_nonbre;
        apellido = v_apellido;
        direccion = v_direccion;
        telefono = v_telefono;
        correo = v_correo;
        fecha_ultimo_ingreso = v_fecha_ultimo_ingreso;
    }

    public String get_id(){return id;}
    public String get_nombre(){
        return nombre;
    }
    public String get_apellido(){
        return apellido;
    }
    public String get_direccion(){
        return direccion;
    }
    public String get_telefono(){
        return telefono;
    }
    public String get_correo(){
        return correo;
    }
    public String get_fecha_ultimo_ingreso(){
        return fecha_ultimo_ingreso;
    }
}