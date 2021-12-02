package sara.melo.crudspringcomics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sara.melo.crudspringcomics.models.Comic;

import java.util.Set;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Integer> {

    @Query("SELECT  new Comic(t.id, t.comicId, t.titulo, t.preco, t.autores,  t.ISBN, t.descricao) from Comic t join t.users u where u.id = :id")
    Set<Comic> findAllByUserId(@Param("id") Integer id);
}
