package ma.insea.connect.chat.group;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findById(Long id);


    @Query(value = "SELECT * FROM testo.chat_group WHERE EXISTS (SELECT 1 FROM unnest(users) AS u(id) WHERE u.id = :id)", nativeQuery = true)

    List<Group> findAllByUser(Long id);

}