package com.afci.service;

import com.afci.data.Serial;
import com.afci.data.SerialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SerialService {

    @Autowired
    private SerialRepository serialRepository;

    public List<Serial> getAllSerials() {
        return serialRepository.findAll();
    }

    public Optional<Serial> getSerialById(Long id) {
        return serialRepository.findById(id);
    }

    public Serial createSerial(Serial serial) {
        return serialRepository.save(serial);
    }

    public Serial updateSerial(Serial serial) {
        if (serialRepository.existsById(serial.getId())) {
            return serialRepository.save(serial);
        }
        throw new RuntimeException("Série non trouvée avec l'ID : " + serial.getId());
    }

    public void deleteSerial(Long id) {
        serialRepository.deleteById(id);
    }

    public List<Serial> findByTitle(String title) {
        return serialRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Serial> findByAuthorId(Long authorId) {
        return serialRepository.findByAuthor_Id(authorId);
    }
}