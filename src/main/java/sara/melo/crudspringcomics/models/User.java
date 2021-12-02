package sara.melo.crudspringcomics.models;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import sara.melo.crudspringcomics.controllers.validation.Cpf;

@Entity
@Table(name="users", uniqueConstraints={@UniqueConstraint(columnNames = {"cpf" , "email"})})
public class User {
	
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@NotBlank(message = "O Nome é obrigatório!")
	private String nome;		

	@NotBlank(message = "O E-mail é obrigatório!")
	@Email(message = "Por favor, informe um e-mail válido!")
	private String email;	

	@Cpf
	@NotBlank(message = "O CPF é obrigatório!")
	private String cpf;
		
	@NotNull(message = "A data de nascimento é obrigatória!")
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date nascimento;

	@ManyToMany(mappedBy="users")
	@JsonIgnore
	private Set<Comic> comics = new HashSet<>();
		
	public Set<Comic> getComics() {
		return comics;
	}
	public void setComics(Set<Comic> comics) {
		this.comics = comics;
	}
	public Integer getId() {
		return id;
	}	
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public Date getNascimento() {
		return nascimento;
	}
	public void setNascimento(Date nascimento) {
		this.nascimento = nascimento;
	}
	
	

}
