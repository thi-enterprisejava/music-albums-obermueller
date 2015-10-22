package de.thi.mymusic.web;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.net.URL;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Wildfly must be running!
 *
 * The test uses htmlUnit by default.
 * You can change the webdriver (browser) by amending the arquillian.xml or
 * define a system property e. g.
 * {@code -Darq.extension.webdriver.browserCapabilities=firefox}
 */
@RunWith(Arquillian.class)
public class MyIntegrationTest {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addAsWebResource(new File("src/main/webapp/index.xhtml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/faces-config.xml"));
        System.out.println(webArchive.toString(Formatters.VERBOSE));
        return webArchive;
    }

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL deploymentUrl;

    @Test
    public void thatHelloWorldIsRendered() throws Exception {

        browser.get(deploymentUrl.toExternalForm() + "index.xhtml");

        // Log the complete page source
        System.out.println(browser.getPageSource());

        assertThat(browser.getTitle(), is("Welcome"));
        assertThat(browser.findElement(By.id("welcome")).getText(), is("Hello World"));
    }

}
