package io.analytics.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.ui.Model;

@Aspect
public class SiteAspect {
	
	@After("@annotation(io.analytics.aspect.HeaderFooter)")
	public void HeaderFooterAdvice(JoinPoint joinPoint)
	{
		System.out.println("HeaderFooterAdvice!");
		Model m = (Model)joinPoint.getArgs()[1];
		m.addAttribute("HeaderFooter", true);
	}

}
