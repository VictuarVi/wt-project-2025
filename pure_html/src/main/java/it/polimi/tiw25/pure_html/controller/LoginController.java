package it.polimi.tiw25.pure_html.controller;

import it.polimi.tiw25.pure_html.DAO.UserDAO;
import it.polimi.tiw25.pure_html.entities.User;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/Login") // access this controller via action="" on the forms
public class LoginController extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    private Connection connection = null;

    @Override
    public void init() throws ServletException {
        try {
            ServletContext context = getServletContext();
            String driver = context.getInitParameter("dbDriver");
            String url = context.getInitParameter("dbUrl");
            String user = context.getInitParameter("dbUser");
            String password = context.getInitParameter("dbPassword");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new UnavailableException("Can't load database driver");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UnavailableException("Couldn't get db connection");
        }

        ServletContext servletContext = getServletContext();
        JakartaServletWebApplication webApplication = JakartaServletWebApplication.buildApplication(servletContext);
        WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(webApplication);

        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        JakartaServletWebApplication webApplication = JakartaServletWebApplication.buildApplication(getServletContext());
        WebContext ctx = new WebContext(webApplication.buildExchange(req, res), req.getLocale());

        templateEngine.process("index.html", ctx, res.getWriter());
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String nickname = req.getParameter("nickname");
        String password = req.getParameter("password");

        UserDAO userDAO = new UserDAO(connection);
        User schrondingerUser = null;
        try {
            schrondingerUser = userDAO.checkUser(nickname, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        req.getSession().setAttribute("user", schrondingerUser);

        if (schrondingerUser != null) {
            res.sendRedirect(getServletContext().getContextPath() + "/HomePage");
        } else {
            res.sendRedirect(getServletContext().getContextPath() + "/index.html");
        }
    }
}
