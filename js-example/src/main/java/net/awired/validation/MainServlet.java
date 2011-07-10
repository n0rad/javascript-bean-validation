package net.awired.validation;

import java.io.IOException;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import net.awired.client.bean.validation.js.service.ValidationService;
import net.awired.validation.entity.Address;
import net.awired.validation.entity.Person;
import net.awired.validation.entity.Work;
import org.codehaus.jackson.map.ObjectMapper;

public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public MainServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        doPost(request, response);

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

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

        Person person = new Person();
        person.setLastname("sdfghj");
        person.setFirstname("SDFGHJ");

        Work work = new Work();
        work.setCompanyName("COMPANYNAME");
        person.setCurrentWork(work);

        Address address = new Address();
        address.setCity("city");
        //address.setCode("code");
        address.setStreet("sdfgiè-trd");
        person.getAddresses().add(address);

        Validator v = validatorFactory.getValidator();
        Set<ConstraintViolation<Person>> violation = v.validate(person);
        System.out.println();
    }

}
