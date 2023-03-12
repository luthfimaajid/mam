package com.etspplbo50.inventoryservice.Controller;

import com.etspplbo50.inventoryservice.DTO.InventoryRequest;
import com.etspplbo50.inventoryservice.DTO.InventoryResponse;
import com.etspplbo50.inventoryservice.Service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse createInventory(@RequestBody InventoryRequest inventoryRequest) {
        return inventoryService.createInventory(inventoryRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponse getInventory(@PathVariable("id") String id) {
        return inventoryService.getInventory(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> getAllInventory() {
        return inventoryService.getAllInventory();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponse updateInventory(@PathVariable("id") String id, @RequestBody InventoryRequest inventoryRequest) {
        return inventoryService.updateInventory(id, inventoryRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteInventory(@PathVariable("id") String id) {
        return inventoryService.deleteInventory(id);
    }
}
