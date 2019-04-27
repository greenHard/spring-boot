package com.example.SpringBootWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class JSONRestController {
    @Bean
    public Person currentPerson() {
        Person person = new Person("zhangsan", "shanghaipudong");
        return person;
    }

    @Autowired
    @Qualifier("currentPerson")
    private Person person;


    @GetMapping(path = "/json/person",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Person getPerson() {
        Person person = new Person("zhangsan", "shanghaipudong");
        person.add(linkTo(methodOn(JSONRestController.class).setPersonName(person.getName())).withSelfRel());
        person.add(linkTo(methodOn(JSONRestController.class).setPersonAddress(person.getAddress())).withSelfRel());
        return person;
    }

    // setName
    @PostMapping(path = "/json/person/set/name",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Person setPersonName(@RequestParam String name) {
        person.setName(name);
        return person;
    }

    // setAddress
    @GetMapping(path = "/json/person/set/address",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Person setPersonAddress(@RequestParam String age) {
        person.setAddress("beijing");
        return person;
    }




}
