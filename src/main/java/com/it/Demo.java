package com.it;

import com.it.view.MainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class Demo extends AbstractJavaFxApplicationSupport {

        public static void main(String[] args) {
            launch(Demo.class, MainView.class,args);
        }

}