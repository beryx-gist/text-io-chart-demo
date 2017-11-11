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

import org.beryx.textio.TextIO;
import org.beryx.textio.swing.SwingTextTerminal;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.function.Function;

public class TextIoJFreeChart {
    private static int POINT_COUNT = 360;

    private static JPanel createChartPanel(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart("Text-IO Demo Chart", "x", "y", dataset);
        ChartPanel panel = new ChartPanel(chart, false);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        return panel;
    }

    private static XYDataset createDataset(Function<Double, Double> f1, Function<Double, Double> f2) {
        double[][] data1 = new double[2][POINT_COUNT + 1];
        double[][] data2 = new double[2][POINT_COUNT + 1];
        for (int i = 0; i <= POINT_COUNT; i++) {
            double x = (double)i * 2 / POINT_COUNT - 1;
            data1[0][i] = x;
            data1[1][i] = f1.apply(x);
            data2[0][i] = x;
            data2[1][i] = f2.apply(x);
        }
        DefaultXYDataset dataset = new DefaultXYDataset();
        dataset.addSeries("f(x)=" + f1.toString(), data1);
        dataset.addSeries("f(x)=" + f2.toString(), data2);
        return dataset;
    }

	public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        SwingTextTerminal terminal = new SwingTextTerminal();

        JFrame frame = terminal.getFrame();
        Container contentPane = frame.getContentPane();
        Component termScroll = contentPane.getComponent(0);
        termScroll.setPreferredSize(new Dimension(400, 480));
        termScroll.setMinimumSize(new Dimension(360, 240));
        contentPane.remove(0);

        JScrollPane chartScroll = new JScrollPane (null, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        chartScroll.setPreferredSize(new Dimension(640, 480));
        chartScroll.setMinimumSize(new Dimension(480, 240));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, termScroll, chartScroll);
        contentPane.add(splitPane);
        contentPane.validate();

        frame.pack();
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        TextIO textIO = new TextIO(terminal);
        while(true){
            splitPane.setRightComponent(null);

            Function<Double, Double> fx = textIO.<Function<Double, Double>>newGenericInputReader(null)
                    .withNumberedPossibleValues(FuncUtil.FUNCTIONS)
                    .read("Choose the first function to be plotted");
            Function<Double, Double> fy = textIO.<Function<Double, Double>>newGenericInputReader(null)
                    .withNumberedPossibleValues(FuncUtil.FUNCTIONS)
                    .read("Choose the second function to be plotted");

            XYDataset dataset = createDataset(fx, fy);
            JPanel chartPanel = createChartPanel(dataset);

            chartScroll.setViewportView(chartPanel);
            splitPane.setRightComponent(chartScroll);

            if(!textIO.newBooleanInputReader().read("Draw another chart?")) break;
        }
        textIO.dispose();
	}
}
