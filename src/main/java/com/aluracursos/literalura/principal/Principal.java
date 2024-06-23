package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.modelo.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatosJson;


import java.util.*;

//Se crea la clase principal
public class Principal {
    //Escaneamos la entrada del teclado
    private Scanner teclado = new Scanner(System.in);
    //Se crea una instancia de ConsumoAPI
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    //Se crea la variable URL_BASE
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatosJson convierteDatosJson = new ConvierteDatosJson();
    //Se crea una lista de libros que se inicializa vacía y despues se agregan los libros con base a la clase Libro
    private List<Libro> libros;
    //Se crea una lista de autores que se inicializa vacía y despues se agregan los libros con base a la clase Autores
    private List<Autor> autores;
    //se crcea una instancia de LibroRepository
    private LibroRepository libroRepository;
    //se crea una instancia de AutorRepository // una instancia es una copia de la clase
    private AutorRepository autorRepository;


    public Principal(AutorRepository autorRepository, LibroRepository libroRepository){
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }
    //Menu de opciones
    public void muestramenu(){
        int opcion = -1;
        String menu = """
          \n******************** Hola Bienvenido ********************
                   Estas en el Buscador de Libros
                   Selecciona la opción deseada:
          
          1) Buscar libro por título 
          2) Consulta Lista de Libros registrados
          3) Consulta Lista de Autores registrados
          4) Consulta Lista de Autores vivos en un determinado año
          5) Consulta Lista de Libros por Idioma
          
          0) Salir
          ******************** ******************** *****************       
          """;


        //Se pone Un while para crear un bucle infinito y se encapsula un switch para las opciones
        while (opcion != 0) {
            System.out.println(menu);
            try {
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número del 0 al 5.");
                teclado.nextLine();
                continue;
            }
            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresPorYear();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Saliendo...Hasta pronto");
                    System.out.println("""
                    ****************
                    
                    Este fue un programa creado por Paoxx Dev
                    
                    
                    ****************** """);
                    break;
                default:
                    System.out.printf("Opción inválidaPor favor, ingrese un número del 0 al 5.\n");
            }

    }

    }


    private void listarLibrosPorIdioma() {
        String menuIdioma = """
                Ingrese la opción deseada per pertence al idioma que deseas consultar: 
                es >> Español
                en >> Ingles
                fr >> Frances 
                pt >> Portugues
                """;
        System.out.println(menuIdioma);
        String idiomaBuscado = teclado.nextLine();
        CategoriaIdioma idioma = null;
        switch (idiomaBuscado){
            case "es":
                idioma = CategoriaIdioma.fromEspanol("Español") ;
                break;
            case "en":
                idioma = CategoriaIdioma.fromEspanol("Ingles") ;
                break;
            case "fr":
                idioma = CategoriaIdioma.fromEspanol("Frances") ;
                break;
            case "pt":
                idioma = CategoriaIdioma.fromEspanol("Portugues");
                break;
            default:
                System.out.println("Entrada inválida.");
                return;

        }
        buscarPorIdioma(idioma);

    }

    private void buscarPorIdioma(CategoriaIdioma idioma){
        libros = libroRepository.findLibrosByidioma(idioma);
        if (libros.isEmpty()){
            System.out.println("No hay libros registrados");
        } else {
            libros.stream().forEach(System.out::println);
        }
    }

    private void listarAutoresPorYear() {

        System.out.println("Ingrese el año vivo de Autore(s) que desea buscar: ");
        try {
            Integer year = teclado.nextInt();
            autores = autorRepository.findAutoresByYear(year);
            if (autores.isEmpty()){
                System.out.println("No hay autores en ese rango");
            } else {
                autores.stream().forEach(System.out::println);
            }
        } catch (InputMismatchException e) {
            System.out.println("Ingrese un año correcto");
            teclado.nextLine();
        }

    }


    private void listarAutoresRegistrados() {
        autores = autorRepository.findAll();
        autores.stream().forEach(System.out::println);
    }

    private void listarLibrosRegistrados() {
        libros = libroRepository.findAll();
        libros.stream().forEach(System.out::println);

    }

    private String realizarConsulta (){
        System.out.println("Escribe el nombre del libro a buscar: ");
        var nombreLibro = teclado.nextLine();
        //Para generar la busqueda correcta debe tener espacios reemplazados por %20 para que la URL los encuentre
        String url = URL_BASE + "?search=" + nombreLibro.replace(" ", "%20");
        System.out.println("Esperando la respuesta...");
        String respuesta = consumoAPI.obtenerDatosApi(url);
        return respuesta;
    }

    private void buscarLibroPorTitulo() {
        String respuesta = realizarConsulta();
        DatosConsultaAPI datosConsultaAPI =convierteDatosJson.obtenerDatos(respuesta, DatosConsultaAPI.class);
        if (datosConsultaAPI.numeroLibros() !=0) {
            DatosLibro primerLibro = datosConsultaAPI.resultado().get(0);
            Autor autorLibro = new Autor(primerLibro.autores().get(0));
            Optional<Libro> libroBase = libroRepository.findLibroBytitulo(primerLibro.titulo());
            if (libroBase.isPresent()) {
                System.out.println("No se puede registrar el mismo líbro ");
            } else {
                Optional<Autor> autorDeBase = autorRepository.findBynombre(autorLibro.getNombre());
                if (autorDeBase.isPresent()) {
                    autorLibro = autorDeBase.get();
                } else {
                    autorRepository.save(autorLibro);
                }

                Libro libro = new Libro(primerLibro);
                libro.setAutor(autorLibro);
                libroRepository.save(libro);
                System.out.println(libro);
            }
        } else {
            System.out.println("Líbro no encontrado...");
        }
    }


}
