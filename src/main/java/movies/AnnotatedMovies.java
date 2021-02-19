package movies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
public class AnnotatedMovies {
    public static void main(String[] args) {
        SpringApplication.run(AnnotatedMovies.class, args);
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String helloWorld() {
        return "Welcome to Spring MVC!";
    }
}

// можно в connection через блок объявлять sessionFactory(статик блок можно добавить)
// Через спринг магию, добавить в xml доп параметр(очень хорошо подходит как сервис)
// Connection как bean
// запускаешь не через мейн. мейн - инициализация контекста спринга.
// springApplication run наверх