package be.ucll.ipminor341t.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RegattaRepository extends JpaRepository<Regatta, Long> {

    Page<Regatta> findAllByDateBetweenAndCategoryContainsIgnoreCase(Date startDate, Date endDate, String category, Pageable pageable);

    Page<Regatta> findAllByCategoryContainsIgnoreCase(String category, Pageable pageable);

    Page<Regatta> findAllByDateAfterAndCategoryContainsIgnoreCase(Date startDate, String category, Pageable pageable);

    Page<Regatta> findAllByDateBeforeAndCategoryContainsIgnoreCase(Date endDate, String category, Pageable pageable);

    List<Regatta> findAllByTeamsId(Long id);

}

