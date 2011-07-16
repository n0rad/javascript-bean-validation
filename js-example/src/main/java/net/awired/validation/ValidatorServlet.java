package net.awired.validation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ValidatorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ValidatorServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        // Get the absolute path of the image
        //        ServletContext sc = getServletContext();
        //        String filename = sc.getRealPath("Validator.js");

        //        // Get the MIME type of the image
        //        String mimeType = sc.getMimeType(filename);
        //        if (mimeType == null) {
        //            sc.log("Could not get MIME type of " + filename);
        //            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        //            return;
        //        }
        //
        //        // Set content type

        // Set content size
        try {
            response.setContentType("text/javascript");
            InputStream in = getClass().getResourceAsStream("/Validator.js");
            //            File file = new File(resource.toURI());
            //            response.setContentLength((int) file.length());

            // Open the file and output streams
            //            FileInputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();

            // Copy the contents of the file to the output stream
            byte[] buf = new byte[1024];
            int count = 0;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

    }

}
