package server;

/**
 * @ClassName:Mapper
 * @Description: TODO
 * @Auth: tch
 * @Date: 2020/4/3
 */
public class Mapper {
    private String content;
    private String path;
    private HttpServlet sevletClass;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpServlet getSevletClass() {
        return sevletClass;
    }

    public void setSevletClass(HttpServlet sevletClass) {
        this.sevletClass = sevletClass;
    }
}