package org.rayala;

import java.util.Arrays;

import org.rayala.domain.model.Product;
import org.rayala.ps.Publisher;
import org.rayala.ps.Subscriber;

public class Main {
    public static void main(String[] args) {
        

        String channel = "testChannel";

        Product product = Product.builder()
            .id(1)
            .name("iPhone 16 max")
            .price(1520.50)
            .build();

        // Iniciar el suscriptor
        Subscriber<Product> subscriber = new Subscriber<>(Product.class);
        subscriber.subscribe(channel, Arrays.asList(
            (Product data) -> {
                System.out.println(String.format("handler for channel: %s with data: %s", channel, data));
            }
        ));

        // Iniciar el publicador
        Publisher<Product> publisher = new Publisher<Product>();
        
        // Publicar mensajes en el canal
        for (int i = 0; i < 3; i++) {
            product.setPrice(product.getPrice() + 130.5);
            publisher.publish(channel, product);
        }

        // Cerrar el pool de threads al finalizar la ejecución
        // Subscriber.shutdown();
        System.out.println("App finished");
    }
}