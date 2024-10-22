import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rentread.model.Book;
import com.rentread.model.User;
import com.rentread.model.Rental;
import com.rentread.repository.RentalRepository;
import com.rentread.service.RentalService;

import java.util.Optional;

public class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private RentalService rentalService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRentBook() {
        User user = new User("test@example.com", "password123", "John", "Doe", "USER");
        Book book = new Book(1L, "Book One", "Author One", "Fiction", true);

        Rental rental = new Rental(user, book);
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

        Rental result = rentalService.rentBook(user, book);

        assertNotNull(result);
        assertEquals("Book One", result.getBook().getTitle());
        verify(rentalRepository, times(1)).save(any(Rental.class));
    }

    @Test
    public void testReturnBook() {
        User user = new User("test@example.com", "password123", "John", "Doe", "USER");
        Book book = new Book(1L, "Book One", "Author One", "Fiction", false);
        Rental rental = new Rental(user, book);

        when(rentalRepository.findByUserAndBook(user, book)).thenReturn(Optional.of(rental));

        rentalService.returnBook(user, book);

        verify(rentalRepository, times(1)).delete(any(Rental.class));
    }
}
