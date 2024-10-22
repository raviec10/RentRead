import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUserIdAndReturnDateIsNull(Long userId);
    Optional<Rental> findByUserIdAndBookIdAndReturnDateIsNull(Long userId, Long bookId);
}
