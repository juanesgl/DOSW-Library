package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.core.exception.BookNotAvaibleException;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
@Tag(name = "Loans", description = "Loan and book return management")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    @Operation(summary = "Create a loan", description = "Assigns an available book to a registered user")
    public ResponseEntity<Loan> createLoan(@RequestParam String userId, @RequestParam String bookId) throws BookNotAvaibleException {
        Loan newLoan = loanService.createLoan(userId, bookId);
        return new ResponseEntity<>(newLoan, HttpStatus.CREATED);
    }

    @PutMapping("/return")
    @Operation(summary = "Return a book", description = "Registers the return of a book and enables it in the inventory")
    public ResponseEntity<Loan> returnLoan(@RequestParam String userId, @RequestParam String bookId) {
        Loan returnedLoan = loanService.returnLoan(userId, bookId);
        return ResponseEntity.ok(returnedLoan);
    }

    @GetMapping
    @Operation(summary = "Get loan history", description = "Returns a list of all loans (active and returned)")
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }
}