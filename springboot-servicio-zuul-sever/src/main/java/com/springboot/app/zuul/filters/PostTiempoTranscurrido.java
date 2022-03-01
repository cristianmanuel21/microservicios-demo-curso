package com.springboot.app.zuul.filters;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class PostTiempoTranscurrido extends ZuulFilter{
	
	private static Logger log=LoggerFactory.getLogger(PostTiempoTranscurrido.class);

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		
		RequestContext ctx=RequestContext.getCurrentContext();
		HttpServletRequest request=ctx.getRequest();
		log.info("entrando al post");
		
		Long tiempoInicio=(Long)request.getAttribute("tiempoInicio");
		Long tiempoFinal=System.currentTimeMillis();
		Long tiempoTranscurrido=tiempoFinal-tiempoInicio;
		
		log.info(String.format("Tiempo Transcurrido en segundos %s seg.",tiempoTranscurrido.doubleValue()/1000));
		log.info(String.format("Tiempo Transcurrido en milisegundos %s mlseg.",tiempoTranscurrido.doubleValue()));
		
		return null;
		
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "post";
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 1;
	}

}
