package org.zelosin.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PrimaryController {

    @RequestMapping(value = "/processing")
    public String primeHandler(Model model) {
        return "core/processing";
    }

    @RequestMapping(value = "/about")
    public String aboutHandler(Model model) {
        return "core/about";
    }

    @RequestMapping(value = "/contacts")
    public String contactsHandler(Model model) {
        return "core/contacts";
    }
}
