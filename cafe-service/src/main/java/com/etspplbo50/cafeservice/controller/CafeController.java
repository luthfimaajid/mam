package com.etspplbo50.cafeservice.controller;

import com.etspplbo50.cafeservice.dto.CafeRequest;
import com.etspplbo50.cafeservice.dto.CafeResponse;
import com.etspplbo50.cafeservice.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cafe")
@RequiredArgsConstructor
public class CafeController {
    private final CafeService cafeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CafeResponse createCafe(@RequestBody CafeRequest cafeRequest) {
        return cafeService.createCafe(cafeRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CafeResponse getCafe(@PathVariable("id") String id) {
        return cafeService.getCafe(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<CafeResponse> getAllCafe() {
        return cafeService.getAllCafe();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CafeResponse updateCafe(@PathVariable("id") String id, @RequestBody CafeRequest cafeRequest) {
        return cafeService.updateCafe(id, cafeRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteCafe(@PathVariable("id") String id) {
        return cafeService.deleteCafe(id);
    }
}

