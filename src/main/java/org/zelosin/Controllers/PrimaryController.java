package org.zelosin.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PrimaryController {

    @RequestMapping(value = "/processing")
    public String processingHandler() {
        return "core/processing";
    }

    @RequestMapping(value = "/about")
    public String aboutHandler() {
        return "core/about";
    }

    @RequestMapping(value = "/contacts")
    public String contactsHandler() {
        return "core/contacts";
    }

    @RequestMapping(value = "/")
    public String primeHandler() {
        return "core/prime";
    }
}
