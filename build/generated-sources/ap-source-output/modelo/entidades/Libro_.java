package modelo.entidades;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.entidades.Autor;
import modelo.entidades.Genero;
import modelo.entidades.Prestamo;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2024-03-05T22:46:22")
@StaticMetamodel(Libro.class)
public class Libro_ { 

    public static volatile SingularAttribute<Libro, Date> fechaEdicion;
    public static volatile SingularAttribute<Libro, String> imagenPortada;
    public static volatile SingularAttribute<Libro, String> isbn;
    public static volatile ListAttribute<Libro, Genero> generos;
    public static volatile SingularAttribute<Libro, String> titulo;
    public static volatile SingularAttribute<Libro, Integer> ejemplaresDisponibles;
    public static volatile SingularAttribute<Libro, Long> id;
    public static volatile ListAttribute<Libro, Autor> autores;
    public static volatile ListAttribute<Libro, Prestamo> prestamos;

}