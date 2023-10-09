package be.ucll.ipminor341t.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {

    @Query("SELECT p FROM Storage p ORDER BY CAST(p.height AS double) DESC")
    Page<Storage> findStoragesOrderByHeight(Pageable pageable);

    Page<Storage> findStoragesByNameContainsIgnoreCase(String name, Pageable pageable);

}