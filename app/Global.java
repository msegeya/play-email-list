import configs.AppConfig;
import configs.DataConfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.ApplicationContext;

import play.Application;
import play.GlobalSettings;

public class Global extends GlobalSettings {

    private ApplicationContext ctx;

    @Override
    public void onStart(Application app) {
        ctx = new AnnotationConfigApplicationContext(AppConfig.class, DataConfig.class);
    }

    @Override
    public <A> A getControllerInstance(Class<A> clazz) {
        return ctx.getBean(clazz);
    }

}
