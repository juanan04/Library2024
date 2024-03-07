package modelo.entidades;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.entidades.Prestamo;
import modelo.entidades.TipoUsuario;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2024-03-05T22:46:22")
@StaticMetamodel(Usuario.class)
public class Usuario_ { 

    public static volatile SingularAttribute<Usuario, String> apellidos;
    public static volatile SingularAttribute<Usuario, String> password;
    public static volatile SingularAttribute<Usuario, String> domicilio;
    public static volatile SingularAttribute<Usuario, TipoUsuario> tipoUsuario;
    public static volatile SingularAttribute<Usuario, Long> id;
    public static volatile SingularAttribute<Usuario, String> nombre;
    public static volatile ListAttribute<Usuario, Prestamo> prestamos;
    public static volatile SingularAttribute<Usuario, String> email;

}