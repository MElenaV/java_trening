package bdd;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;


@CucumberOptions(features = "classpath:bdd", plugin = {"pretty", "html:build/cucumber-report"})  // аннотация, которая указывает, где находятся файлы с описанием сценариев (где-то в одной из директорий classpath есть поддеректория bdd и в ней нахоядтся файлы - groups.feature; опция plugin задаёт формат отчетов, "pretty" - чтобы на консоль красиво выводилось )
public class GroupTests extends AbstractTestNGCucumberTests {  // класс запускатель
}
