package be.ucll.ipminor341t.domain.service;

import be.ucll.ipminor341t.domain.Boat;
import be.ucll.ipminor341t.domain.Storage;
import be.ucll.ipminor341t.domain.StorageRepository;
import be.ucll.ipminor341t.generic.ServiceException;
import be.ucll.ipminor341t.web.StorageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StorageService {
    @Autowired
    private StorageRepository storageRepository;

    public Page<Storage> getStorages(String page,String numberOfItems, Sort sort) {
        try {
            Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(numberOfItems), sort);
            return sort.getOrderFor("height") == null ? storageRepository.findAll(pageable) : storageRepository.findStoragesOrderByHeight(PageRequest.of(Integer.parseInt(page), Integer.parseInt(numberOfItems)));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Number of items should be a number");
        }
    }

    public Storage getStorage(Long id) {
        return storageRepository.findById(id).orElseThrow(() -> new ServiceException("findError","Storage with id " + id + " not found"));
    }

    public Storage createStorage(StorageDto dto) {
        Storage storage = new Storage();

        storage.setName(dto.getName());
        storage.setPlace(dto.getPlace());
        storage.setAvailableSpace(dto.getAvailableSpace());
        storage.setHeight(dto.getHeight());

        return storageRepository.save(storage);
    }

    public Storage updateStorage(Long id, StorageDto dto) {
        Storage storage = getStorage(id);

        if(storage.getBoats().size() > 0) throw new ServiceException("deleteError","Storage with id " + id + " still has boats in it");
        storage.setName(dto.getName());
        storage.setPlace(dto.getPlace());
        storage.setAvailableSpace(dto.getAvailableSpace());
        storage.setHeight(dto.getHeight());

        return storageRepository.save(storage);
    }

    public void deleteStorage(long id) {
        Storage storage = getStorage(id);
        if(storage.getBoats().size() > 0) throw new ServiceException("deleteError","Storage with id " + id + " still has boats in it");
        storageRepository.delete(getStorage(id));
    }

    public Page<Storage> getStoragesByName(String searchValue, String numberOfItems, String pageNumber, Sort sort) {
        Pageable page = PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(numberOfItems), sort);
        return storageRepository.findStoragesByNameContainsIgnoreCase(searchValue, page);
    }

    public Boat addBoatToStorage(Long storageId, Boat boat) {

        Storage storage = getStorage(storageId);

        storage.addBoat(boat);
        storageRepository.save(storage);
        return boat;
    }

    public Boat removeBoatFromStorage(Long storageId, Boat boat) {
        Storage storage = getStorage(storageId);

        storage.removeBoat(boat);
        storageRepository.save(storage);
        return boat;
    }

    public List<Boat> getBoatsFromStorage(Long storageId) {
        Storage storage = getStorage(storageId);
        return new ArrayList<>(storage.getBoats());
    }
}
