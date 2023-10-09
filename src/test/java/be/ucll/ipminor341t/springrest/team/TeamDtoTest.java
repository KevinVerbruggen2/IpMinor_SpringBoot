package be.ucll.ipminor341t.springrest.team;

import be.ucll.ipminor341t.web.TeamDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class TeamDtoTest {

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
    public void givenValidTeam_shouldHaveNoViolations() {
        //given
        TeamDto team = TeamDtoBuilder.aTeamA().build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(team);

        //then
        assertTrue(violations.isEmpty());
    }

    //Unhappy Case
    @Test
    public void givenTeamWithEmptyName_shouldDetectInvalidNameError() {
        //given
        TeamDto team = TeamDtoBuilder.anInvalidTeamWithNoName().build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(team);

        //then
        assertEquals(violations.size(), 1);
        assertTrue(containsViolation(violations, "name.missing"));
        assertEquals("name", getViolation(violations, "name.missing").getPropertyPath().toString());
        assertEquals("", getViolation(violations, "name.missing").getInvalidValue());
    }

    //Unhappy Case
    @Test
    public void givenTeamWithNameWith3Characters_shouldDetectInvalidNameError() {
        //given
        TeamDto team = TeamDtoBuilder.anInvalidTeamWithNameWith3Characters().build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(team);

        //then
        assertEquals(violations.size(), 1);
        assertTrue(containsViolation(violations, "name.too.short"));
        assertEquals("name", getViolation(violations, "name.too.short").getPropertyPath().toString());
        assertEquals("ABC", getViolation(violations, "name.too.short").getInvalidValue());
    }

    //Unhappy Case
    @Test
    public void givenTeamWithEmptyCategory_ShouldDetectInvalidCategoryError() {
        //given
        TeamDto team = TeamDtoBuilder.anInvalidTeamWithNoCategory().build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(team);

        //then
        assertEquals(violations.size(), 1);
        assertTrue(containsViolation(violations, "category.missing"));
        assertEquals("category", getViolation(violations, "category.missing").getPropertyPath().toString());
        assertEquals("", getViolation(violations, "category.missing").getInvalidValue());
    }

    //Unhappy Case
    @Test
    public void givenTeamWithCategoryShorterThan7Characters_ShouldDetectInvalidCategoryError() {
        //given
        TeamDto team = TeamDtoBuilder.anInvalidTeamWithCategoryShorterThan7Characters().build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(team);

        //then
        assertEquals(violations.size(), 1);
        assertTrue(containsViolation(violations, "category.pattern"));
        assertEquals("category", getViolation(violations, "category.pattern").getPropertyPath().toString());
    }

    //Unhappy Case
    @Test
    public void givenTeamWithNumberOfCrewLessThan1_ShouldDetectInvalidNumberOfCrewError() {
        //given
        TeamDto team = TeamDtoBuilder.anInvalidTeamWithNumberOfCrewLessThan1().build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(team);

        //then
        assertEquals(violations.size(), 1);
        assertTrue(containsViolation(violations, "passengers.min"));
        assertEquals("passengers", getViolation(violations, "passengers.min").getPropertyPath().toString());
        assertEquals(0, getViolation(violations, "passengers.min").getInvalidValue());
    }

    //Unhappy Case
    @Test
    public void givenTeamWithNumberOfCrewMoreThan12_ShouldDetectInvalidNumberOfCrewError(){
        //given
        TeamDto team = TeamDtoBuilder.anInvalidTeamWithNumberOfCrewMoreThan12().build();

        //when
        Set<ConstraintViolation<TeamDto>> violations = validator.validate(team);

        //then
        assertTrue(containsViolation(violations, "passengers.max"));
        assertEquals("passengers", getViolation(violations, "passengers.max").getPropertyPath().toString());
        assertEquals(13, getViolation(violations, "passengers.max").getInvalidValue());
    }

    public boolean containsViolation(Set<ConstraintViolation<TeamDto>> violations, String message) {
        for (ConstraintViolation<TeamDto> violation : violations) {
            if (violation.getMessage().equals(message)) {
                return true;
            }
        }
        return false;
    }

    public ConstraintViolation<TeamDto> getViolation(Set<ConstraintViolation<TeamDto>> violations, String message) {
        for (ConstraintViolation<TeamDto> violation : violations) {
            if (violation.getMessage().equals(message)) {
                return violation;
            }
        }
        return null;
    }
}
