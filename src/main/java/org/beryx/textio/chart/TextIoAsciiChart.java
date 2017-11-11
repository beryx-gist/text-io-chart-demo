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

import com.indvd00m.ascii.render.Region;
import com.indvd00m.ascii.render.Render;
import com.indvd00m.ascii.render.api.ICanvas;
import com.indvd00m.ascii.render.api.IContextBuilder;
import com.indvd00m.ascii.render.api.IRender;
import com.indvd00m.ascii.render.elements.Rectangle;
import com.indvd00m.ascii.render.elements.plot.Axis;
import com.indvd00m.ascii.render.elements.plot.AxisLabels;
import com.indvd00m.ascii.render.elements.plot.Plot;
import com.indvd00m.ascii.render.elements.plot.api.IPlotPoint;
import com.indvd00m.ascii.render.elements.plot.misc.PlotPoint;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class TextIoAsciiChart {
    private static int POINT_COUNT = 360;
    private static int WIDTH = 80;
    private static int HEIGHT = 40;

    public static String getChart(Function<Double, Double> fx, Function<Double, Double> fy) {
        Locale.setDefault(Locale.US);
        List<IPlotPoint> points = new ArrayList<>();
        for (int i = 0; i <= POINT_COUNT; i++) {
            double x = (double)i * 2 / POINT_COUNT - 1;
            double val1 = fx.apply(x);
            double val2 = fy.apply(x);
            IPlotPoint plotPoint = new PlotPoint(val1, val2);
            points.add(plotPoint);
        }
        IRender render = new Render();
        IContextBuilder builder = render.newBuilder();
        builder.width(WIDTH).height(HEIGHT);
        builder.element(new Rectangle(0, 0, WIDTH, HEIGHT));
        builder.layer(new Region(1, 1, WIDTH - 2, HEIGHT - 2));
        builder.element(new Axis(points, new Region(0, 0, WIDTH - 2, HEIGHT - 2)));
        builder.element(new AxisLabels(points, new Region(0, 0, WIDTH - 2, HEIGHT - 2)));
        builder.element(new Plot(points, new Region(0, 0, WIDTH - 2, HEIGHT - 2)));
        ICanvas canvas = render.render(builder.build());
        return canvas.getText();
    }

	public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        TextIO textIO = TextIoFactory.getTextIO();
        while(true){
            Function<Double, Double> fx = textIO.<Function<Double, Double>>newGenericInputReader(null)
                    .withNumberedPossibleValues(FuncUtil.FUNCTIONS)
                    .read("Choose the function to be used on the X axis");
            Function<Double, Double> fy = textIO.<Function<Double, Double>>newGenericInputReader(null)
                    .withNumberedPossibleValues(FuncUtil.FUNCTIONS)
                    .read("Choose the function to be used on the Y axis");

            textIO.getTextTerminal().println(getChart(fx,fy));
            if(!textIO.newBooleanInputReader().read("Draw another chart?")) break;
        }
        textIO.dispose();
	}
}
