package it.polimi.tiw25.pure_html.controller;

import it.polimi.tiw25.pure_html.Queries;
import it.polimi.tiw25.pure_html.entities.Playlist;
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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/HomePage")
public class HomepageController extends HttpServlet {
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
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        JakartaServletWebApplication webApplication = JakartaServletWebApplication.buildApplication(getServletContext());
        WebContext ctx = new WebContext(webApplication.buildExchange(req, res), req.getLocale());

        List<Playlist> playlists = null;
        try {
            playlists = getUserPlaylists();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ctx.setVariable("playlists", playlists);
        templateEngine.process("index.html", ctx, res.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        super.doPost(req, res);
    }

    /**
     * Get all Playlists of a User.
     *
     * @return List of Playlists
     * @throws SQLException
     */
    private List<Playlist> getUserPlaylists() throws SQLException {
        List<Playlist> playlists = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(Queries.GET_USER_PLAYLISTS);
            statement.setString(1, "username");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        assert statement != null;

        ResultSet result = null;
        try {
            result = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        assert result != null;

        while (result.next()) {
            User user = new User(
                    result.getString("user")
            );
            Playlist playlist = new Playlist(
                    result.getInt("id"),
                    result.getString("title"),
                    result.getInt("creation_date"),
                    user
            );
            playlists.add(playlist);
        }

        return playlists;
    }
}
