package io.analytics.aspect;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.ui.Model;

@Aspect
public class SiteAspect {
	

	@Pointcut("execution(public * *(..))")
    public void publicMethod() {}
	
	// @HeaderFooter
	@Around("publicMethod() && @annotation(headerfooter)")
	public Object HeaderFooterAdvice(ProceedingJoinPoint joinPoint, HeaderFooter headerfooter) throws Throwable
	{
		Model model = this.getModel(joinPoint.getArgs());
		
		/* A map to store various information pertaining to the header */
		Map<String, Object> Header = new HashMap<String, Object>();
		/* A map to store various information pertaining to the footer */
		Map<String, Object> Footer = new HashMap<String, Object>();
		
		Header.put("path", "/WEB-INF/views/includes/header.jsp");
		Header.put("type", headerfooter.value());
		
		model.addAttribute("SETTINGS", headerfooter.value().equals("Application") ? "/WEB-INF/views/includes/settings.jsp" : "");
		
		Footer.put("path", "/WEB-INF/views/includes/footer.jsp");
		Footer.put("type", headerfooter.value());
		
		model.addAttribute("HEADER", Header);
		model.addAttribute("FOOTER", Footer);
		
		return joinPoint.proceed();
	}
	

	// @SidePanel
	@Around("publicMethod() && @annotation(sidepanel)")
	public Object SidePanelAdvice(ProceedingJoinPoint joinPoint, SidePanel sidepanel) throws Throwable
	{
		Model model = this.getModel(joinPoint.getArgs());
		
		Map<String, Object> Sidepanel = new HashMap<String, Object>();
		
		Sidepanel.put("path", "/WEB-INF/views/includes/sidepanel.jsp");
		Sidepanel.put("animate", sidepanel.animate());
		
		model.addAttribute("SIDEPANEL", Sidepanel);
		
		return joinPoint.proceed();
	}

	
	
	/**
	 * Returns the Model object from a list of parameters.
	 * @param args
	 * @return The Model.
	 * @throws Exception if there is no Model.
	 */
	private Model getModel(Object[] args) throws Exception {
		
		for(Object arg : args) {
			if (arg instanceof Model)
				return (Model)arg;
		}
		
		throw new Exception("No Model parameter");
		
	}

}
