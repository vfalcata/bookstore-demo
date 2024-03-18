package ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.beans.Book;
import model.AdminModel;
import model.MainPageModel;

/**
 * Servlet implementation class Admin
 */
@WebServlet({"/Admin","/Admin/*"})
public class Admin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ADMIN_MODEL = "admin_model";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Admin() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init(ServletConfig config) throws ServletException { 
    	super.init(config);

	    ServletContext context = getServletContext();
	    
	    try {
		    AdminModel model = AdminModel.getInstance();
		    
		    context.setAttribute(ADMIN_MODEL, model);
	    }
	    catch (Exception e) {
	    	System.out.println("ERROR initializeing admin page model!");
	    }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		ServletContext context = getServletContext();
		
		AdminModel model = (AdminModel) context.getAttribute(ADMIN_MODEL);
		
		boolean ajax = false;
		
		if (request.getParameter("reportAjax") != null) {
			if (request.getParameter("reportAjax").equals("true")) {
				ajax = true;
			}
		}
		
		if (request.getParameter("type") != null) {
			if (request.getParameter("type").equals("report")) {
				if (ajax) {
					ajaxReport(request, response, model);
				} else {
					loadReport(request, model);
					request.getRequestDispatcher("/html/admin/admin.jspx").forward(request, response);
				}
			} else if (request.getParameter("type").equals("analytics")) {
				loadAnalytics(request, model);
				request.getRequestDispatcher("/html/admin/admin.jspx").forward(request, response);
			} else if (request.getParameter("type").equals("stats")) {
				loadStats(request, model);
				request.getRequestDispatcher("/html/admin/admin.jspx").forward(request, response);
			} else {
				loadHome(request, model);
				request.getRequestDispatcher("/html/admin/admin.jspx").forward(request, response);
			}
		} else {
			loadHome(request, model);
			request.getRequestDispatcher("/html/admin/admin.jspx").forward(request, response);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void loadHome(HttpServletRequest request, AdminModel model) {

		String main_page_html = "<h2>Welcome to the admin page!</h2>\n"
				+ "	<h3>Create and view reports by selecting options in the menu on the left.</h3>";
		
		request.setAttribute("HTML_SUGGST_ADMIN", main_page_html);
		
	}
	
	private void loadReport(HttpServletRequest request, AdminModel model) {

		String main_page_html = "<h2>Welcome to the Report page!</h2>\n"
				+ "	<h3>You can generate a report with the books sold each month</h3>";
		
		LocalDateTime oldestSale = model.oldestSale();
		LocalDateTime latestSale = model.latestSale();
		
		int oldestYear = oldestSale.getYear();
		int latestYear = latestSale.getYear();
		
		System.out.println(oldestYear + ">" + latestYear);
		
		main_page_html += "<form action=\"/Admin/report\">\n"
				+ "			  <label for=\"startYear\">Choose a start Month:</label>\n"
				+ "			  <select id=\"startYear\" name=\"startYear\">\n";
		
		for (int i = oldestYear; i <= latestYear; i++) {
			main_page_html += " <option value=\"" + i + "\">" + i + "</option>\n";
		}
		
		main_page_html += "   </select>\n"
				+ "			  <select id=\"startMonth\" name=\"startMonth\">\n";
		
		for (int i = 0; i < 12; i++) {
			main_page_html += " <option value=\"" + (i + 1) + "\">" + new DateFormatSymbols().getMonths()[i] + "</option>\n";
		}
		
		main_page_html += "   </select>\n"
				+ "			  <label for=\"endYear\">Choose a end Month:</label>\n"
				+ "			  <select id=\"endYear\" name=\"endYear\">\n";
		
		for (int i = oldestYear; i <= latestYear; i++) {
			main_page_html += " <option value=\"" + i + "\">" + i + "</option>\n";
		}
		
		main_page_html += "   </select>\n"
				+ "			  <select id=\"endMonth\" name=\"endMonth\">\n";
		
		for (int i = 0; i < 12; i++) {
			main_page_html += " <option value=\"" + (i + 1) + "\">" + new DateFormatSymbols().getMonths()[i] + "</option>\n";
		}
		
		main_page_html += "   </select>\n"
				+ "			</form>"
				+ "	      <button id=\"report\" name=\"reportAjax\" onclick=\"doSimpleAjax('/Admin?type=report&reportAjax=true'); return false;\">Generate Report</button>"
				+ "<div id=\"result\"></div>";

		request.setAttribute("HTML_SUGGST_ADMIN", main_page_html);
		
	}
	
	private void ajaxReport(HttpServletRequest request, HttpServletResponse response, AdminModel model) throws ServletException, IOException {
		int startYear = Integer.valueOf(request.getParameter("startYear"));
		int startMonth = Integer.valueOf(request.getParameter("startMonth"));
		int endYear =  Integer.valueOf(request.getParameter("endYear"));
		int endMonth =  Integer.valueOf(request.getParameter("endMonth"));
		
		Map<String, Map<Book, Integer>> salesReport = model.listOfBooksSold(LocalDateTime.of(startYear, startMonth, 1, 0, 0, 0), LocalDateTime.of(endYear, endMonth, 1, 0, 0, 0));
			
		PrintWriter out = response.getWriter();
		String html = "<h3>Report range: " + startMonth + "/" + startYear + " - " +  endMonth + "/" + endYear + "</h3>";
		
		for (Map.Entry<String, Map<Book, Integer>> reportEntry : salesReport.entrySet()) {
			html +=   "	<h4 style=\"margin-bottom:0px\">Report for month: " + reportEntry.getKey() + "</h4>"
					+ "	<table class=\"report\" border=\"1\"> "
					+ "		<tr> "
					+ "			<th>Book Title</td>"
					+ "			<th>Book Cost</td> "
					+ "			<th>Books Sold</td>"
					+ "			<th>Total Sale</td> "
					+ "		</tr>";
			for (Map.Entry<Book, Integer> bookEntry : reportEntry.getValue().entrySet()) {
				html +=	"	<tr>" +
						"		<td>" + bookEntry.getKey().getTitle() + "</td>" +
						"		<td>" + bookEntry.getKey().getPrice() + "</td>" +
						"		<td>" + bookEntry.getValue() + "</td>" +
						"		<td>" + String.format("%.2f", bookEntry.getKey().getPrice() * bookEntry.getValue()) + "</td>" +
						"	</tr>"; 
			}
			html +=	"		</br>";
		}
		
		html += "</table>";
		
		out.printf(html);
		out.flush();
	}
	
	private void loadAnalytics(HttpServletRequest request, AdminModel model) {
		
		Map<Book, Integer> report = model.getTop10BooksAlltime();
		
		String html = "<h2>Welcome to the RT Analytics page!</h2>\n"
				+ "	<h3>View real time info about the top 10 books sold</h3>"
				+ "	<h4 style=\"margin-bottom:0px\">Top 10 books sold: </h4>"
					+ "	<table class=\"report\" border=\"1\"> "
					+ "		<tr> "
					+ "			<th>Book Title</td>"
					+ "			<th>Books Sold</td>"
					+ "		</tr>";
		
		for (Entry<Book, Integer> entry : report.entrySet()) {
			html +=	"	<tr>" +
					"		<td>" + entry.getKey().getTitle() + "</td>" +
					"		<td>" + entry.getValue() + "</td>" +
					"	</tr>"; 
		}
		
		request.setAttribute("HTML_SUGGST_ADMIN", html);
		
	}
	
	private void loadStats(HttpServletRequest request, AdminModel model) {

		String html = "<h2>Welcome to the stats page!</h2>\n"
				+ "	<h3>View user buying statistics the data is anonimized</h3>";
		
		Map<String, Double> report = model.buyingStats();
		
			html += "	<h4 style=\"margin-bottom:0px\">Top 10 books sold: </h4>"
			+ "	<table class=\"report\" border=\"1\"> "
			+ "		<tr> "
			+ "			<th>Customer</td>"
			+ "			<th>Total Paid</td>"
			+ "		</tr>";

		for (Entry<String, Double> entry : report.entrySet()) {
		html +=	"	<tr>" +
				"		<td>" + entry.getKey() + "</td>" +
				"		<td>" + String.format("%.2f", entry.getValue()) + "</td>" +
				"	</tr>"; 
		}
				
		
		request.setAttribute("HTML_SUGGST_ADMIN", html);
		
	}
}