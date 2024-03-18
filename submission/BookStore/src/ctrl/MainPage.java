package ctrl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.beans.Book;
import data.beans.Customer;
import data.beans.SiteUser;
import data.beans.Visitor;
import data.dao.BookDAO;
import model.MainPageModel;

/**
 * Servlet implementation class MainPage
 */
@WebServlet({"/MainPage","/MainPage/*"})
public class MainPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    private static final String MODEL = "model";
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainPage() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void init(ServletConfig config) throws ServletException { 
    	super.init(config);

	    ServletContext context = getServletContext();
	    
	    try {
		    MainPageModel model = MainPageModel.getInstance();
		    
		    context.setAttribute(MODEL, model);
	    }
	    catch (Exception e) {
	    	System.out.println("ERROR initializeing main page model!");
	    }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext();
		MainPageModel model = (MainPageModel) context.getAttribute(MODEL);
		

	    Visitor visitor = (Visitor) request.getSession().getAttribute("visitor");
		if(visitor == null)
		{
			visitor = (Visitor) model.getVisitor(request);
			request.getSession().setAttribute("visitor", visitor);
		}
		
		loadPage(request, model);
		request.getRequestDispatcher("html/mainPage.jspx").forward(request, response);


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	/**
	 * This method loads all the html, css and javascript on the main page
	 * in a dynamic manner. 
	 * 
	 * @param request
	 * @param model
	 * 			a model instance for MainPage
	 */
	private void loadPage (HttpServletRequest request, MainPageModel model) {
		Map <String, Integer> category_count = model.getBestSellerBooksByCategory();
		Iterator iterator = category_count.entrySet().iterator(); 
		
		System.out.println(category_count.size());
		
		String main_page_html = "";
		
		String css_value = "";
		
		String result_js =    "/********************************************************************\n"
							+ " *   This file handles the product sliders in the main page\n"
							+ " *   of this website.  \n"
							+ " ********************************************************************/\n\n";
		String declaration_js = "";
		String func_call_js = "";
		String plusSlides_js = "";
		String showSlides_js = "";
		
		while (iterator.hasNext()){
			// create a dynamic suggestion in the main page
			Map.Entry me = (Map.Entry) iterator.next(); 
			
			String category = (String) me.getKey();
			if (category.equals("science-fiction")) category = "science_fiction"; 
			
			List<Book> l = model.getBooksInThisCategory((String)me.getKey());
			
			css_value += ".slides_"+category+" {display: none;}\n";
			
			// update js based on up to date info of categories
			declaration_js += "var slideIndex_"+category + " = 1;\n";
			
			func_call_js += "showSlides_"+category+"(slideIndex_"+category+");\n";
			
			plusSlides_js += "function plusSlides_"+category+"(n) {\n"
									+ "  showSlides_"+category+"(slideIndex_"+category+" += n);\n"
									+ "}\n\n";
			
			showSlides_js += 
					 "function showSlides_"+category+"(n) {\n"
					+ "	var slides = document.getElementsByClassName(\"slides_"+category+"\");\n"
					+ "	var i;\n"
					+ "	var counter;\n"
					+ "	for (i = 0; i < slides.length; i++) {\n"
					+ "    	slides[i].style.display = \"none\";  \n"
					+ " 	 }\n"
					+ "	  \n"
					+ "	var slideNum = n;\n"
					+ "\n"
					+ "	// Adjust slideIndex if there exists overflow or underflow\n"
					+ "	if (slideNum  > slides.length) {slideIndex_"+category+" = 1; }\n"
					+ "  	if (slideNum  < 1) {slideIndex_"+category+" = slides.length; }	\n"
					+ "	\n"
					+ "	for (counter = 0; counter < 7; counter ++) {\n"
					+ "		\n"
					+ "		if (slideNum > slides.length) {slideNum = 1;}\n"
					+ "	    if (slideNum < 1) {slideNum = slides.length; }\n"
					+ "\n"
					+ "		slides[slideNum-1].style.display = \"inline\";\n"
					+ "		slideNum++;\n"
					+ "	}\n"
					+ "}\n\n";
			
			main_page_html += addSuggestionSection(l, category);
		}
		
		// finish the js update
		result_js += declaration_js + "\n" + func_call_js + "\n" + plusSlides_js + "\n" + showSlides_js;
		
		request.setAttribute("JS_CODE_MAIN", result_js);
		request.setAttribute("HTML_SUGGST_MAIN", main_page_html);
		request.setAttribute("CSS_MAIN", css_value);
		
		//System.out.println(main_page_html);
		System.out.println(request.getContextPath()+"/WebContent/");
	}
	
	/**
	 * This mehtod creates html tags designated for the specified genre in `category`
	 * 
	 * @param l			
	 * 			list of top 20 books in that category 
	 * @param category
	 * 			the given genre of the list of books
	 * @return
	 * 		the html portion for that genre of books
	 */
	private String addSuggestionSection(List<Book> l, String category) {
		
		String text = category;
		
		if (category.contains("_")) {
			String[] s = category.split("_");
			text = "";
			
			for (int i = 0; i < s.length; i ++)
				text += s[i] + " ";
		}
		
		String result_html = "<div class=\"container\" >\n"
						   + "	<span class=\"title\"> Top 20 recommended books in "+text+"</span>\n"
						   + "	<div class=\"row\">\n";
		
		for (int index = 0; index < l.size(); index++) {

			
			if (index < 7) {
				result_html += "	<div class=\"column slides_"+category+"\" style=\"display:inline;\" >\n"
						    + "			<form action=\"/ProductPage\" method=\"Post\">\n"
						    + "				<input type=\"hidden\" name=\"bookID\" value=\""+l.get(index).getId()+"\" />"
							+ "				<button id=\"press\" class=\"book\""
							+           " style=\"padding: 15px; height: 260px;width:100%;background-color:grey;background-image:url('/res/book_images/covers/"+l.get(index).getCover()+"');background-position: center;background-size: cover;\">\n"
							+ "				</button>\n"
							+ "			</form>"
							+ "		</div>\n";
			} else {
				result_html += "	<div class=\"column slides_"+category+"\" >\n"
						+ "			<form action=\"/ProductPage\" method=\"Post\">\n"
					    + "				<input type=\"hidden\" name=\"bookID\" value=\""+l.get(index).getId()+"\" />"
						+ "				<button id=\"press\" class=\"book\""
						+           " style=\"padding: 15px; height: 260px;width:100%;background-color:grey;background-image:url('/res/book_images/covers/"+l.get(index).getCover()+"');background-position: center;background-size: cover;\">\n"
						+ "				</button>\n"
						+ "			</form>"
						+ "		</div>\n";
			}
		}
		
		result_html += "	</div>\n"
					+ "	<a class=\"prev\" onclick=\"plusSlides_"+category+"(-1)\">&#10094;</a>\n"
					+ "	<a class=\"next\" onclick=\"plusSlides_"+category+"(1)\">&#10095;</a>\n"
					+ "</div>\n\n";
		return result_html;
		
	}
}
