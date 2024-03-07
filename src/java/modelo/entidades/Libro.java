/*
 * Clase de la entidad Libro
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Juan Antonio
 */
@Entity
public class Libro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 13)
    private String isbn;
    @Column(nullable = false, length = 100)
    private String titulo;
    @JoinColumn
    @ManyToMany
    private List<Autor> autores;
    @JoinColumn
    @ManyToMany
    private List<Genero> generos;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fechaEdicion;
    @Column(nullable = false, length = 255)
    private String imagenPortada;
    @Column(nullable = false)
    private int ejemplaresDisponibles;
    @JoinColumn
    @OneToMany(mappedBy = "libro")
    private List<Prestamo> prestamos;

    public Libro() {
    }

    public Libro(String isbn, String titulo, List<Autor> autores, List<Genero> generos,
            Date fechaEdicion, String imagenPortada, int ejemplaresDisponibles) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autores = autores;
        this.generos = generos;
        this.fechaEdicion = fechaEdicion;
        this.imagenPortada = imagenPortada;
        this.ejemplaresDisponibles = ejemplaresDisponibles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public List<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(List<Genero> generos) {
        this.generos = generos;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }

    public Date getFechaEdicion() {
        return fechaEdicion;
    }

    public void setFechaEdicion(Date fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }

    public String getImagenPortada() {
        return imagenPortada;
    }

    public void setImagenPortada(String imagenPortada) {
        this.imagenPortada = imagenPortada;
    }

    public int getEjemplaresDisponibles() {
        return ejemplaresDisponibles;
    }

    public void setEjemplaresDisponibles(int ejemplaresDisponibles) {
        this.ejemplaresDisponibles = ejemplaresDisponibles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Libro{id=").append(id);
        sb.append(", isbn='").append(isbn).append('\'');
        sb.append(", titulo='").append(titulo).append('\'');
        sb.append(", autores=[");
        for (Autor autor : autores) {
            sb.append(autor.getNombre()).append(", ");
        }
        // Eliminar la última coma y el espacio si hay autores
        if (!autores.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        sb.append(", generos=[");
        for (Genero genero : generos) {
            sb.append(genero.getNombre()).append(", ");
        }
        // Eliminar la última coma y el espacio si hay generos
        if (!generos.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        sb.append(", fechaEdicion='").append(fechaEdicion).append('\'');
        sb.append(", imagenPortada='").append(imagenPortada).append('\'');
        sb.append(", ejemplaresDisponibles=").append(ejemplaresDisponibles);
        sb.append('}');
        return sb.toString();
    }
}
