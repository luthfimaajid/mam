package com.etspplbo50.cafeservice.service;


import com.etspplbo50.cafeservice.dto.CafeRequest;
import com.etspplbo50.cafeservice.dto.CafeResponse;
import com.etspplbo50.cafeservice.model.Cafe;
import com.etspplbo50.cafeservice.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CafeService {
    private final CafeRepository cafeRepository;

    public CafeResponse createCafe(CafeRequest cafeRequest) {
        Cafe cafe = Cafe.builder()
                .name(cafeRequest.getName())
                .description(cafeRequest.getDescription())
                .address(cafeRequest.getAddress())
                .latitude(cafeRequest.getLatitude())
                .longitude(cafeRequest.getLongitude())
                .build();

        cafeRepository.save(cafe);

        return mapModelToResponse(cafe);
    }

    public List<CafeResponse> getAllCafe() {
        List<Cafe> cafeList = cafeRepository.findAll();

        return cafeList.stream().map(cafe -> mapModelToResponse(cafe)).toList();
    }

    public CafeResponse getCafe(String id) {
        Optional<Cafe> cafe = cafeRepository.findById(id);

        if (cafe.isPresent()) {
            return mapModelToResponse(cafe.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cafe not found");
        }
    }

    public CafeResponse updateCafe(String id, CafeRequest cafeRequest) {
        Optional<Cafe> cafe = cafeRepository.findById(id);

        if (cafe.isPresent()) {
            Cafe newCafe = cafe.get();

            newCafe.setName(cafeRequest.getName());
            newCafe.setDescription(cafeRequest.getDescription());
            newCafe.setAddress(cafeRequest.getAddress());
            newCafe.setLatitude(cafeRequest.getLatitude());
            newCafe.setLongitude(cafeRequest.getLongitude());

            cafeRepository.save(newCafe);
            return mapModelToResponse(newCafe);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cafe not found");
        }
    }

    public String deleteCafe(String id) {
        Optional<Cafe> cafe = cafeRepository.findById(id);

        if (cafe.isPresent()) {
            cafeRepository.deleteById(id);
            return "Cafe deleted";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cafe not found");
        }
    }

    private CafeResponse mapModelToResponse(Cafe cafe) {
        return CafeResponse.builder()
                .id(cafe.getId())
                .name(cafe.getName())
                .description(cafe.getDescription())
                .address(cafe.getAddress())
                .latitude(cafe.getLatitude())
                .longitude(cafe.getLongitude())
                .build();
    }
}
