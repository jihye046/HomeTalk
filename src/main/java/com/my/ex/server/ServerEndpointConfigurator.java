package com.my.ex.server;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

// @ServerEndpoint를 사용하면 @Autowired를 사용할 수 없기때문에 사용 가능하도록 해당 클래스를 설정
@Component
public class ServerEndpointConfigurator extends javax.websocket.server.ServerEndpointConfig.Configurator implements ApplicationContextAware {
	
	private static volatile BeanFactory context;

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return context.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ServerEndpointConfigurator.context = applicationContext;
    }
    
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
    	HttpSession httpSession = (HttpSession) request.getHttpSession();
    	
    	if(httpSession != null) {
    		String userId = (String) httpSession.getAttribute("userId");
    		if(userId != null) {
    			// 가져온 userId를 WebSocket Session의 userProperties에 저장
    			sec.getUserProperties().put("userId", userId);
    			System.out.println("WebSocket Handshake: userId '" + userId + "' from HttpSession stored in WebSocket session.");
    		} else {
    			System.out.println("WebSocket Handshake: userId not found in HttpSession.");
    		}
    	} else {
    		System.out.println("WebSocket Handshake: HttpSession is null. User might not be logged in or session expired.");
    	}
    	
//    	super.modifyHandshake(sec, request, response);
    }
    
}
