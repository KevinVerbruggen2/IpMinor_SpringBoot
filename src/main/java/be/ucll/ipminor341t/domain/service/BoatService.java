package be.ucll.ipminor341t.domain.service;

import be.ucll.ipminor341t.domain.Boat;
import be.ucll.ipminor341t.domain.BoatRepository;
import be.ucll.ipminor341t.domain.Storage;
import be.ucll.ipminor341t.generic.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoatService {

    @Autowired
    private BoatRepository boatRepository;

    public List<Boat> getBoats() {
        return boatRepository.findAll();
    }

    public Iterable<Boat> getBoatsByInsurance(String insurance) {
        if(insurance.isBlank() || !insurance.matches("^[a-zA-Z0-9]{10}$") ) throw new ServiceException("invalidInsurance", "Please provide a valid insurance number");
        return boatRepository.findAllByInsurance(insurance);
    }

    public Iterable<Boat> getBoatsByHeightAndWidth(double height, double width) {
        if(height < 0 || width < 0) throw new ServiceException("invalidHeightOrWidth", "Both height and width must be positive");
        return boatRepository.findAllByHeightAndWidth(height, width);
    }

    public Boat createBoat(Boat boat) {
        return boatRepository.save(boat);
    }

    public Boat getBoat(Long id) {
        return boatRepository.findById(id).orElseThrow(() -> new ServiceException("updateError","Boat with id " + id + " not found"));
    }

    public Boat updateBoat(Long id, Boat boat) {
        Boat updatedBoat = getBoat(id);

        Storage storage = updatedBoat.getStorage();
        if(storage != null) throw new ServiceException("updateError", "Boat is stored in a storage so it cannot be updated");

        updatedBoat.setInsurance(boat.getInsurance());
        updatedBoat.setHeight(boat.getHeight());
        updatedBoat.setName(boat.getName());
        updatedBoat.setLength(boat.getLength());
        updatedBoat.setWidth(boat.getWidth());
        updatedBoat.setEmail(boat.getEmail());

        return boatRepository.save(updatedBoat);
    }

    public Boat deleteBoat(Long id) throws ServiceException {
        Boat boat = getBoat(id);

        Storage storage = boat.getStorage();
        if(storage != null) throw new ServiceException("updateError", "Boat is stored in a storage so it cannot be updated");

        boatRepository.delete(boat);
        return boat;
    }
}
