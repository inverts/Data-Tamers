package io.analytics.aspect;

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
		
		model.addAttribute("HEADER", "/WEB-INF/views/includes/header.jsp");
		
		model.addAttribute("SETTINGS", headerfooter.showSettings() ? "/WEB-INF/views/includes/settings.jsp" : "");
		
		model.addAttribute("FOOTER", "/WEB-INF/views/includes/footer.jsp");
		
		return joinPoint.proceed();
	}
	

	// @SidePanel
	@Around("publicMethod() && @annotation(sidepanel)")
	public Object SidePanelAdvice(ProceedingJoinPoint joinPoint, SidePanel sidepanel) throws Throwable
	{
		Model model = this.getModel(joinPoint.getArgs());
		
		model.addAttribute("SIDEPANEL", "/WEB-INF/views/includes/sidepanel.jsp");
		model.addAttribute("SIDEPANEL_animate", sidepanel.animate());
		
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
