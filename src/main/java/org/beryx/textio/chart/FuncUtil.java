/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
    package org.beryx.textio.chart;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import static java.lang.Math.*;

public class FuncUtil {
    public static Function<Double, Double> fun(String name, Function<Double, Double> f) {
        return new Function<Double, Double>() {
            @Override
            public Double apply(Double x) {
                return f.apply(x);
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    public static final Function<Double, Double> X = fun("x", x -> x);
    public static final Function<Double, Double> X_SQR = fun("x^2", x -> x * x);
    public static final Function<Double, Double> SIN = fun("sin(x)", x -> sin(x * PI));
    public static final Function<Double, Double> COS = fun("cos(x)", x -> cos(x * PI));
    public static final Function<Double, Double> SIN_2 = fun("sin(x/2)", x -> sin(x / 2 * PI));
    public static final Function<Double, Double> COS_2 = fun("cos(x/2)", x -> cos(x / 2 * PI));
    public static final Function<Double, Double> SIN_SQR = fun("sin^2(x)", x -> pow(sin(x * PI), 2));
    public static final Function<Double, Double> COS_SQR = fun("cos^2(x)", x -> pow(sin(x * PI), 2));
    public static final Function<Double, Double> SINH = fun("sinh(x)", x -> sinh(x));
    public static final Function<Double, Double> COSH = fun("cosh(x)", x -> cosh(x));

    public static final List<Function<Double, Double>> FUNCTIONS = Collections.unmodifiableList(Arrays.asList(
            X, X_SQR, SIN, COS, SIN_2, COS_2, SIN_SQR, COS_SQR, SINH, COSH
    ));
}
