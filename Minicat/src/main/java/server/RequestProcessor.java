package server;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

public class RequestProcessor extends Thread {

    private Socket socket;
    private Map<String,Mapper> servletMap;

    public RequestProcessor(Socket socket, Map<String, Mapper> servletMap) {
        this.socket = socket;
        this.servletMap = servletMap;
    }

    @Override
    public void run() {
        try{
            InputStream inputStream = socket.getInputStream();

            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            // 静态资源处理
            if(servletMap.get(request.getUrl()) == null) {
                response.outputHtml(request.getUrl());
            }else{
                Mapper mapper = servletMap.get(request.getUrl());
                // 动态资源servlet请求
                HttpServlet httpServlet = mapper.getSevletClass();
                httpServlet.service(request,response);
            }

            socket.close();

        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
