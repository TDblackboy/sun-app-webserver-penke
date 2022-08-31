package pers.sun.protocol;

import pers.sun.protocol.dto.HttpRequest;
import pers.sun.protocol.dto.HttpResponse;

/**
 * @author 曹沫
 * @date 2021/9/2
 */
public interface Servlet {

    void doService(HttpRequest request, HttpResponse response);

}
