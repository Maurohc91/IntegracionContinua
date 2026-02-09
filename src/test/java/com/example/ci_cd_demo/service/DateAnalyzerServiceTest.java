package com.example.ci_cd_demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.ci_cd_demo.model.DateAnalysisResult;

/**
 * Pruebas unitarias para DateAnalyzerService.
 * Verifica la validación de fechas, cálculo de años bisiestos,
 * signos del zodiaco occidental y signos del zodiaco chino.
 */
class DateAnalyzerServiceTest {

    private DateAnalyzerService service;

    @BeforeEach
    void setUp() {
        service = new DateAnalyzerService();
    }

    @Nested
    @DisplayName("Pruebas de validación de fechas")
    class DateValidationTests {

        @Test
        @DisplayName("Fecha válida debe retornar resultado exitoso")
        void validDate_shouldReturnSuccess() {
            DateAnalysisResult result = service.analyzeDate(15, 6, 2003);

            assertTrue(result.isValidDate());
            assertNull(result.getErrorMessage());
            assertEquals(15, result.getDay());
            assertEquals(6, result.getMonth());
            assertEquals(2003, result.getYear());
        }

        @ParameterizedTest
        @DisplayName("Años fuera de rango deben retornar error")
        @ValueSource(ints = { 1999, 2006, 1900, 2100 })
        void yearOutOfRange_shouldReturnError(int year) {
            DateAnalysisResult result = service.analyzeDate(1, 1, year);

            assertFalse(result.isValidDate());
            assertNotNull(result.getErrorMessage());
            assertTrue(result.getErrorMessage().contains("2000"));
            assertTrue(result.getErrorMessage().contains("2005"));
        }

        @ParameterizedTest
        @DisplayName("Meses inválidos deben retornar error")
        @ValueSource(ints = { 0, 13, -1, 100 })
        void invalidMonth_shouldReturnError(int month) {
            DateAnalysisResult result = service.analyzeDate(1, month, 2003);

            assertFalse(result.isValidDate());
            assertTrue(result.getErrorMessage().contains("mes"));
        }

        @Test
        @DisplayName("Día 0 debe retornar error")
        void dayZero_shouldReturnError() {
            DateAnalysisResult result = service.analyzeDate(0, 5, 2003);

            assertFalse(result.isValidDate());
            assertTrue(result.getErrorMessage().contains("día"));
        }

        @Test
        @DisplayName("Día 32 en enero debe retornar error")
        void day32InJanuary_shouldReturnError() {
            DateAnalysisResult result = service.analyzeDate(32, 1, 2003);

            assertFalse(result.isValidDate());
        }

        @Test
        @DisplayName("Día 31 en abril debe retornar error (abril tiene 30 días)")
        void day31InApril_shouldReturnError() {
            DateAnalysisResult result = service.analyzeDate(31, 4, 2003);

            assertFalse(result.isValidDate());
        }

        @Test
        @DisplayName("29 de febrero en año no bisiesto debe retornar error")
        void feb29InNonLeapYear_shouldReturnError() {
            DateAnalysisResult result = service.analyzeDate(29, 2, 2003);

            assertFalse(result.isValidDate());
            assertTrue(result.getErrorMessage().contains("28"));
        }

        @Test
        @DisplayName("29 de febrero en año bisiesto debe ser válido")
        void feb29InLeapYear_shouldBeValid() {
            DateAnalysisResult result = service.analyzeDate(29, 2, 2004);

            assertTrue(result.isValidDate());
        }
    }

    @Nested
    @DisplayName("Pruebas de año bisiesto")
    class LeapYearTests {

        @ParameterizedTest
        @DisplayName("Años bisiestos deben ser identificados correctamente")
        @ValueSource(ints = { 2000, 2004 })
        void leapYears_shouldReturnTrue(int year) {
            assertTrue(service.isLeapYear(year));
        }

        @ParameterizedTest
        @DisplayName("Años no bisiestos deben ser identificados correctamente")
        @ValueSource(ints = { 2001, 2002, 2003, 2005 })
        void nonLeapYears_shouldReturnFalse(int year) {
            assertFalse(service.isLeapYear(year));
        }

        @Test
        @DisplayName("Año divisible por 100 pero no por 400 no es bisiesto")
        void year1900_shouldNotBeLeapYear() {
            assertFalse(service.isLeapYear(1900));
        }

        @Test
        @DisplayName("Año divisible por 400 es bisiesto")
        void year2000_shouldBeLeapYear() {
            assertTrue(service.isLeapYear(2000));
        }

        @Test
        @DisplayName("Resultado de fecha debe incluir información de año bisiesto")
        void analysisResult_shouldIncludeLeapYearInfo() {
            DateAnalysisResult leapResult = service.analyzeDate(1, 1, 2004);
            DateAnalysisResult nonLeapResult = service.analyzeDate(1, 1, 2003);

            assertTrue(leapResult.isLeapYear());
            assertFalse(nonLeapResult.isLeapYear());
        }
    }

    @Nested
    @DisplayName("Pruebas de zodiaco occidental")
    class WesternZodiacTests {

