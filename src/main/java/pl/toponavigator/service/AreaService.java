package pl.toponavigator.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.toponavigator.model.Area;

import java.util.List;

@Service
public class AreaService {

    public ResponseEntity<Object> getAllAreas() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
