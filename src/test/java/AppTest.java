import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.junit.rules.ExternalResource;
import org.sql2o.*;


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
    fill("#name").with("Banking");
    submit(".btn");
    assertThat(pageSource()).contains("Banking");
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
}
