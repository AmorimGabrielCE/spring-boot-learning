package com.example.springboot.controllers;

import com.example.springboot.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductController {

    @Autowired
    ProductRepository productRepo;
}
