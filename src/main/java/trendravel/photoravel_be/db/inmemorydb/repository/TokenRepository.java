package trendravel.photoravel_be.db.inmemorydb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import trendravel.photoravel_be.db.inmemorydb.entity.Token;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {
}
