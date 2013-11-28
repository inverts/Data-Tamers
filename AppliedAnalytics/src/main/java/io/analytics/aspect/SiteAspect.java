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
		Model model = (Model)joinPoint.getArgs()[1];
		
		model.addAttribute("HEADER", "/WEB-INF/views/includes/header.jsp");
		model.addAttribute("FOOTER", "/WEB-INF/views/includes/footer.jsp");
	}

}
