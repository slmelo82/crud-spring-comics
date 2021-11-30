package sara.melo.crudspringcomics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sara.melo.crudspringcomics.models.Comic;

public interface ComicRepository extends JpaRepository<Comic, Integer> {

}
