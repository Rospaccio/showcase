package org.merka.showcase.utils;

public class ShowcaseUtils {

	public static final String JSP_PREFIX = "jsp";
	public static final String THYMELEAF_PREFIX = "templates";
	
	/**
	 * Creates a viewName such that it will be resolved to a JSP
	 * @param viewName the simple name of the view
	 * @return A name in the form "<jsp-prefix>/viewName"
	 */
	public static String jspViewName(String viewName)
	{
		return combineViewName(JSP_PREFIX, viewName);
	}
	
	/**
	 * Creates a viewName such that it will be resolved to a HTML template
	 * by the Thymeleaf resolver.
	 * @param viewName
	 * @return A name in the form "<tymeleaf-prefix>/viewName"
	 */
	public static String thymeleafViewName(String viewName)
	{
		return combineViewName(THYMELEAF_PREFIX, viewName);
	}
	
	private static String combineViewName(String prefix, String viewName)
	{
		return prefix + "/" + viewName;
	}
}
