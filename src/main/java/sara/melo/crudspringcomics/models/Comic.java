package sara.melo.crudspringcomics.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="comics", uniqueConstraints={@UniqueConstraint(columnNames = {"ISBN"})})
public class Comic {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "comicId")
	@NotNull(message = "O comicId é obrigatório!")
	private Integer comicId;
	
	@NotBlank(message = "O titulo é obrigatório!")
	private String titulo;
	
	@NotNull(message = "O preco é obrigatório!")
	private Double preco;
	
	@NotBlank(message = "Os autores são obrigatórios!")
	private String autores;
	
	@NotBlank(message = "Os ISBN e obrigatório!")
	private String ISBN;

	private String descricao;

	@ManyToMany(cascade = CascadeType.ALL)
	@JsonIgnore
	@JoinTable(name="comics_users",
			joinColumns = {@JoinColumn(name = "comic_fk")},
			inverseJoinColumns = {@JoinColumn(name = "user_fk")}
	)
	private Set<User> users = new HashSet<>();

	public Comic() {

	}

	public Comic(Integer id, Integer comicId, String titulo, Double preco, String autores, String ISBN, String descricao) {
		this.id = id;
		this.comicId = comicId;
		this.titulo = titulo;
		this.preco = preco;
		this.autores = autores;
		this.ISBN = ISBN;
		this.descricao = descricao;
	}

	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getComicId() {
		return comicId;
	}
	public void setComicId(Integer comicId) {
		this.comicId = comicId;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Double getPreco() {
		return preco;
	}
	public void setPreco(Double preco) {
		this.preco = preco;
	}
	public String getAutores() {
		return autores;
	}
	public void setAutores(String autores) {
		this.autores = autores;
	}
	public String getISBN() {
		return ISBN;
	}
	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	
}
