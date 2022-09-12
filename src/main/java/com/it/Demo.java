package com.it;

import com.it.util.SqlUtils;
import com.it.view.MainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class Demo extends AbstractJavaFxApplicationSupport {

        public static void main(String[] args) {
            launch(Demo.class, MainView.class,args);
        }

//    public static void main(String[] args) {
//        String pre =  "update COUNT(1) FROM T_USER_TEST3 WHERE name = ? ";
//        String params = "zhangsan3(String)";
//        System.out.println(SqlUtils.parseSql(pre, params));
//    }

}