package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.enums.Messages;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepo;

    @PostMapping("/products")
    public ResponseEntity<ProductModel> registerProduct(@RequestBody @Valid ProductRecordDto productRecordDto){
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepo.save(productModel));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAllProducts(){
        List<ProductModel> productsList = productRepo.findAll();

        if (!productsList.isEmpty()){
            for (ProductModel product : productsList){
                UUID id = product.getIdProduct();
                product.add(linkTo(methodOn(ProductController.class).getProductById(id.toString()))
                        .withSelfRel().withRel("Detalhes do Produto"));
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(productsList);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable(value = "id") String idString){
        UUID id;
        try {
            id = UUID.fromString(idString);
            Optional<ProductModel> product0 = productRepo.findById(id);

            return product0.<ResponseEntity<Object>>map(
                    productModel -> ResponseEntity.status(HttpStatus.OK).body(productModel)).orElseGet(()
                    -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Messages.ERRO_PRODUTO_NAO_ENCONTRADO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Messages.ERRO_PRODUTO_NAO_ENCONTRADO);
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") String idString,
                                                @RequestBody @Valid ProductRecordDto productRecordDto){
        UUID id;
        try {
            id = UUID.fromString(idString);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Messages.ERRO_PRODUTO_NAO_ENCONTRADO);
        }

        Optional<ProductModel> product0 = productRepo.findById(id);

        if (product0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Messages.ERRO_PRODUTO_NAO_ENCONTRADO);
        }

        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.OK).body(productRepo.save(productModel));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") String idString){
        UUID id;
        try {
            id = UUID.fromString(idString);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Messages.ERRO_PRODUTO_NAO_ENCONTRADO);
        }

        Optional<ProductModel> product0 = productRepo.findById(id);

        if (product0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Messages.ERRO_PRODUTO_NAO_ENCONTRADO);
        }

        productRepo.delete(product0.get());
        return ResponseEntity.status(HttpStatus.OK).body(Messages.MENSAGEM_PRODUTO_DELETADO);
    }
}
