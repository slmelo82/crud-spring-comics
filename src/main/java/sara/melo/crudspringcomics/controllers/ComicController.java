package sara.melo.crudspringcomics.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sara.melo.crudspringcomics.models.Comic;
import sara.melo.crudspringcomics.models.User;
import sara.melo.crudspringcomics.repository.ComicRepository;

@RestController
@RequestMapping(value = "/comics")
public class ComicController {

	private ComicRepository comicRepository;
	
	public ComicController(ComicRepository comicRepository) {
		super();
		this.comicRepository = comicRepository;
	}
	
	
	@GetMapping
	//Retorna uma lista de todos os quadrinhos
	public ResponseEntity<List<Comic>> getAll() {
		List<Comic> comics = new ArrayList<>(); // Cria uma lista
		comics = comicRepository.findAll(); // Busca todos os quadrinhos no banco de dados
		return new ResponseEntity<>(comics, HttpStatus.OK); // Retorna status 200 com a lista de todos quadrinhos no banco
	}
	
	@GetMapping(path="/{id}")
	//Retorna um unico quadrinho, de acordo com o id
	public ResponseEntity<Optional<Comic>> getById(@PathVariable Integer id) {
		Optional<Comic> comic;
		
		try {
			comic = comicRepository.findById(id); // Busca o usuário no banco de dados
			return new ResponseEntity<Optional<Comic>>(comic, HttpStatus.OK); // Retorna status 200 com os dados do quadrinho em questão
		} catch (NoSuchElementException nsee) {
			return new ResponseEntity<Optional<Comic>>(HttpStatus.NOT_FOUND); // Não encontrou o quadrinho, retorna status 404;
		}			
	}	
	
	@PostMapping
	public ResponseEntity<Comic> save(@RequestBody @Valid Comic comic) {
		comicRepository.save(comic); // Salva quadrinho no banco de dados
		return new ResponseEntity<>(comic, HttpStatus.OK); // Retorna status 200 com os dados do quadrinho criado;
	}
	
	@DeleteMapping(path="/{id}")
	//Deleta um unico quadrinho, de acordo com o id
	public ResponseEntity<Optional<Comic>> deleteById(@PathVariable Integer id) {			
		try {
			comicRepository.deleteById(id); // Deleta o quadrinho no banco de dados
			return new ResponseEntity<>(HttpStatus.OK); // Retorna status 200 , quadrinho deletado com sucesso
		} catch (NoSuchElementException nsee) {
			return new ResponseEntity<Optional<Comic>>(HttpStatus.NOT_FOUND); // Não encontrou o quadrinho, retorna status 404;
		}			
	}
	
	@PutMapping(path="/{id}")
	//Atualiza os dados de um quadrinho; Se não encontrar, retorna erro 404
	public ResponseEntity<Comic> update(@PathVariable Integer id, @RequestBody @Valid Comic newComic) {			
		return comicRepository.findById(id).map(comic -> {
			comic.setComicId(newComic.getComicId());
			comic.setTitulo(newComic.getTitulo());
			comic.setPreco(newComic.getPreco());
			comic.setAutores(newComic.getAutores());
			comic.setISBN(newComic.getISBN());
			
			if(newComic.getDescricao() != null) {
				comic.setDescricao(newComic.getDescricao());
			}
			
			Comic comicUpdated = comicRepository.save(comic);
			return new ResponseEntity<>(comicUpdated, HttpStatus.OK);
		}).orElse(ResponseEntity.notFound().build());
	}
	
	
}
