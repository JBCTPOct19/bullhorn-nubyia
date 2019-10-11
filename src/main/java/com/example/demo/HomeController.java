package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    BullhornRepository bullhornRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String listBullhorn(Model model) {
        model.addAttribute("bullhorns", bullhornRepository.findAll());
        return "list";

    }

    @GetMapping("/add")
    public String bullform(Model model) {
        model.addAttribute("bullhorn", new Bullhorn());
        return "bullform";
    }

        @PostMapping("/add")
        public String processBullhorn(@ModelAttribute Bullhorn bullhorn,
                @RequestParam("file") MultipartFile file){
            if (file.isEmpty()){
                return "redirect:/add";
            }
            try {
                Map uploadResult = cloudc.upload(file.getBytes(),
                        ObjectUtils.asMap("resourcetype", "auto"));
                bullhorn.setHeadshot(uploadResult.get("url").toString());
                bullhornRepository.save(bullhorn);

            } catch (IOException e){
                e.printStackTrace();
                return "redirect:/add";
            }
            return "redirect:/";
        }
}