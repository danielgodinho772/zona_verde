package aep5s.zonaverde.controller;

import aep5s.zonaverde.domain.entity.CheckIn;
import aep5s.zonaverde.dto.CheckInDTO;
import aep5s.zonaverde.service.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkins")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CheckInController {
    private final CheckInService service;

    @PostMapping
    public ResponseEntity<CheckIn> realizar(@RequestBody CheckInDTO dto) {
        return ResponseEntity.ok(service.realizar(dto));
    }
}
