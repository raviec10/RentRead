import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/{bookId}/rent")
    public String rentBook(@PathVariable Long bookId, @RequestParam Long userId) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        if (!book.isAvailabilityStatus()) {
            return "Book not available";
        }

        User user = userRepository.findById(userId).orElseThrow();
        List<Rental> userRentals = rentalRepository.findByUserIdAndReturnDateIsNull(userId);
        if (userRentals.size() >= 2) {
            return "User already has two active rentals";
        }

        Rental rental = new Rental();
        rental.setBook(book);
        rental.setUser(user);
        rental.setRentalDate(LocalDate.now());
        rentalRepository.save(rental);

        book.setAvailabilityStatus(false);
        bookRepository.save(book);

        return "Book rented successfully";
    }

    @PostMapping("/{bookId}/return")
    public String returnBook(@PathVariable Long bookId, @RequestParam Long userId) {
        Rental rental = rentalRepository.findByUserIdAndBookIdAndReturnDateIsNull(userId, bookId).orElseThrow();
        rental.setReturnDate(LocalDate.now());
        rentalRepository.save(rental);

        Book book = bookRepository.findById(bookId).orElseThrow();
        book.setAvailabilityStatus(true);
        bookRepository.save(book);

        return "Book returned successfully";
    }
}
