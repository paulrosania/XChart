/**
 * Copyright 2011 - 2014 Xeiam LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xeiam.xchart.internal.chartpart;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.xeiam.xchart.StyleManager;

/**
 * @author timmolter
 */
public class NumberFormatter {

  private final StyleManager styleManager;

  /**
   * Constructor
   */
  public NumberFormatter(StyleManager styleManager) {

    this.styleManager = styleManager;
  }

  public String getFormatPattern(double value, double min, double max) {

    System.out.println("value: " + value);
    System.out.println("min: " + min);
    System.out.println("max: " + max);

    double difference = max - min;
    int placeOfDifference;
    if (difference == 0.0) {
      placeOfDifference = 0;
    }
    else {
      placeOfDifference = (int) Math.floor(Math.log(difference) / Math.log(10));
    }
    int placeOfValue;
    if (value == 0.0) {
      placeOfValue = 0;
    }
    else {
      placeOfValue = (int) Math.floor(Math.log(value) / Math.log(10));
    }

    System.out.println("difference: " + difference);
    System.out.println("placeOfDifference: " + placeOfDifference);
    System.out.println("placeOfValue: " + placeOfValue);

    if (placeOfDifference <= 4 && placeOfDifference >= -4) {
      System.out.println("getNormalDecimalPattern");
      return getNormalDecimalPatternPositive(placeOfValue, placeOfDifference);
    }
    else {
      System.out.println("getScientificDecimalPattern");
      return getScientificDecimalPattern(placeOfValue, placeOfDifference);
    }
  }

  private String getNormalDecimalPatternPositive(int placeOfValue, int placeOfDifference) {

    int maxNumPlaces = 15;
    StringBuilder sb = new StringBuilder();
    for (int i = maxNumPlaces - 1; i >= -1 * maxNumPlaces; i--) {

      if (i >= 0 && (i < placeOfValue && i >= placeOfDifference)) {
        sb.append("0");
      }
      else if (i < 0 && (i > placeOfValue && i >= placeOfDifference)) {
        sb.append("0");
      }
      else {
        sb.append("#");
      }
      if (i % 3 == 0 && i > 0) {
        sb.append(",");
      }
      if (i == 0) {
        sb.append(".");
      }
    }
    System.out.println(sb.toString());
    return sb.toString();
  }

  private String getScientificDecimalPattern(int placeOfValue, int placeOfDifference) {

    StringBuilder sb = new StringBuilder();
    for (int i = placeOfValue; i >= 0; i--) {
      sb.append("0");
      if (i == placeOfDifference) {
        sb.append(".");
      }
    }
    sb.append("E0");
    System.out.println(sb.toString());
    return sb.toString();
  }

  /**
   * Format a number value, if the override patterns are null, it uses defaults
   * 
   * @param value
   * @return
   */
  public String formatNumber(double value, double min, double max) {

    NumberFormat numberFormat = NumberFormat.getNumberInstance(styleManager.getLocale());

    String decimalPattern;

    if (styleManager.getDatePattern() == null) {

      decimalPattern = getFormatPattern(value, min, max);
    }
    else {
      decimalPattern = styleManager.getDecimalPattern();
    }

    DecimalFormat normalFormat = (DecimalFormat) numberFormat;
    normalFormat.applyPattern(decimalPattern);
    return normalFormat.format(value);

  }

  /**
   * Format a log number value for log Axes which show only decade tick labels. if the override patterns are null, it uses defaults
   * 
   * @param value
   * @return
   */
  public String formatLogNumber(double value) {

    NumberFormat numberFormat = NumberFormat.getNumberInstance(styleManager.getLocale());

    String decimalPattern;

    if (styleManager.getDatePattern() == null) {

      decimalPattern = "0E0";
    }
    else {
      decimalPattern = styleManager.getDatePattern();
    }

    DecimalFormat normalFormat = (DecimalFormat) numberFormat;
    normalFormat.applyPattern(decimalPattern);
    return normalFormat.format(value);

  }
}
