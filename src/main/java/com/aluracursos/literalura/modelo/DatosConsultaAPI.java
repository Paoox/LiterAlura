package com.aluracursos.literalura.modelo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

//La info de la respuesta que necesitamos mapear a la clase
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosConsultaAPI(
        @JsonAlias("count") Integer numeroLibros,
        @JsonAlias("next") String pagProxima,
        @JsonAlias("previous") String pagAnterior,
        @JsonAlias("results") List<DatosLibro> resultado
        ) {
}
