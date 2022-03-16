package ssg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Junit test class for MyApplicationRunner.
 */
public class MyApplicationRunnerTest {

    /**
     * Tests if MyApplicationRunner can be created properly.
     */
    @Test
    void createInstanceProperlyTest() {
        String[] args = {};
        MyApplicationRunner myApplicationRunner = new MyApplicationRunner( args);
        assertNotNull(myApplicationRunner,
                "MyApplicationRunner objet was created but got null object");
    }

    /**
     * Tests if 0 exit code is obtained on successful run.
     */
    @Test
    void exitCodeOnSuccessTest() {
        String[] args = {};
        MyApplicationRunner myApplicationRunner = new MyApplicationRunner( args);
        myApplicationRunner.run();
        assertEquals(0,myApplicationRunner.getExitCode(),
                "Run should be successful but got non 0 exit code");
    }
}
