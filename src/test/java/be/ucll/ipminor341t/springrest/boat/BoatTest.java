package be.ucll.ipminor341t.springrest.boat;

import be.ucll.ipminor341t.domain.Boat;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class BoatTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }


    //Happy Case
    @Test
    public void givenValidBoat_shouldHaveNoViolations() {
        Boat boat = BoatBuilder.aTeamA().build();

        Set<ConstraintViolation<Boat>> violations = validator.validate(boat);

        assertTrue(violations.isEmpty());
    }

    //Unhappy Case
    @Test
    public void givenBoatWithEmptyName_shouldDetectInvalidNameError() {
        Boat boat = BoatBuilder.anInvalidTeamWithNoName().build();

        Set<ConstraintViolation<Boat>> violations = validator.validate(boat);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Boat> violation = violations.iterator().next();
        assertEquals("name.missing", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    //Unhappy Case
    @Test
    public void givenBoatWithShortName_shouldDetectShortNameError() {
        Boat boat = BoatBuilder.anInvalidTeamWithShortName().build();

        Set<ConstraintViolation<Boat>> violations = validator.validate(boat);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Boat> violation = violations.iterator().next();
        assertEquals("name.too.short", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals(boat.getName(), violation.getInvalidValue());
    }

    //Unhappy Case
    @Test
    public void givenBoatWithEmptyEmail_shouldDetectMissingEmailError() {
        Boat boat = BoatBuilder.anInvalidTeamWithNoEmail().build();

        Set<ConstraintViolation<Boat>> violations = validator.validate(boat);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Boat> violation = violations.iterator().next();
        assertEquals("email.missing", violation.getMessage());
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    //Unhappy Case
    @Test
    public void givenBoatWithInvalidEmail_shouldDetectInvalidEmailError() {
        Boat boat = BoatBuilder.anInvalidTeamWithInvalidEmail().build();

        Set<ConstraintViolation<Boat>> violations = validator.validate(boat);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Boat> violation = violations.iterator().next();
        assertEquals("email.not.valid", violation.getMessage());
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals(boat.getEmail(), violation.getInvalidValue());
    }

    //Unhappy Case
    @Test
    public void givenBoatWithEmptyInsurance_shouldDetectMissingInsuranceError() {
        Boat boat = BoatBuilder.anInvalidTeamWithNoInsurance().build();

        Set<ConstraintViolation<Boat>> violations = validator.validate(boat);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Boat> violation = violations.iterator().next();
        assertEquals("insurance.number.missing", violation.getMessage());
        assertEquals("insurance", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    //Unhappy Case
    @Test
    public void givenBoatWithInvalidInsurance_shouldDetectInvalidInsuranceError() {
        Boat boat = BoatBuilder.anInvalidTeamWithInvalidInsurance().build();

        Set<ConstraintViolation<Boat>> violations = validator.validate(boat);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Boat> violation = violations.iterator().next();
        assertEquals("insurance.number.invalid", violation.getMessage());
        assertEquals("insurance", violation.getPropertyPath().toString());
        assertEquals(boat.getInsurance(), violation.getInvalidValue());
    }

    //Unhappy Case
    @Test
    public void givenBoatWithNegativeLength_shouldDetectNegativeLengthError() {
        Boat boat = BoatBuilder.anInvalidTeamWithNegativeLength().build();

        Set<ConstraintViolation<Boat>> violations = validator.validate(boat);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Boat> violation = violations.iterator().next();
        assertEquals("length.must.be.positive", violation.getMessage());
        assertEquals("length", violation.getPropertyPath().toString());
        assertEquals(boat.getLength(), violation.getInvalidValue());
    }

    //Unhappy Case
    @Test
    public void givenBoatWithNegativeWidth_shouldDetectNegativeWidthError() {
        Boat boat = BoatBuilder.anInvalidTeamWithNegativeWidth().build();

        Set<ConstraintViolation<Boat>> violations = validator.validate(boat);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Boat> violation = violations.iterator().next();
        assertEquals("width.must.be.positive", violation.getMessage());
        assertEquals("width", violation.getPropertyPath().toString());
        assertEquals(boat.getWidth(), violation.getInvalidValue());
    }

    //Unhappy Case
    @Test
    public void givenBoatWithNegativeHeight_shouldDetectNegativeHeightError() {
        Boat boat = BoatBuilder.anInvalidTeamWithNegativeHeight().build();

        Set<ConstraintViolation<Boat>> violations = validator.validate(boat);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Boat> violation = violations.iterator().next();
        assertEquals("height.must.be.positive", violation.getMessage());
        assertEquals("height", violation.getPropertyPath().toString());
        assertEquals(boat.getHeight(), violation.getInvalidValue());
    }

    private boolean containsViolation(Set<ConstraintViolation<Boat>> violations, String message) {
        for (ConstraintViolation<Boat> violation : violations) {
            if (violation.getMessage().equals(message)) {
                return true;
            }
        }
        return false;
    }

    private ConstraintViolation<Boat> getViolation(Set<ConstraintViolation<Boat>> violations, String message) {
        for (ConstraintViolation<Boat> violation : violations) {
            if (violation.getMessage().equals(message)) {
                return violation;
            }
        }
        return null;
    }
}
