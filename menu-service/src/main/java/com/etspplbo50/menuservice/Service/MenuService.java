package com.etspplbo50.menuservice.Service;

import com.etspplbo50.menuservice.DTO.MenuRequest;
import com.etspplbo50.menuservice.DTO.MenuResponse;
import com.etspplbo50.menuservice.Model.Menu;
import com.etspplbo50.menuservice.Repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    public MenuResponse createMenu(MenuRequest menuRequest) {
        Menu menu = Menu.builder()
                .name(menuRequest.getName())
                .description(menuRequest.getDescription())
                .category(menuRequest.getCategory())
                .price(menuRequest.getPrice())
                .build();

        menuRepository.save(menu);

        return mapMenuToMenuResponse(menu);
    }

    public List<MenuResponse> getAllMenu() {
        List<Menu> menuList = menuRepository.findAll();

        return menuList.stream().map(menu -> mapMenuToMenuResponse(menu)).toList();
    }

    public MenuResponse getMenu(String id) {
        Optional<Menu> menu = menuRepository.findById(id);

        if (menu.isPresent()) {
            return mapMenuToMenuResponse(menu.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found");
        }
    }

    public MenuResponse updateMenu(String id, MenuRequest menuRequest) {
        Optional<Menu> menu = menuRepository.findById(id);

        if (menu.isPresent()) {
            Menu newMenu = menu.get();

            newMenu.setName(menuRequest.getName());
            newMenu.setDescription(menuRequest.getDescription());
            newMenu.setCategory(menuRequest.getCategory());
            newMenu.setPrice(menuRequest.getPrice());

            menuRepository.save(newMenu);
            return mapMenuToMenuResponse(newMenu);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found");
        }
    }

    public String deleteMenu(String id) {
        Optional<Menu> menu = menuRepository.findById(id);

        if (menu.isPresent()) {
            menuRepository.deleteById(id);
            return "Menu deleted";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found");
        }
    }

    private MenuResponse mapMenuToMenuResponse(Menu menu) {
        return MenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .description(menu.getDescription())
                .category(menu.getCategory())
                .price(menu.getPrice())
                .build();
    }
}
