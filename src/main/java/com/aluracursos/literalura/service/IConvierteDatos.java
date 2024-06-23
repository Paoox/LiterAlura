package com.aluracursos.literalura.service;

//Convierte la respuesta de la API a una clase
public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
