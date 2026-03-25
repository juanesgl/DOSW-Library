package edu.eci.dosw.tdd.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    void isNullOrEmpty_ShouldReturnTrue_WhenNull() {
        assertTrue(ValidationUtil.isNullOrEmpty(null));
    }

    @Test
    void isNullOrEmpty_ShouldReturnTrue_WhenEmpty() {
        assertTrue(ValidationUtil.isNullOrEmpty(""));
    }

    @Test
    void isNullOrEmpty_ShouldReturnTrue_WhenOnlySpaces() {
        assertTrue(ValidationUtil.isNullOrEmpty("   "));
    }

    @Test
    void isNullOrEmpty_ShouldReturnFalse_WhenValidString() {
        assertFalse(ValidationUtil.isNullOrEmpty("hello"));
    }

    @Test
    void isNullOrEmpty_ShouldReturnFalse_WhenStringWithSpacesAndContent() {
        assertFalse(ValidationUtil.isNullOrEmpty("  hello  "));
    }
}
