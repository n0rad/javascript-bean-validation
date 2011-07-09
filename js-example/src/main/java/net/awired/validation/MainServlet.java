package net.awired.validation;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.awired.client.bean.validation.js.service.ValidationService;
import org.codehaus.jackson.map.ObjectMapper;

public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public MainServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        ValidationService validationService = new ValidationService();
        Object validationObject = validationService.getValidationObject(Person.class);

        ObjectMapper mapper = new ObjectMapper();
        String jsonContraints = mapper.writeValueAsString(validationObject);

        request.setAttribute("contraints", jsonContraints);

        String destination = "/form.jsp";
        RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        // TODO Auto-generated method stub
    }

}
