package sara.melo.crudspringcomics.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="comics", uniqueConstraints={@UniqueConstraint(columnNames = {"ISBN"})})
public class Comic {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Integer id;
	
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
	
	
	public Comic() {
		super();
	}
	
	//@ManyToMany(mappedBy="comics", fetch = FetchType.LAZY)
	//private Set<User> users = new HashSet<>();		
	
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

	@Override
	public String toString() {
		return "Comic [id=" + id + ", comicId=" + comicId + ", titulo=" + titulo + ", preco=" + preco + ", autores="
				+ autores + ", ISBN=" + ISBN + ", descricao=" + descricao + "]";
	}	
	
	
}