        @ParameterizedTest
        @DisplayName("Signos del zodiaco occidental deben ser correctos")
        @CsvSource({
                "1, 1, Capricornio",
                "19, 1, Capricornio",
                "20, 1, Acuario",
                "18, 2, Acuario",
                "19, 2, Piscis",
                "20, 3, Piscis",
                "21, 3, Aries",
                "19, 4, Aries",
                "20, 4, Tauro",
                "20, 5, Tauro",
                "21, 5, Géminis",
                "20, 6, Géminis",
                "21, 6, Cáncer",
                "22, 7, Cáncer",
                "23, 7, Leo",
                "22, 8, Leo",
                "23, 8, Virgo",
                "22, 9, Virgo",
                "23, 9, Libra",
                "22, 10, Libra",
                "23, 10, Escorpio",
                "21, 11, Escorpio",
                "22, 11, Sagitario",
                "21, 12, Sagitario",
                "22, 12, Capricornio",
                "31, 12, Capricornio"
        })
        void westernZodiac_shouldBeCorrect(int day, int month, String expectedSign) {
            String result = service.getWesternZodiac(day, month);
            assertEquals(expectedSign, result);
        }

        @Test
        @DisplayName("Análisis de fecha debe incluir signo occidental correcto")
        void analysisResult_shouldIncludeWesternZodiac() {
            DateAnalysisResult result = service.analyzeDate(25, 12, 2003);

            assertTrue(result.isValidDate());
            assertEquals("Capricornio", result.getWesternZodiac());
        }
    }

    @Nested
    @DisplayName("Pruebas de zodiaco chino")
    class ChineseZodiacTests {

        @ParameterizedTest
        @DisplayName("Signos del zodiaco chino por año deben ser correctos")
        @CsvSource({
                "2000, Dragón",
                "2001, Serpiente",
                "2002, Caballo",
                "2003, Cabra",
                "2004, Mono",
                "2005, Gallo"
        })
        void chineseZodiac_shouldBeCorrect(int year, String expectedSign) {
            String result = service.getChineseZodiac(year);
            assertEquals(expectedSign, result);
        }

        @Test
        @DisplayName("Año fuera de rango debe retornar 'Fuera de rango'")
        void yearOutOfRange_shouldReturnOutOfRange() {
            assertEquals("Fuera de rango", service.getChineseZodiac(1999));
            assertEquals("Fuera de rango", service.getChineseZodiac(2006));
        }

        @Test
        @DisplayName("Análisis de fecha debe incluir signo chino correcto")
        void analysisResult_shouldIncludeChineseZodiac() {
            DateAnalysisResult result = service.analyzeDate(15, 6, 2004);

            assertTrue(result.isValidDate());
            assertEquals("Mono", result.getChineseZodiac());
        }
    }

    @Nested
    @DisplayName("Pruebas de días máximos por mes")
    class MaxDaysInMonthTests {

        @ParameterizedTest
        @DisplayName("Meses con 31 días")
        @ValueSource(ints = { 1, 3, 5, 7, 8, 10, 12 })
        void monthsWith31Days(int month) {
            assertEquals(31, service.getMaxDaysInMonth(month, false));
        }

        @ParameterizedTest
        @DisplayName("Meses con 30 días")
        @ValueSource(ints = { 4, 6, 9, 11 })
        void monthsWith30Days(int month) {
            assertEquals(30, service.getMaxDaysInMonth(month, false));
        }

        @Test
        @DisplayName("Febrero en año no bisiesto tiene 28 días")
        void februaryNonLeapYear_has28Days() {
            assertEquals(28, service.getMaxDaysInMonth(2, false));
        }

        @Test
        @DisplayName("Febrero en año bisiesto tiene 29 días")
        void februaryLeapYear_has29Days() {
            assertEquals(29, service.getMaxDaysInMonth(2, true));
        }
    }

    @Nested
    @DisplayName("Pruebas de integración del modelo DateAnalysisResult")
    class DateAnalysisResultTests {

        @Test
        @DisplayName("toString() de fecha válida debe contener toda la información")
        void validDate_toStringShouldContainAllInfo() {
            DateAnalysisResult result = service.analyzeDate(15, 8, 2004);
            String str = result.toString();

            assertTrue(str.contains("15"));
            assertTrue(str.contains("08") || str.contains("8"));
            assertTrue(str.contains("2004"));
            assertTrue(str.contains("Sí")); // año bisiesto
            assertTrue(str.contains("Leo"));
            assertTrue(str.contains("Mono"));
        }

        @Test
        @DisplayName("toString() de fecha inválida debe contener mensaje de error")
        void invalidDate_toStringShouldContainError() {
            DateAnalysisResult result = service.analyzeDate(31, 2, 2003);
            String str = result.toString();

            assertTrue(str.contains("INVÁLIDA"));
        }
    }

    @Nested
    @DisplayName("Pruebas de casos límite")
    class EdgeCaseTests {

        @Test
        @DisplayName("Primer día del rango válido")
        void firstDayOfRange() {
            DateAnalysisResult result = service.analyzeDate(1, 1, 2000);

            assertTrue(result.isValidDate());
            assertEquals("Dragón", result.getChineseZodiac());
            assertEquals("Capricornio", result.getWesternZodiac());
            assertTrue(result.isLeapYear());
        }

        @Test
        @DisplayName("Último día del rango válido")
        void lastDayOfRange() {
            DateAnalysisResult result = service.analyzeDate(31, 12, 2005);

            assertTrue(result.isValidDate());
            assertEquals("Gallo", result.getChineseZodiac());
            assertEquals("Capricornio", result.getWesternZodiac());
            assertFalse(result.isLeapYear());
        }

        @Test
        @DisplayName("Cambio de signo occidental exacto")
        void exactZodiacChange() {
            // 19 de enero es Capricornio, 20 es Acuario
            assertEquals("Capricornio", service.getWesternZodiac(19, 1));
            assertEquals("Acuario", service.getWesternZodiac(20, 1));
        }
    }
}
