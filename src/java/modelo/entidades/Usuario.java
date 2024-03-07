/*
 * Clase de la entidad Usuario
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Juan Antonio
 */
@Entity
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String nombre;
    @Column(nullable = false, length = 100)
    private String apellidos;
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    @Column(nullable = false, length = 100)
    private String password;
    @Column(nullable = false, length = 255)
    private String domicilio;
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;
    @OneToMany(mappedBy = "usuario")
    private List<Prestamo> prestamos;

    public Usuario() {
    }

    public Usuario(String nombre, String apellidos, String email, String password,
            String domicilio, TipoUsuario tipoUsuario) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.password = password;
        this.domicilio = domicilio;
        this.tipoUsuario = tipoUsuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public String toString() {
        return "Usuario{"
                + "id=" + id
                + ", nombre='" + nombre + '\''
                + ", apellidos='" + apellidos + '\''
                + ", email='" + email + '\''
                + ", password='" + "****" + '\''
                + ", domicilio='" + domicilio + '\''
                + ", tipoUsuario=" + tipoUsuario
                + '}';
    }
}
