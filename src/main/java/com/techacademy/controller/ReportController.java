package com.techacademy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
import com.techacademy.entity.Report;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;


@Controller
@RequestMapping("reports")
public class ReportController {

    private final ReportService reportService;


    @Autowired
    public ReportController(ReportService reportService)
    {
        this.reportService = reportService;
    }

    // 日報一覧画面
    @GetMapping
    public String list(@AuthenticationPrincipal UserDetail userDetail,Model model) {

            model.addAttribute("listSize", reportService.find(userDetail).size());
            model.addAttribute("reportList", reportService.find(userDetail));

        return "reports/rlist";
    }

    // 日報新規登録画面
    @GetMapping(value = "/radd")
    public String reportcreate(@ModelAttribute Report report,@AuthenticationPrincipal UserDetail userDetail, Model model) {

        model.addAttribute("name", userDetail.getEmployee().getName());

        return "reports/rnew";
    }

    // 日報新規登録処理
    @PostMapping(value = "/radd")
    public String radd(@Validated Report report, BindingResult res, @AuthenticationPrincipal UserDetail userDetail, Model model) {

        // 入力チェック
        if (res.hasErrors()) {
            return reportcreate(report, userDetail, model);
        }

            ErrorKinds result = reportService.save(report, userDetail);

            if (ErrorMessage.contains(result)) {
                model.addAttribute(ErrorMessage.getErrorName(result),
                                   ErrorMessage.getErrorValue(result));

            return reportcreate(report, userDetail, model);
        }

        return "redirect:/reports";
    }

    // 日報詳細画面
    @GetMapping(value = "/{id}/")
    public String rdetail(@PathVariable Integer id, Model model) {

        model.addAttribute("report", reportService.findById(id));
        return "reports/rdetail";
    }

    // 日報削除処理
    @PostMapping(value = "/{id}/rdelete")
    public String delete(@PathVariable Integer id, @AuthenticationPrincipal UserDetail userDetail, Model model) {

        ErrorKinds result = reportService.delete(id);

        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            model.addAttribute("report", reportService.findById(id));
            return rdetail(id, model);
        }

        return "redirect:/reports";
    }

    // 日報更新画面
    @GetMapping(value = "/rupdate/{id}")
    public String getUser(@PathVariable Integer id,@AuthenticationPrincipal UserDetail userDetail, Model model) {
        // Modelにサービスから取得したuserを登録
        model.addAttribute("name", userDetail.getEmployee().getName());

        if (id != null) {
            model.addAttribute("report", reportService.findById(id));
        }

        return "reports/rupdate";
    }

    // 日報更新登録処理
    @PostMapping(value = "/rupdate/{id}")
    public String rupdate(@Validated Report report, BindingResult res, @AuthenticationPrincipal UserDetail userDetail, Model model) {

        // 入力チェック
        if (res.hasErrors()) {
            return getUser(null, userDetail, model);
        }

        ErrorKinds result = reportService.edit(report, userDetail);

        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result),
                               ErrorMessage.getErrorValue(result));
            return getUser(null, userDetail, model);
        }
        return "redirect:/reports";
    }



}
