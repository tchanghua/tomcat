package server;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:LoadServletUtil
 * @Description: TODO
 * @Auth: tch
 * @Date: 2020/4/3
 */
public class LoadServletUtil {

    public Map<String,Mapper> loadServlet(Map<String, Mapper> map){
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("server.xml");
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//Server/Service/Engine/Host");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element =  selectNodes.get(i);
                // <servlet-name>lagou</servlet-name>
                List context = element.selectNodes("Context");
                for(Object obj : context) {
                    Element element1 = (Element) obj;
                    String path = element1.attributeValue("docBase");
                    String urlParttln = element1.attributeValue("path");
                    System.out.println("path" + path);
                    // <servlet-class>server.LagouServlet</servlet-class>
                   getClassName(path);
                   if(listFileName != null && listFileName.size() > 0 ){
                       for(String fileName:listFileName){

                           String className = fileName.substring(path.length()+1,fileName.length()-".class".length()).replaceAll("\\\\",".");
                           System.out.println(className);
                           HttpServlet servlet = loadServlet(className);
                           System.out.println(servlet);
                           if(servlet != null){
                               Mapper mapper = new Mapper();
                               mapper.setPath(urlParttln);
                               mapper.setSevletClass(servlet);
                               map.put(urlParttln,mapper);
                           }
                       }
                   }
                }
                // /lagou
//                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                Mapper mapper = new Mapper();
//                mapper.setPath(urlPattern);
//                HttpServlet servlet = (HttpServlet) Class.forName(servletClass).newInstance();
//                mapper.setSevletClass(servlet);
//                map.put(urlPattern, mapper);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }


    private HttpServlet loadServlet(String name){
        try {
            Class<?> aClass = Class.forName(name);
            Object o = aClass.newInstance();
            if(o instanceof HttpServlet){
                return (HttpServlet) o;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> listFileName = new ArrayList<>();

    private void getClassName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        String[] names = file.list();
        if (names != null) {
            String[] completNames = new String[names.length];
            for (int i = 0; i < names.length; i++) {
                if(names[i].endsWith(".class")) {
                    completNames[i] = path + names[i];
                    System.out.println("aaa:"+completNames[i]);
                    listFileName.add(completNames[i]);
                }
            }
        }
        for (File a : files) {
            if (a.isDirectory()) {//如果文件夹下有子文件夹，获取子文件夹下的所有文件全路径。
                 getClassName(a.getAbsolutePath()+"\\");
            }
        }
    }

}