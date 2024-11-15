// BorrowerServiceImpl.java
package com.lms.librarymgmtsystem.service.impl;

import com.lms.librarymgmtsystem.dto.BorrowerDTO;
import com.lms.librarymgmtsystem.entity.Book;
import com.lms.librarymgmtsystem.entity.Borrower;
import com.lms.librarymgmtsystem.exception.BookAlreadyBorrowedException;
import com.lms.librarymgmtsystem.exception.BookNotBorrowedException;
import com.lms.librarymgmtsystem.exception.DuplicateBorrowerEmailException;
import com.lms.librarymgmtsystem.exception.ResourceNotFoundException;
import com.lms.librarymgmtsystem.repository.BookRepository;
import com.lms.librarymgmtsystem.repository.BorrowerRepository;
import com.lms.librarymgmtsystem.service.BorrowerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BorrowerServiceImpl(BorrowerRepository borrowerRepository, BookRepository bookRepository, ModelMapper modelMapper) {
        this.borrowerRepository = borrowerRepository;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BorrowerDTO registerBorrower(BorrowerDTO borrowerDTO) {
        // Check if a borrower with the same email already exists
        if (borrowerRepository.existsByEmail(borrowerDTO.getEmail())) {
            throw new DuplicateBorrowerEmailException("A borrower with this email already exists.");
        }

        Borrower borrower = modelMapper.map(borrowerDTO, Borrower.class);
        Borrower savedBorrower = borrowerRepository.save(borrower);
        return modelMapper.map(savedBorrower, BorrowerDTO.class);
    }
    @Override
    public void borrowBook(Long borrowerId, Long bookId) {
        // Retrieve borrower from repository or throw exception if not found
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + borrowerId));

        // Retrieve book from repository or throw exception if not found
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        // Check if the book is already borrowed (example logic)
        if (book.getBorrower() != null) {
            throw new BookAlreadyBorrowedException("Book is already borrowed");
        }

        // Logic to mark the book as borrowed by the borrower
        book.setBorrower(borrower);

        // Save the updated book entity
        bookRepository.save(book);
    }

    @Override
    public void returnBook(Long borrowerId, Long bookId) {
        // Retrieve borrower from repository or throw exception if not found
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + borrowerId));

        // Retrieve book from repository or throw exception if not found
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        // Additional logic to ensure the book was borrowed by the borrower
        if ( book.getBorrower() ==null || !book.getBorrower().equals(borrower)) {
            throw new BookNotBorrowedException("Book was not borrowed by this borrower");
        }

        // Logic to mark the book as returned
        book.setBorrower(null);

        // Save the updated book entity
        bookRepository.save(book);
    }

    @Override
    public List<BorrowerDTO> getAllBorrowers() {
        return borrowerRepository.findAll().stream()
                .map(borrower -> modelMapper.map(borrower, BorrowerDTO.class))
                .toList();
    }

    @Override
    public BorrowerDTO getBorrowerById(Long borrowerId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + borrowerId));
        return modelMapper.map(borrower, BorrowerDTO.class);
    }

}