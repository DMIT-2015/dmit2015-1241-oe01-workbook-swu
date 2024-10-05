package dmit2015.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CircleTest {

    @Test
    void area_whenCircleIsCreatedWithSpecificRadius_shouldReturnsCorrectArea() {
// Arrange and Act
        Circle circle1 = new Circle(5);
// Assert
        assertEquals(78.54, circle1.area(), 0.005);
    }
    @Test
    void diameter_whenCircleIsCreatedWithSpecificRadius_shouldReturnsCorrectArea() {
// Arrange and Act
        Circle circle1 = new Circle(5);
// Assert
        assertEquals(10, circle1.diameter());
    }

    @Test
    void circumference_whenCircleIsCreatedWithSpecificRadius_shouldReturnsCorrectArea() {
// Arrange and Act
        Circle circle1 = new Circle(5);
// Assert
        assertEquals(31.42, circle1.circumference(), 0.005);
    }
    @Test
    void allMethods_whenCircleIsCreatedWithSpecificRadius_shouldReturnsCorrectArea() {
// Arrange and Act
        Circle circle1 = new Circle(5);
// Assert
        assertAll("all methods",
                () -> assertEquals(78.54, circle1.area(), 0.01),
                () -> assertEquals(10, circle1.diameter()),
                () -> assertEquals(31.42, circle1.circumference(), 0.005)
        );
    }

    @Test
    void setRadius_whenAssignedInvalidRadius_shouldThrowsRuntimeException() {
// Arrange
        Circle circle1 = new Circle();
// Act
        var exception = assertThrows(
                RuntimeException.class,
                () -> circle1.setRadius(-10));
// Assert
        assertEquals("Radius value must be greater than 0", exception.getMessage());
    }
}