/*
 * Clase Prestamo
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Juan Antonio
 */
@Entity
public class Prestamo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date fechaPrestamo;
    @Temporal(TemporalType.DATE)
    private Date fechaDevolucion;
    @ManyToOne
    @JoinColumn(name = "libro_id")
    private Libro libro;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @Enumerated(EnumType.STRING)
    private EstadoPrestamo estadoPrestamo;

    public Prestamo() {
    }

    public Prestamo(Date fechaPrestamo, Date fechaDevolucion) {
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public EstadoPrestamo getEstadoPrestamo() {
        return estadoPrestamo;
    }

    public void setEstadoPrestamo(EstadoPrestamo estadoPrestamo) {
        this.estadoPrestamo = estadoPrestamo;
    }

    
    
    @Override
    public String toString() {
        return "Prestamo{"
                + "id=" + id
                + ", fechaPrestamo=" + fechaPrestamo
                + ", fechaDevolucion=" + fechaDevolucion
                + '}';
    }
}
