package guru.springframework.spring6di.controllers;

import guru.springframework.spring6di.services.GreetingService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/*
Spring Bean Lifecycle

1.  constructor
2.  setters (@Autowired)
3.  beanName (BeanNameAware)
4.  beanFactory (BeanFactoryAware)
5.  applicationContext (ApplicationContextAware)
7.  postProcessBefore
8.  init (@PostConstruct)
9.  beanInitializing (InitializingBean)
10. postProcessAfter
11. preDestroy (@PreDestroy)
12. destroy (DisposableBean)

 */
@Controller
public class BeanLifeCycleController implements InitializingBean,
        BeanNameAware, BeanFactoryAware, ApplicationContextAware, DisposableBean {

    private GreetingService greetingService;

    public BeanLifeCycleController() {
        System.out.println("1. constructor");
    }

    @Autowired
    public void setGreetingService(GreetingService greetingService) {
        this.greetingService = greetingService;
        System.out.println("2. setter");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("3. beanName : " + name);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("4. beanFactory");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("5. applicationContext");
    }

    @PostConstruct
    public void init() {
        System.out.println("8. init");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("9. beanInitializing");
    }

    public String sayHello() {
        System.out.println("I'm in the controller");

        return greetingService.sayGreeting();
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("11. preDestroy");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("12. destroy");
    }
}

@Component
class MyControllerBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof BeanLifeCycleController controller) {
            System.out.println("7. postProcessBefore");
            controller.setGreetingService(new GreetingService() {
                @Override
                public String sayGreeting() {
                    System.out.println("I'm hacked (^_^)");
                    return "Lol this is an injected deps :) :)";
                }
            });
            return controller;
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if ("myController".equals(beanName))
            System.out.println("10. postProcessAfter");
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}