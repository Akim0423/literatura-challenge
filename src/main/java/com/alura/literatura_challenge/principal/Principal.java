package com.alura.literatura_challenge.principal;

import com.alura.literatura_challenge.model.*;
import com.alura.literatura_challenge.repository.AutorRepository;
import com.alura.literatura_challenge.repository.LibroRepository;
import com.alura.literatura_challenge.service.ConsumoAPI;
import com.alura.literatura_challenge.service.ConvierteDatos;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Principal {
    private Scanner teclado= new Scanner(System.in);
    private ConsumoAPI consumoAPI= new ConsumoAPI();
    private final String URL_BASE="https://gutendex.com/books/";
    private ConvierteDatos conversor= new ConvierteDatos();
    private LibroRepository repositorio;
    private AutorRepository autorRepositorio;
    private List<DatosLibro> datosLibros = new ArrayList<>();
    private List<Libros> datos;

    public Principal(LibroRepository repository, AutorRepository autorRepository) {
        this.repositorio=repository;
        this.autorRepositorio = autorRepository;
    }

    public void menu(){
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    \n***** MENU *****  
                    
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    
                    Elija la opcion: 
                    """;
            System.out.println(menu);
            opcion=teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
                case 1:
                    buscarLibros();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutorVivo();
                    break;
                case 5:
                    listarIdiomas();
                    break;
                case 0:
                    System.out.println("\nCerrando la aplicación...\n");
                    break;
                default:
                    System.out.println("\nOpción inválida\n");
            }
        }
    }

    private Libros crearLibro(DatosLibro datosLibros, Autor autor) {
        if (autor != null) {
            return new Libros(datosLibros, autor);
        } else {
            System.out.println("El autor es null, no se puede crear el libro");
            return null;
        }
    }

    private void buscarLibros() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = teclado.nextLine();
        if (!nombreLibro.isBlank()){
            var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
            var datos = conversor.obtenerDatos(json, Respuesta.class);
            if (!datos.libros().isEmpty()){
                DatosLibro datosLibros = datos.libros().get(0);
                AutorDatos datosAutor = datosLibros.autor().get(0);
                Libros libro = null;
                Libros libroRepo = repositorio.findByTitulo(datosLibros.titulo());
                if (libroRepo != null){
                    System.out.println("\nEste libre ya se encuentra ingresado en la base de datos.");
                    System.out.println(libroRepo.toString());
                } else {
                    Autor autorRepo = autorRepositorio.findByNombreIgnoreCase(datosLibros.autor().get(0).nombre());
                    if (autorRepo != null){
                        libro = crearLibro(datosLibros, autorRepo);
                        repositorio.save(libro);
                        System.out.println("\nSe agregó un nuevo libro a la base de datos. \n");
                        System.out.println(libro);
                    } else {
                        Autor autor = new Autor(datosAutor);
                        autor = autorRepositorio.save(autor);
                        libro = crearLibro(datosLibros, autor);
                        repositorio.save(libro);
                        System.out.println("\nSe agregó un nuevo libro a la base de datos. \n");
                        System.out.println(libro);
                    }
                }
            } else {
                System.out.println("\n<ATENCION> El libro buscado NO existe en la API de Gutendex, ingresa otro");
                System.out.println("URL de búsqueda: " + URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));

            }
        } else {
            System.out.println("No ingresó un nombre de libro.");
        }

    }

    private void listarLibros() {
        System.out.println("Los libros registrados en la base de datos son: \n");
        List<Libros> librosRegistrados = repositorio.findAll();
        if (!librosRegistrados.isEmpty()){
            librosRegistrados.stream()
                    .forEach(System.out::println);
        } else {
            System.out.println("No hay ningún libro aún registrado en la base de datos.");
        }
    }

    private void listarAutores() {
        System.out.println("\nLos autores registrados en la base de datos son: \n");
        List<Autor> autoresRegistrados = autorRepositorio.findAll();
        if (!autoresRegistrados.isEmpty()){
            autoresRegistrados.stream()
                    .sorted(Comparator.comparing(Autor::getNombre))
                    .forEach(System.out::println);
        } else {
            System.out.println("No hay ningún autor aún registrado en la base de datos.");
        }
    }

    private void listarAutorVivo() {
        System.out.println("Ingresa el año para listar los autores que deseas buscar");
        var anio = teclado.nextInt();
        teclado.nextLine();
        if (anio > 0) {
            List<Autor> autorPorAnio = autorRepositorio
                    .findByAnioNacimientoLessThanEqualAndAnioMuerteGreaterThanEqual(anio, anio);
            if (!autorPorAnio.isEmpty()){
                System.out.println("Los autores vivos registrados en " + anio + " en la base de datos son: \n");
                autorPorAnio.stream()
                        .sorted(Comparator.comparing(Autor::getNombre))
                        .forEach(System.out::println);
            } else {
                System.out.println("No hay ningún autor aún registrado en " + anio + " en la base de datos.");
            }
        } else {
            System.out.println("Debe ingresar una fecha válida");
        }
    }

    private void listarIdiomas() {
        System.out.println("Escribe el idioma del libro que deseas buscar. Utilice las siguientes opciones que estan entre []:");
        System.out.println("""
                [ES]: español
                [EN]: inglés
                [FR]: francés
                [PT]: portugués
                [IT]: italiano
                """);
        var idiomaBuscado = teclado.nextLine().toLowerCase();
        if (!idiomaBuscado.isBlank()){
            if (idiomaBuscado.equals("es") ||
                    idiomaBuscado.equals("en") ||
                    idiomaBuscado.equals("fr") ||
                    idiomaBuscado.equals("pt") ||
                    idiomaBuscado.equals("it")
            ){
                List<Libros> librosBuscados = repositorio.findByIdiomaContaining(idiomaBuscado);
                if (!librosBuscados.isEmpty()){
                    AtomicInteger contador = new AtomicInteger(0);
                    librosBuscados.stream()
                            .sorted(Comparator.comparing(Libros::toString))
                            .forEach(libro -> {
                                System.out.println(libro);
                                contador.incrementAndGet();
                            });
                    System.out.println("\n Estan registrados " + contador + " libros en " + "[" + idiomaBuscado + "]");
                } else {
                    System.out.println("No hay ningún libro con el idioma " + idiomaBuscado + " registrado en la base de datos");
                }
            } else {
                System.out.println("Ingresó un idioma inválido");
            }
        } else {
            System.out.println("No escribió ningún idioma.");
        }
    }

}
