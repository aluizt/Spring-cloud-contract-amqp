package br.com.rabbitspringcloudcontractprodutor.util;


import br.com.rabbitspringcloudcontractprodutor.model.Invoice;
import br.com.rabbitspringcloudcontractprodutor.model.User;

public class Data {
    public static User getUser(){
        return User.builder()
                .name("João da Silva ")
                .address("Rua Figueira ")
                .number(318)
                .build();
    }

    public static Invoice getInvoice(){
        return Invoice.builder()
                .number(199887)
                .price(125.00)
                .build();
    }
}
