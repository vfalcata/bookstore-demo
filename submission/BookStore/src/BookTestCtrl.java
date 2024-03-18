

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import data.beans.Book;
import data.beans.Customer;
import data.beans.Id;
import data.beans.PurchaseOrder;
import data.beans.Review;
import data.beans.SiteUser;
import data.beans.Visitor;
import data.dao.BookDAO;
import data.dao.CustomerDAO;
import data.dao.PurchaseOrderDAO;
import data.dao.ReviewDAO;
import data.dao.UpdateBook;
import data.dao.UpdateCustomer;
import data.dao.UpdateReview;
import data.dao.VisitorDAO;
import data.query.DataObjectCompiler;

/**
 * Servlet implementation class BookTestCtrl
 */
@WebServlet("/BookTestCtrl")
public class BookTestCtrl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookTestCtrl() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ReviewDAO revdao=new ReviewDAO();
		
		SiteUser siteUser=(SiteUser)new Customer.Builder().withId(new Id("fakeeCustID")).build();
		Book book=new Book.Builder().withId(new Id("fakeeBookID")).build();
		Review review=new Review.Builder().withBook(book).withCustomer((Customer)siteUser).build();
		revdao.newUpdateRequest()
		.requestUpdateReview(siteUser,review)
		.updateReviewBody("newBODy")
		.updateReviewRating(3)
		.updateReviewTitle("new Title")
		.executeUpdate();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
