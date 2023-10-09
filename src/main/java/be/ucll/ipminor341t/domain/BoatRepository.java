package be.ucll.ipminor341t.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoatRepository extends JpaRepository<Boat, Long> {


    List<Boat> findAllByInsurance(String insurance);

    List<Boat> findAllByHeightAndWidth(double height, double width);

}

