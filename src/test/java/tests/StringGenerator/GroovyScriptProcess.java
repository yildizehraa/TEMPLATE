package tests.StringGenerator;

import common.BaseLibrary;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;


/****************************************************
 * Tarih: 2019-02-12
 * Proje: SolCRM Functional Test Automation
 * Class: 
 * Yazan: Zehra Yıldız
 ****************************************************/

public class GroovyScriptProcess extends BaseLibrary {

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        String[] file;
        ArrayList<String> name = new ArrayList();
        String[] splitClass;
        String className;
        int cnt = 0;
        int cnt2 = 0;
        String packageName;
        String packages;
        String path = "C:\\Users\\admin\\Desktop\\TestHackathon\\Yeni klasör\\src\\test\\java\\tests";
        File dir = new File(path);
        ArrayList liste = new ArrayList();
        Class c;
        File[] subDirs = dir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        for (int i = 0; i < subDirs.length; i++) {
            File subDir = subDirs[i];
            file = subDir.getCanonicalFile().list();

            cnt2 = name.size();
            cnt = cnt2;

            if (file.length > 1) {

                File subFile = subDir.getCanonicalFile();
                packageName = subDir.getName();

                for (int j = 0; j < file.length; j++) {
                    cnt = cnt + j;
                    String pt = subDir.getCanonicalFile() + "\\" + file[j];

                    File dir2 = new File(pt);
                    className = dir2.getCanonicalFile().list()[0];
                    splitClass = className.split(".java");
                    name.add(cnt, splitClass[0]);

                    String subPackageName;
                    subPackageName = dir2.getCanonicalFile().getName();
                    packages = "tests." + packageName + "." + subPackageName;
                    c = Class.forName(packages + "." + name.get(cnt));
                    liste.add(c.getMethods());
                    cnt = cnt2;
                }

            } else {
                className = file[0];
                splitClass = className.split(".java");
                name.add(cnt, splitClass[0]);
                packageName = subDir.getName();
                packages = "tests." + packageName;
                c = Class.forName(packages + "." + name.get(cnt));
                liste.add(c.getMethods());
            }
        }

        ArrayList<String> allMethods = new ArrayList<>();
        ArrayList<String> methods = new ArrayList<>();
        int z = 0;

        for (int i = 0; i < liste.size(); i++) {
            int size = ((Method[]) liste.get(i)).length;
            for (int j = 0; j < size; j++) {
                String str2 = stringOlusturma(((Method[]) liste.get(i))[j]);
                allMethods.add(j, str2);
                if (allMethods.get(j).contains("TS")) {
                    methods.add(z, allMethods.get(j));
                    z++;
                }
            }
        }

        System.out.println("String tests='" + methods + "'" + "\nreturn \"<input name='value' value='${tests}' class='setting-input' type='text' style='width:100%;'>\"");
    }
}
