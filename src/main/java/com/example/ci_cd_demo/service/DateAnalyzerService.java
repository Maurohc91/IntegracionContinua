package com.example.ci_cd_demo.service;

import org.springframework.stereotype.Service;

import com.example.ci_cd_demo.model.DateAnalysisResult;

/**
 * Servicio para analizar fechas.
 * Determina si una fecha es válida, si pertenece a un año bisiesto,
 * el signo del zodiaco occidental y el signo del zodiaco chino.
 * Solo acepta fechas entre los años 2000 y 2005.
 */
@Service
public class DateAnalyzerService {

    private static final int MIN_YEAR = 2000;
    private static final int MAX_YEAR = 2005;

    // Días que tiene cada mes (índice 0 no se usa, enero es índice 1)
    private static final int[] DAYS_IN_MONTH = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    // Signos del zodiaco chino para cada año (2000-2005)
    private static final String[] CHINESE_ZODIAC = {
            "Dragón", // 2000
            "Serpiente", // 2001
            "Caballo", // 2002
            "Cabra", // 2003
            "Mono", // 2004
            "Gallo" // 2005
    };

    /**
     * Analiza una fecha y devuelve información detallada sobre ella.
     * 
     * @param day   día del mes (1-31)
     * @param month mes del año (1-12)
     * @param year  año (debe estar entre 2000 y 2005)
     * @return resultado del análisis con toda la información
     */
    public DateAnalysisResult analyzeDate(int day, int month, int year) {
        // Validar el año
        if (year < MIN_YEAR || year > MAX_YEAR) {
            return DateAnalysisResult.error(day, month, year,
                    String.format("El año debe estar entre %d y %d", MIN_YEAR, MAX_YEAR));
        }

        // Validar el mes
        if (month < 1 || month > 12) {
            return DateAnalysisResult.error(day, month, year,
                    "El mes debe estar entre 1 y 12");
        }

        // Calcular si es año bisiesto
        boolean leapYear = isLeapYear(year);

        // Validar el día
        int maxDays = getMaxDaysInMonth(month, leapYear);
        if (day < 1 || day > maxDays) {
            return DateAnalysisResult.error(day, month, year,
                    String.format("El día debe estar entre 1 y %d para el mes %d", maxDays, month));
        }

        // Calcular signos del zodiaco
        String westernZodiac = getWesternZodiac(day, month);
        String chineseZodiac = getChineseZodiac(year);

        return DateAnalysisResult.success(day, month, year, leapYear, westernZodiac, chineseZodiac);
    }

    /**
     * Determina si un año es bisiesto.
     * Un año es bisiesto si:
     * - Es divisible por 4 Y
     * - No es divisible por 100, O es divisible por 400
     */
    public boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * Obtiene el número máximo de días en un mes.
     */
    public int getMaxDaysInMonth(int month, boolean leapYear) {
        if (month == 2 && leapYear) {
            return 29;
        }
        return DAYS_IN_MONTH[month];
    }

    /**
     * Determina el signo del zodiaco occidental basado en el día y mes.
     */
    public String getWesternZodiac(int day, int month) {
        return switch (month) {
            case 1 -> day <= 19 ? "Capricornio" : "Acuario";
            case 2 -> day <= 18 ? "Acuario" : "Piscis";
            case 3 -> day <= 20 ? "Piscis" : "Aries";
            case 4 -> day <= 19 ? "Aries" : "Tauro";
            case 5 -> day <= 20 ? "Tauro" : "Géminis";
            case 6 -> day <= 20 ? "Géminis" : "Cáncer";
            case 7 -> day <= 22 ? "Cáncer" : "Leo";
            case 8 -> day <= 22 ? "Leo" : "Virgo";
            case 9 -> day <= 22 ? "Virgo" : "Libra";
            case 10 -> day <= 22 ? "Libra" : "Escorpio";
            case 11 -> day <= 21 ? "Escorpio" : "Sagitario";
            case 12 -> day <= 21 ? "Sagitario" : "Capricornio";
            default -> "Desconocido";
        };
    }

    /**
     * Determina el signo del zodiaco chino basado en el año.
     * Nota: Esta es una simplificación que no tiene en cuenta el Año Nuevo Chino.
     */
    public String getChineseZodiac(int year) {
        if (year < MIN_YEAR || year > MAX_YEAR) {
            return "Fuera de rango";
        }
        return CHINESE_ZODIAC[year - MIN_YEAR];
    }
}
