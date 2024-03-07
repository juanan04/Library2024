package modelo.entidades;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.entidades.EstadoPrestamo;
import modelo.entidades.Libro;
import modelo.entidades.Usuario;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2024-03-05T22:46:22")
@StaticMetamodel(Prestamo.class)
public class Prestamo_ { 

    public static volatile SingularAttribute<Prestamo, Libro> libro;
    public static volatile SingularAttribute<Prestamo, Date> fechaDevolucion;
    public static volatile SingularAttribute<Prestamo, Date> fechaPrestamo;
    public static volatile SingularAttribute<Prestamo, Usuario> usuario;
    public static volatile SingularAttribute<Prestamo, Long> id;
    public static volatile SingularAttribute<Prestamo, EstadoPrestamo> estadoPrestamo;

}