import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.junit.rules.ExternalResource;
import org.sql2o.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AppTest extends FluentTest{
  public WebDriver webDriver = new HtmlUnitDriver();
  public WebDriver getDefaultDriver(){
    return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("To Do List");
  }

  @Test
  public void categoryIsDisplayedWhenCreated() {
    goTo("http://localhost:4567/");
    fill("#name").with("Morping");
    submit(".submit-name");
    assertThat(pageSource()).contains("Morping");
  }

  @Test
  public void allTasksDisplayDescriptionOnCategoryPage() {
    Category myCategory = new Category("Banking");
    myCategory.save();
    Task firstTask = new Task("Steal money", myCategory.getId());
    firstTask.save();
    Task secondTask = new Task("Steal more money", myCategory.getId());
    secondTask.save();
    String categoryPath = String.format("http://localhost:4567/categories/%d", myCategory.getId());
    goTo(categoryPath);
    assertThat(pageSource()).contains("Steal money");
    assertThat(pageSource()).contains("Steal more money");
  }

  @Test
  public void categoryIsDeleted() {
    Category myCategory = new Category("meep");
    myCategory.save();
    String categoryPath = String.format("http://localhost:4567/categories/%d", myCategory.getId());
    submit(".btn-danger");
    assertThat(pageSource()).doesNotContain("meep");
  }

  @Test
  public void categoryIsUpdated() {
    Category myCategory = new Category("team");
    myCategory.save();
    goTo("http://localhost:4567/");
    click("a", withText("team"));
  //String categoryPath = String.format("http://localhost:4567/categories/%d", myCategory.getId());
    click("a", withText("press"));
    fill("#name").with("morp");
    submit(".btn-success");
    assertThat(pageSource()).contains("morp");
  }

  @Test
  public void taskIsDeleted() {
    Category myCategory = new Category("team");
    myCategory.save();
    Task firstTask = new Task("mooo", myCategory.getId());
    Task secondTask = new Task("booo", myCategory.getId()); //all objects must be created before you go to routes
    firstTask.save();
    secondTask.save();
    goTo("http://localhost:4567/");
    click("a", withText("team"));
    submit(".mooo");
    goTo("http://localhost:4567/");
    click("a", withText("team"));
    assertThat(pageSource()).doesNotContain("mooo");
    assertThat(pageSource()).contains("booo");
  }

  @Test
  public void taskIsUpdated(){
    Category myCategory = new Category("moblie");
    myCategory.save();
    Task firstTask = new Task("marble", myCategory.getId());
    firstTask.save();
    goTo("http://localhost:4567/");
    click("a", withText("moblie"));
    click("a", withText("EDIT"));
    fill("#description").with("toy");
    submit(".btn-success");
    click("a", withText("moblie"));
    assertThat(pageSource()).contains("toy");
  }



}
