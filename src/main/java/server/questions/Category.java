package server.questions;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase encargada de almacenar las categorías de las preguntas
 * consta con la implementación de hibernate.
 *
 * @author David Puerto Cuenca
 * @version 1.0
 */
@Entity
@Getter @Setter  @NoArgsConstructor
public class Category {
    @Id
    private String name;
    private String color;

    public Category(String name, String color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Category name: '" + name + '\'' +
                " Color: '" + color + '\'' ;
    }

}
