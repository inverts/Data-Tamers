package io.analytics.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.ui.Model;

@Aspect
public class SiteAspect {
	
	@After("@annotation(io.analytics.aspect.HeaderFooter)")
	public void HeaderFooterAdvice(JoinPoint joinPoint) throws Exception
	{
		//TODO: Figure out a better way to find the Model argument
		Model model = this.getModel(joinPoint.getArgs());
		
		model.addAttribute("HEADER", "/WEB-INF/views/includes/header.jsp");
		model.addAttribute("FOOTER", "/WEB-INF/views/includes/footer.jsp");
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
