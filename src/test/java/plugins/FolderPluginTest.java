/*
 * The MIT License
 *
 * Copyright 2015 CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package plugins;

import org.hamcrest.MatcherAssert;
import org.jenkinsci.test.acceptance.Matchers;
import org.jenkinsci.test.acceptance.junit.AbstractJUnitTest;
import org.jenkinsci.test.acceptance.junit.WithPlugins;
import org.jenkinsci.test.acceptance.po.Folder;
import org.junit.Test;

/**
 * Acceptance tests for the CloudBees Folder Plugins.
 */
@WithPlugins("cloudbees-folder")
public class FolderPluginTest extends AbstractJUnitTest {
    /** Test folder name. */
    private static final String F01 = "F01";
    /** Test folder name. */
    private static final String F02 = "F02";
    
    /**
     * Checks that a folder exists and has the provided name.
     */
    private void checkFolder(Folder folder, String name) {
        folder.open();
        MatcherAssert.assertThat(driver, Matchers.hasContent(name));
    }
    
    /**
     * First simple test scenario: Folder creation (JENKINS-31648).
     * <ol>
     * <li>We create a folder named "F01".</li>
     * <li>We check the folder exists and we can enter it.</li>
     * </ol>
     */
    @Test
    public void createFolder() {
        final Folder job = jenkins.jobs.create(Folder.class, F01);
        job.save();
        jenkins.open();
        checkFolder(job, F01);
    }
    
    /**
     * Simple folder hierarchy test scenario: Folder creation (JENKINS-31648).
     * <ol>
     * <li>We create a folder named "F01".</li>
     * <li>We check the folder exists and we can enter it.</li>
     * <li>We create a folder name "F02" inside "F01" and check it.</li>
     * <li>We visit "F01" and the root page, create a folder named "F01" inside the existing "F01" one and check it.
     * </ol>
     */
    @Test
    public void createFolderHierarchy() {
        final Folder parent = jenkins.jobs.create(Folder.class, F01);
        parent.save();
        checkFolder(parent, F01);
        final Folder child1 = parent.getJobs().create(Folder.class, F02);
        child1.save();
        checkFolder(child1, F02);
        parent.open();
        jenkins.open();
        final Folder child2 = parent.getJobs().create(Folder.class, F01);
        child2.save();
        checkFolder(child2, F01);
    }
}
