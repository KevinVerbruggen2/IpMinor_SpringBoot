package be.ucll.ipminor341t.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByCategoryIgnoreCase(String category);

    List<Team> findByPassengersLessThanOrderByPassengersDesc(int passengers);

    Team findByNameAndCategory(String name, String category);
}