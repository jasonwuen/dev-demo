package cn.net.wujun;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by Jason Wu on 2017/04/23.
 */
public class MvnTest {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void userDir() {
        String userDir = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(userDir);
        userDir = userDir.replaceAll("/target/test-classes/", "");
        System.out.println(userDir);
    }

    @Test
    public void dependencyList() throws Exception {
        String userDir = this.getClass().getClassLoader().getResource("").getPath();
        userDir = userDir.replaceAll("/target/test-classes/", "");
        System.out.println(userDir);
        if (!isMavenProject(userDir)) {
            logger.warn("---->>>>>> {} is not maven project", userDir);
            System.exit(-1);
        }
        File projectFile = new File(userDir);
        Process proc = Runtime.getRuntime().exec("mvn dependency:list", null, projectFile);
        InputStream stderr = proc.getInputStream();
        InputStreamReader isr = new InputStreamReader(stderr);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        Set<String> mvnCpList = new HashSet<String>();
        System.out.println("################");
        while ((line = br.readLine()) != null) {
            line = line.replaceAll("\\[INFO\\]", "").trim();
            //System.out.println(line);
            processMavenOutput(line, mvnCpList);
        }
        System.out.println("################");
        int exitVal = proc.waitFor();
        System.out.println("Process exitValue: " + exitVal);
        System.out.println("mvnCpList:\n" + mvnCpList);
        System.out.println("------------------------------------");
        for (String groupId : mvnCpList) {
            System.out.println(groupId + ",");
        }
        System.out.println("------------------------------------");
    }

    private boolean isMavenProject(String projectDir) {
        File pomfile = new File(String.format("%s/pom.xml", projectDir));
        return pomfile.exists();
    }

    private void processMavenOutput(String line, Set<String> dependencies) {
        if (line.matches("^.*:.*:.*:.*:.*")) {
            System.out.println("#######  dep: " + line + "   ########    ");
            if (line.matches(".*test$") || line.matches(".*provided$") || line.matches("" +
                    ".*system$") || line.matches(".*Finished at.*"))
                System.out.println(line + " filtered");
            else {
                StringTokenizer stk = new StringTokenizer(line, ":");
                int i = 0;
                String groupId = "";
                String artifactId = "";
                String packaging = "";
                String version = "";
                while (stk.hasMoreElements()) {
                    String s = (String) stk.nextElement();
                    if (i == 0)
                        groupId = s;
                    if (i == 1)
                        artifactId = s;
                    if (i == 2)
                        packaging = s;
                    if (i == 3)
                        version = s;
                    i++;
                }
                String dep = groupId;
                //String dep = artifactId + "-" + version + "." + packaging;
                System.out.println("--->>>>>>" + dep);
                dependencies.add(dep);
            }
        }
    }
}
