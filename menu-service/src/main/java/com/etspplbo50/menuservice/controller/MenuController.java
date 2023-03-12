package com.etspplbo50.menuservice.controller;

import com.etspplbo50.menuservice.dto.MenuRequest;
import com.etspplbo50.menuservice.dto.MenuResponse;
import com.etspplbo50.menuservice.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuResponse createMenu(@RequestBody MenuRequest menuRequest) {
        return menuService.createMenu(menuRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MenuResponse getMenu(@PathVariable("id") String id) {
        return menuService.getMenu(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<MenuResponse> getAllMenu() {
        return menuService.getAllMenu();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MenuResponse updateMenu(@PathVariable("id") String id, @RequestBody MenuRequest menuRequest) {
        return menuService.updateMenu(id, menuRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteMenu(@PathVariable("id") String id) {
        return menuService.deleteMenu(id);
    }
}
