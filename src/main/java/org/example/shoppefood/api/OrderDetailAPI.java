package org.example.shoppefood.api;

import org.example.shoppefood.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderDetailAPI {
    @Autowired
    OrderDetailRepository orderDetailRepository;

    @GetMapping("/reports/products")
    public List<Object[]> reportProducts() {
        return orderDetailRepository.repo();
    }

    @GetMapping("/reports/category")
    public List<Object[]> reportCategory() {
        return orderDetailRepository.repoWhereCategory();
    }

    @GetMapping("/reports/years")
    public List<Object[]> reportYears() {
        return orderDetailRepository.repoWhereYear();
    }

    @GetMapping("/reports/months")
    public List<Object[]> reportMonths() {
        return orderDetailRepository.repoWhereMonth();
    }

    @GetMapping("/reports/quater")
    public List<Object[]> reportQuater() {
        return orderDetailRepository.repoWhereQUARTER();
    }

    @GetMapping("/reports/customer")
    public List<Object[]> reportCustomer() {
        return orderDetailRepository.reportCustommer();
    }
}
