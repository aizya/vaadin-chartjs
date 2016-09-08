package com.byteowls.vaadin.chartjs.demo.ui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;

import org.springframework.util.ClassUtils;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public abstract class AbstractChartView extends VerticalLayout implements ChartView {

    private static final long serialVersionUID = -1280497201872048972L;
    
    @PostConstruct
    public void postConstruct() {
        setSizeFull();
        setMargin(true);
        Component layout = getChart();
        layout.setHeight(100, Unit.PERCENTAGE);
        layout.setWidth(90, Unit.PERCENTAGE);
        addComponent(layout);
        setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
    }

    @Override
    public String getSource() {
        // thanks to https://github.com/johndevs/dragdroplayouts/blob/master/demo/src/main/java/fi/jasoft/dragdroplayouts/demo/DemoView.java#L41
        String path = getClass().getCanonicalName().replaceAll("\\.", "/") + ".java";

        try (InputStream in = getClass().getClassLoader().getResourceAsStream("../src/main/java/"+ path)) {
            if (in != null) {

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                    boolean inCodeBlock = false;

                    StringBuilder codelines = new StringBuilder();
                    String line = reader.readLine();
                    while (line != null) {
                        if (line.contains("public Component getChart() {")) {
                            inCodeBlock = true;
                        } else if (line.contains("return chart;")) {
                            inCodeBlock = false;
                        } else if (inCodeBlock) {
                            codelines.append(line);
                            codelines.append("\n");
                        } 
                        line = reader.readLine();
                    }
                    return codelines.toString();
                } catch (Exception ignore) {}
            }
        } catch (Exception ignore) {}

        return null;
    }
    
    @Override
    public void enter(ViewChangeEvent event) {
        
    }

    @Override
    public String getViewName() {
        Class<?> realBeanClass = ClassUtils.getUserClass(getClass());
        return realBeanClass.getSimpleName();
    }
    
    

}