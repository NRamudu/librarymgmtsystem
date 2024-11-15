// BorrowerRepository.java
package com.lms.librarymgmtsystem.repository;

import com.lms.librarymgmtsystem.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
    boolean existsByEmail(String email);
}
