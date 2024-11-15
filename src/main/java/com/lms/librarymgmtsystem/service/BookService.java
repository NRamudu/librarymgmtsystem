// BookService.java
package com.lms.librarymgmtsystem.service;

import com.lms.librarymgmtsystem.dto.BookDTO;
import java.util.List;

public interface BookService {
    BookDTO registerBook(BookDTO bookDTO);
    List<BookDTO> getAllBooks();

    BookDTO getBookById(Long bookId);
}
