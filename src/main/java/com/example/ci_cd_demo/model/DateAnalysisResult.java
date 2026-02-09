package com.example.ci_cd_demo.model;

/**
 * Resultado del análisis de una fecha.
 * Contiene información sobre la validez de la fecha, año bisiesto,
 * signo del zodiaco occidental y signo del zodiaco chino.
 */
public class DateAnalysisResult {

    private final boolean validDate;
    private final boolean leapYear;
    private final String westernZodiac;
    private final String chineseZodiac;
    private final String errorMessage;
    private final int day;
    private final int month;
    private final int year;

    private DateAnalysisResult(int day, int month, int year, boolean validDate, boolean leapYear,
            String westernZodiac, String chineseZodiac, String errorMessage) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.validDate = validDate;
        this.leapYear = leapYear;
        this.westernZodiac = westernZodiac;
        this.chineseZodiac = chineseZodiac;
        this.errorMessage = errorMessage;
    }

    /**
     * Crea un resultado exitoso con todos los datos del análisis.
     */
    public static DateAnalysisResult success(int day, int month, int year, boolean leapYear,
            String westernZodiac, String chineseZodiac) {
        return new DateAnalysisResult(day, month, year, true, leapYear, westernZodiac, chineseZodiac, null);
    }

    /**
     * Crea un resultado de error cuando la fecha no es válida.
     */
    public static DateAnalysisResult error(int day, int month, int year, String errorMessage) {
        return new DateAnalysisResult(day, month, year, false, false, null, null, errorMessage);
    }

    public boolean isValidDate() {
        return validDate;
    }

    public boolean isLeapYear() {
        return leapYear;
    }

    public String getWesternZodiac() {
        return westernZodiac;
    }

    public String getChineseZodiac() {
        return chineseZodiac;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        if (!validDate) {
            return String.format("Fecha %02d/%02d/%d: INVÁLIDA - %s", day, month, year, errorMessage);
        }
        return String.format(
                "Fecha %02d/%02d/%d: Válida | Año bisiesto: %s | Zodiaco occidental: %s | Zodiaco chino: %s",
                day, month, year,
                leapYear ? "Sí" : "No",
                westernZodiac,
                chineseZodiac);
    }
}
